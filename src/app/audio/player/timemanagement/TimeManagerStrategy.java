package app.audio.player.timemanagement;

import static app.searchbar.SearchType.PODCAST;

import app.Constants;
import app.audio.player.AudioPlayer;
import app.audio.player.states.PlayerPlayPauseStates;
import app.history.History;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.NextPrevOutputNode;
import app.io.nodes.output.PlayerOutputNode;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Episode;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class which represents a strategy for managing time in an audio player. It provides
 * methods for updating time, getting remaining time, getting the currently playing audio entity,
 * and navigating to the next or previous audio entity.
 */
@Getter
@Setter
public abstract class TimeManagerStrategy {
  private Long lastTimeUpdated = 0L;

  /**
   * Serves as a track bar, indicating the point reached in the playback.
   *
   * <p>The {@link #elapsedTime} value is updated to reflect the current position in the playback.
   */
  private Long elapsedTime = 0L;

  /**
   * Constructs a PlaylistTimeManagerStrategy with a specified starting time.
   *
   * @param startingTime The initial time to set for the strategy.
   */
  public TimeManagerStrategy(final Long startingTime) {
    this.setLastTimeUpdated(startingTime);
    this.setElapsedTime(0L);
  }

  /**
   * Adds the specified time to the current elapsed time of the audio player.
   *
   * @param audioPlayer The audio player instance.
   * @param timeToAdd   The time to add, in seconds.
   * @return
   */
  public abstract History addTime(AudioPlayer audioPlayer, long timeToAdd);

  /**
   * Calculates the remaining time for the currently playing {@link
   * library.entities.audio.audioFiles.AudioFile AudioFile} (song/episode) in the audio player.
   *
   * @param audioPlayer The audio player whose current audio file's remaining time will be
   *     calculated.
   * @return The remaining time in seconds for the current {@link
   *     library.entities.audio.audioFiles.AudioFile AudioFile}.
   */
  public abstract long getRemainingTime(AudioPlayer audioPlayer);

  /**
   * Retrieves the current playing {@link library.entities.audio.audioFiles.AudioFile AudioFile}
   * (song/episode) from the audio player.
   *
   * @param player The audio player from which the current playing audio entity is retrieved.
   * @return The currently playing {@link library.entities.audio.audioFiles.AudioFile AudioFile},
   *      or null if none is playing.
   */
  public abstract AudioEntity getPlayingAudioEntity(AudioPlayer player);

  /**
   * <b>
   *
   * <h2>ONLY FOR PLAYLIST</h2>
   *
   * </b> Moves to the next track in the playlist. Updates the current playing time and adjusts the
   * playing state accordingly. If the playlist reaches its end, it either repeats from the start or
   * stops based on the player's repeat settings.
   *
   * @param audioPlayer The AudioPlayer instance managing the playlist.
   * @param command The InputNode command that triggered this action.
   * @return A {@link app.io.nodes.output.NextPrevOutputNode} containing information about the action's result,
   *     including a message indicating the successful move to the next track or an error message if
   *     no track is loaded.
   */
  public NextPrevOutputNode next(final AudioPlayer audioPlayer, final InputNode command) {
    if (!audioPlayer.hasLoadedTrack()) {
      return new NextPrevOutputNode(command, Constants.NEXT_ERROR_MESSAGE);
    }

    long remainingTime = getRemainingTime(audioPlayer);
    addTime(audioPlayer, remainingTime);
    audioPlayer.setPlayPauseState(PlayerPlayPauseStates.PLAYING);

    if (audioPlayer.hasLoadedTrack() && getRemainingTime(audioPlayer) <= 0) {
      audioPlayer.stopPlayback();
      return new NextPrevOutputNode(command, Constants.NEXT_ERROR_MESSAGE);
    }

    return new NextPrevOutputNode(
        command,
        Constants.NEXT_NO_ERROR_MESSAGE
            + getPlayingAudioEntity(audioPlayer).getName()
            + Constants.PHRASE_TERMINATOR);
  }

  /**
   * <b>
   *
   * <h2>ONLY FOR PLAYLIST</h2>
   *
   * </b>
   *
   * <p>Moves to the previous track in the playlist. If the current track has played for some time,
   * it rewinds to the beginning of the current track. If it's the beginning of the track, it moves
   * to the previous track in the playlist.
   *
   * @param player The AudioPlayer instance managing the playlist.
   * @param command The InputNode command that triggered this action.
   * @return A {@link app.io.nodes.output.NextPrevOutputNode} containing information about the action's result,
   *     including a message indicating the successful move to the previous track or an error
   *     message if no track is loaded.
   */
  public abstract NextPrevOutputNode prev(AudioPlayer player, InputNode command);

  /**
   * <b>
   *
   * <h2>ONLY FOR PODCAST</h2>
   *
   * </b> Moves the playback 90 seconds forward in a podcast.
   *
   * @param command The input command for moving forward.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} indicating the result of the forward action.
   */
  public PlayerOutputNode forward(final AudioPlayer player, final InputNode command) {
    Podcast podcast = (Podcast) player.getLoadedTrack();
    Episode currentEpisode = (Episode) this.getPlayingAudioEntity(player);
    Integer endOfEpisodeDuration = podcast.getEndOfEpisodeDuration(currentEpisode);

    long forwardTime = this.getElapsedTime() + Constants.FORWARD_TIME;

    if (forwardTime >= endOfEpisodeDuration) {
      this.setElapsedTime((long) (endOfEpisodeDuration + 1));
    } else {
      this.setElapsedTime(forwardTime);
    }

    return new PlayerOutputNode(command, Constants.FORWARD_NO_ERROR_MESSAGE);
  }

  /**
   * <b>
   *
   * <h2>ONLY FOR PODCAST</h2>
   *
   * </b> Moves the playback 90 seconds backward in a podcast.
   *
   * @param command The input command for moving backward.
   * @return A {@link app.io.nodes.output.PlayerOutputNode} indicating the result of the backward action.
   */
  public PlayerOutputNode backward(final AudioPlayer player, final InputNode command) {
    if (!player.hasLoadedTrack()) {
      return new PlayerOutputNode(command, Constants.BACKWARD_ERROR_MESSAGE);
    }

    if (!player.getLoadedTrack().getType().equals(PODCAST)) {
      return new PlayerOutputNode(command, Constants.LOADED_SOURCE_NOT_PODCAST_ERROR);
    }

    Podcast podcast = (Podcast) player.getLoadedTrack();
    Episode currentEpisode = (Episode) this.getPlayingAudioEntity(player);
    Integer beginningOfEpisodeDuration = podcast.getBeginningOfEpisodeDuration(currentEpisode);

    long backwardTime = this.getElapsedTime() - Constants.BACKWARD_TIME;

    if (backwardTime <= beginningOfEpisodeDuration) {
      this.setElapsedTime((long) (beginningOfEpisodeDuration + 1));
    } else {
      this.setElapsedTime(backwardTime);
    }

    return new PlayerOutputNode(command, Constants.BACKWARD_NO_ERROR_MESSAGE);
  }
}
