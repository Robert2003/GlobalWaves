package app.audio.player;

import static app.searchbar.SearchType.ALBUM;
import static app.searchbar.SearchType.NOT_INITIALIZED;
import static app.searchbar.SearchType.PLAYLIST;
import static app.searchbar.SearchType.PODCAST;
import static app.searchbar.SearchType.SONG;

import app.Constants;
import app.audio.player.states.PlayerPlayPauseStates;
import app.audio.player.states.PlayerPlaylistRepeatStates;
import app.audio.player.states.PlayerSongPodcastRepeatStates;
import app.audio.player.states.ShuffleStates;
import app.audio.player.timemanagement.TimeManagerStrategy;
import app.audio.player.timemanagement.TimeManagerStrategyFactory;
import app.helpers.ConnectionStatus;
import app.history.History;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.NextPrevOutputNode;
import app.io.nodes.output.PlayerOutputNode;
import app.io.nodes.output.StatusOutputNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audio.collections.SongAudioCollection;
import library.entities.audio.audioFiles.AudioFile;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class AudioPlayer {
  private TimeManagerStrategy timeManager;
  private AudioEntity loadedTrack = null;
  private PlayerPlayPauseStates playPauseState = PlayerPlayPauseStates.STOPPED;
  private PlayerPlaylistRepeatStates repeatPlaylistStates = PlayerPlaylistRepeatStates.NO_REPEAT;
  private PlayerSongPodcastRepeatStates repeatSongPodcastStates =
      PlayerSongPodcastRepeatStates.NO_REPEAT;
  private ShuffleStates shuffleStates = ShuffleStates.OFF;
  private List<Integer> playingOrder = null;
  private HashMap<Podcast, Long> podcastHistory = new HashMap<>();
  private Random random;

  /** Empties the player, resetting its state and clearing the loaded track. */
  public void stopPlayback() {
    if (hasLoadedTrack() && this.getLoadedTrack().getType().equals(PODCAST)) {
      podcastHistory.put((Podcast) this.getLoadedTrack(), timeManager.getElapsedTime());
    }

    if (hasLoadedTrack() && this.getLoadedTrack().getType().equals(PLAYLIST)) {
      SongAudioCollection playlist = (SongAudioCollection) getLoadedTrack();
      for (int i = 0; i < playlist.getPlayingOrder().size(); i++) {
        playlist.getPlayingOrder().set(i, i);
      }
    }

    this.setPlayPauseState(PlayerPlayPauseStates.STOPPED);
    this.setRepeatPlaylistStates(PlayerPlaylistRepeatStates.NO_REPEAT);
    this.setRepeatSongPodcastStates(PlayerSongPodcastRepeatStates.NO_REPEAT);
    this.setShuffleStates(ShuffleStates.OFF);

    this.setPlayingOrder(null);
    if (this.getTimeManager() != null) {
      this.getTimeManager().setElapsedTime(0L);
    }
  }

  /**
   * Loads a track or audio entity based on the given input command.
   *
   * @param command The input command used to specify the loading action.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} containing the result of the load operation.
   */
  public PlayerOutputNode load(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (hasLoadedTrack() && this.getLoadedTrack().getType().equals(PODCAST)) {
      podcastHistory.put((Podcast) this.getLoadedTrack(), timeManager.getElapsedTime());
    }

    assert user != null;
    if (!user.getSearchBar().hasSelectedTrack()) {
      return new PlayerOutputNode(command, Constants.LOAD_ERROR_MESSAGE);
    }

    AudioEntity selectedEntity = user.getSearchBar().getSelectedTrack().getSelectedEntity();
    checkRetrieveFromHistory(command, selectedEntity);

    this.setLoadedTrack(selectedEntity);
    this.getLoadedTrack().setStartTimestamp(command.getTimestamp());
    this.setPlayPauseState(PlayerPlayPauseStates.PLAYING);
    user.getSearchBar().setSelectedTrack(null);

    if (getLoadedTrack().getType().equals(PLAYLIST) || getLoadedTrack().getType().equals(ALBUM)) {
      resetShuffleOrder();
      ((SongAudioCollection) getLoadedTrack()).setPlayingOrder(getPlayingOrder());
    }

    AudioEntity playingEntity = getTimeManager().getPlayingAudioEntity(this);
    user.getHistory().add(playingEntity, command.getTimestamp());

    return new PlayerOutputNode(command, Constants.LOAD_NO_ERROR_MESSAGE);
  }

  /**
   * Toggles between play and pause states.
   *
   * @param command The input command used to toggle play/pause.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} containing the result of the toggle operation.
   */
  public PlayerOutputNode togglePlayPause(final InputNode command) {
    String message = null;

    if (!this.hasLoadedTrack()) {
      return new PlayerOutputNode(command, Constants.PLAY_PAUSE_ERROR_MESSAGE);
    }

    if (this.getPlayPauseState().equals(PlayerPlayPauseStates.PAUSED)) {
      this.setPlayPauseState(PlayerPlayPauseStates.PLAYING);
      message = Constants.PLAY_NO_ERROR_MESSAGE;
    } else if (this.getPlayPauseState().equals(PlayerPlayPauseStates.PLAYING)) {
      this.setPlayPauseState(PlayerPlayPauseStates.PAUSED);
      message = Constants.PAUSE_NO_ERROR_MESSAGE;
    }

    return new PlayerOutputNode(command, message);
  }

  /**
   * Cycles through repeat states for the current audio entity:
   *
   * <ul>
   *   <li><b>Podcast/Song:</b> NO_REPEAT, REPEAT_ONCE, REPEAT_INFINITE
   *   <li><b>SongAudioCollection:</b> NO_REPEAT, REPEAT_ALL, REPEAT_CURRENT_SONG
   * </ul>
   *
   * @param command The input command for changing repeat state.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} indicating the new repeat state.
   */
  public PlayerOutputNode repeat(final InputNode command) {
    String message = null;

    if (!this.hasLoadedTrack()) {
      return new PlayerOutputNode(command, Constants.REPEAT_ERROR_MESSAGE);
    }

    this.cycleRepeatState();
    switch (this.getLoadedTrack().getType()) {
      case SONG:
      case PODCAST:
        message = repeatSongPodcastStates.getDescription().toLowerCase();
        break;
      case PLAYLIST:
      case ALBUM:
        message = repeatPlaylistStates.getDescription().toLowerCase();
        break;
      default:
        break;
    }
    return new PlayerOutputNode(command, Constants.REPEAT_NO_ERROR_MESSAGE + message + ".");
  }

  /**
   * Toggles between shuffle ON and shuffle OFF in a playlist
   *
   * @param command The input command used to shuffle the playlist.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} containing the result of the shuffle operation.
   */
  public PlayerOutputNode shuffle(final InputNode command) {
    String message;

    if (!this.hasLoadedTrack()) {
      return new PlayerOutputNode(command, Constants.SHUFFLE_ERROR_MESSAGE);
    }

    if (!this.getLoadedTrack().getType().equals(PLAYLIST)
        && !this.getLoadedTrack().getType().equals(ALBUM)) {
      return new PlayerOutputNode(command, Constants.LOADED_SOURCE_NOT_PLAYLIST_ERROR);
    }

    this.setShuffleStates(this.getShuffleStates().next());

    if (this.getShuffleStates().equals(ShuffleStates.ON)) {
      message = Constants.SHUFFLE_ACTIVATE_NO_ERROR_MESSAGE;

      if (command.getSeed() != null) {
        random = new Random(command.getSeed());
      }

      List<Integer> playOrder = new ArrayList<>();
      for (int i = 0; i < ((SongAudioCollection) this.getLoadedTrack()).getSongs().size(); i++) {
        playOrder.add(i);
      }
      Collections.shuffle(playOrder, random);

      long newElapsedTime = findStartShuffleRealTime(playOrder);
      getTimeManager().setElapsedTime(newElapsedTime);

      setPlayingOrder(playOrder);
      ((SongAudioCollection) getLoadedTrack()).setPlayingOrder(playOrder);
    } else {
      message = Constants.SHUFFLE_DEACTIVATE_NO_ERROR_MESSAGE;
      long newElapsedTime = findStopShuffleRealTime();
      getTimeManager().setElapsedTime(newElapsedTime);

      resetShuffleOrder();
      ((SongAudioCollection) getLoadedTrack()).setPlayingOrder(getPlayingOrder());
    }

    return new PlayerOutputNode(command, message);
  }

  /**
   * Provides the current status of the audio player.
   *
   * @param command The input command for requesting the status.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} with the current status.
   */
  public PlayerOutputNode status(final InputNode command) {
    if (!this.hasLoadedTrack()) {
      StatusOutputNode stats = new StatusOutputNode();
      return new PlayerOutputNode(command, stats);
    }

    StatusOutputNode stats = new StatusOutputNode(this);
    return new PlayerOutputNode(command, stats);
  }

  /**
   * Moves the playback 90 seconds forward in a podcast.
   *
   * @param command The input command for moving forward.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} indicating the result of the forward action.
   */
  public PlayerOutputNode forward(final InputNode command) {
    if (!this.hasLoadedTrack()) {
      return new PlayerOutputNode(command, Constants.FORWARD_ERROR_MESSAGE);
    }

    if (!this.getLoadedTrack().getType().equals(PODCAST)) {
      return new PlayerOutputNode(command, Constants.LOADED_SOURCE_NOT_PODCAST_ERROR);
    }

    return timeManager.forward(this, command);
  }

  /**
   * Moves the playback 90 seconds backward in a podcast.
   *
   * @param command The input command for moving backward.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} indicating the result of the backward action.
   */
  public PlayerOutputNode backward(final InputNode command) {
    return timeManager.backward(this, command);
  }

  /**
   * Wrapper for next command
   *
   * @param command The InputNode command that triggered this action.
   * @return A {@link app.io.nodes.output.NextPrevOutputNode} containing information about the action's result,
   *     including a message indicating the successful move to the next track or an error message if
   *     no track is loaded.
   */
  public NextPrevOutputNode next(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    NextPrevOutputNode out = timeManager.next(this, command);

    AudioEntity entity = timeManager.getPlayingAudioEntity(user.getAudioPlayer());
    user.getHistory().add(entity, command.getTimestamp());

    return out;
  }

  /**
   * Wrapper for prev command
   *
   * @param command The InputNode command that triggered this action.
   * @return A {@link app.io.nodes.output.NextPrevOutputNode} containing information about the action's result,
   *     including a message indicating the successful move to the previous track or an error
   *     message if no track is loaded.
   */
  public NextPrevOutputNode prev(final InputNode command) {
    if (timeManager != null) {
      User user = Library.getInstance().getUserByName(command.getUsername());
      NextPrevOutputNode out = timeManager.prev(this, command);

      AudioEntity entity = timeManager.getPlayingAudioEntity(user.getAudioPlayer());
      user.getHistory().add(entity, command.getTimestamp());

      return out;
    }
    return null;
  }

  /**
   * Determines if there is a loaded track.
   *
   * @return {@code true} if there is a loaded track, {@code false} otherwise.
   */
  public boolean hasLoadedTrack() {
    return this.getLoadedTrack() != null
        && this.getLoadedTrack().getType() != NOT_INITIALIZED
        && this.getPlayPauseState() != PlayerPlayPauseStates.STOPPED;
  }

  /// HELPER METHODS ///

  /** Resets the playing order of the playlist to be non-shuffled. */
  private void resetShuffleOrder() {
    playingOrder = new ArrayList<>();
    if (this.getLoadedTrack().getType() == PLAYLIST || this.getLoadedTrack().getType() == ALBUM) {
      for (int i = 0; i < ((SongAudioCollection) this.getLoadedTrack()).getSongs().size(); i++) {
        playingOrder.add(i);
      }
    }
  }

  /**
   * Checks and retrieves audio entity from the history if applicable.
   *
   * @param command The input command.
   * @param selectedEntity The selected audio entity.
   */
  private void checkRetrieveFromHistory(final InputNode command, final AudioEntity selectedEntity) {
    try {
      timeManager = TimeManagerStrategyFactory.createTimeManagerStrategy(command);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return;
    }

    if (selectedEntity.getType().equals(PODCAST)) {
      Podcast podcast = (Podcast) selectedEntity;
      if (podcastHistory.containsKey(podcast)) {
        timeManager.setElapsedTime(podcastHistory.get(podcast));
      }
    }
  }

  /** Cycles the repeat state of the current audio entity. */
  private void cycleRepeatState() {
    if (!this.hasLoadedTrack()) {
      return;
    }

    switch (this.getLoadedTrack().getType()) {
      case SONG:
      case PODCAST:
        this.setRepeatSongPodcastStates(this.getRepeatSongPodcastStates().next());
        break;
      case PLAYLIST:
        this.setRepeatPlaylistStates(this.getRepeatPlaylistStates().next());
        break;
      default:
        break;
    }
  }

  /**
   * Finds the real elapsed time when shuffle is stopped.
   *
   * @return The new elapsed time after shuffle is stopped.
   */
  private long findStopShuffleRealTime() {
    Song currentSong = (Song) getTimeManager().getPlayingAudioEntity(this);
    int remainingTime = (int) getTimeManager().getRemainingTime(this);
    SongAudioCollection playlist = (SongAudioCollection) getLoadedTrack();
    long newElapsedTIme = 0;

    for (Song s : playlist.getSongs()) {
      if (!s.equals(currentSong)) {
        newElapsedTIme += s.getDuration();
      } else {
        newElapsedTIme += (s.getDuration() - remainingTime);
        return newElapsedTIme;
      }
    }
    return newElapsedTIme;
  }

  /**
   * Finds the real elapsed time when shuffle starts.
   *
   * @param songPlayingOrder The new playing order after shuffle.
   * @return The new elapsed time after shuffle starts.
   */
  private long findStartShuffleRealTime(final List<Integer> songPlayingOrder) {
    Song currentSong = (Song) getTimeManager().getPlayingAudioEntity(this);
    int remainingTime = (int) getTimeManager().getRemainingTime(this);
    if (getLoadedTrack().getType().equals(PLAYLIST)) {
      SongAudioCollection playlist = (SongAudioCollection) getLoadedTrack();
      long newElapsedTIme = 0;
      Song s;

      for (Integer index : songPlayingOrder) {
        s = playlist.getSongs().get(index);
        if (!s.equals(currentSong)) {
          newElapsedTIme += s.getDuration();
        } else {
          newElapsedTIme += (s.getDuration() - remainingTime);
          return newElapsedTIme;
        }
      }
      return newElapsedTIme;
    } else if (getLoadedTrack().getType().equals(ALBUM)) {
      Album album = (Album) getLoadedTrack();
      long newElapsedTIme = 0;
      Song s;

      for (Integer index : songPlayingOrder) {
        s = album.getSongs().get(index);
        if (!s.equals(currentSong)) {
          newElapsedTIme += s.getDuration();
        } else {
          newElapsedTIme += (s.getDuration() - remainingTime);
          return newElapsedTIme;
        }
      }
      return newElapsedTIme;
    }

    return 0;
  }

  /**
   * Updates the current time of the audio player, based on its play/pause state and the command's
   * timestamp.
   *
   * @param command The input command containing the current timestamp.
   */
  public void updateTime(final InputNode command, final User user) {
    if (!this.hasLoadedTrack() || this.getTimeManager() == null) {
      return;
    }

    if (user.getConnectionStatus() == ConnectionStatus.ONLINE
        && this.getPlayPauseState() == PlayerPlayPauseStates.PLAYING) {
      History history = getTimeManager()
          .addTime(this, command.getTimestamp() - getTimeManager().getLastTimeUpdated());
      user.getHistory().add(history, command.getTimestamp());
    }
    getTimeManager().setLastTimeUpdated(command.getTimestamp());

    if (getTimeManager().getRemainingTime(this) <= 0) {
      this.stopPlayback();
    }
  }
}
