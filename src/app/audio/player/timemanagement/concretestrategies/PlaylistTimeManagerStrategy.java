package app.audio.player.timemanagement.concretestrategies;

import app.Constants;
import app.audio.player.AudioPlayer;
import app.audio.player.states.PlayerPlayPauseStates;
import app.audio.player.states.PlayerPlaylistRepeatStates;
import app.audio.player.states.PlayerSongPodcastRepeatStates;
import app.audio.player.timemanagement.TimeManagerStrategy;
import app.history.History;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.NextPrevOutputNode;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;

/**
 * The PlaylistTimeManagerStrategy class is a concrete implementation of the TimeManagerStrategy
 * abstract class. It provides time management functionality for a playlist in an audio player.
 */
public final class PlaylistTimeManagerStrategy extends TimeManagerStrategy {
  public PlaylistTimeManagerStrategy(final Long startingTime) {
    super(startingTime);
  }

  /**
   * Adds the specified time to the current elapsed time of the audio player. The behavior of adding
   * time depends on the repeat playlist state of the audio player:
   *
   * <ul>
   *   <li>
   *       <p>If the repeat playlist state is set to REPEAT_CURRENT_SONG, the time is added in a way
   *       so elapsedTime doesn't go over the current playing song.
   *   <li>If the repeat playlist state is set to REPEAT_ALL, the time is added to the overall
   *       playlist elapsed time and then modulo is applied for keeping it cyclic.
   *   <li>If the repeat playlist state is set to NO_REPEAT, the time is simply added to the current
   *       elapsed time.
   * </ul>
   *
   * @param audioPlayer The audio player instance.
   * @param timeToAdd   The time to add, in seconds.
   * @return
   */
  @Override
  public History addTime(final AudioPlayer audioPlayer, final long timeToAdd) {
    long timeToAddCopy = timeToAdd;
    Playlist playlist = getPlaylistFromPlayer(audioPlayer);
    History history = new History();

    if (audioPlayer.getRepeatPlaylistStates() == PlayerPlaylistRepeatStates.REPEAT_CURRENT_SONG) {
      Song currentSong = (Song) getPlayingAudioEntity(audioPlayer);
      int beginningOfSongTime = playlist.getBeginningOfSongDuration(currentSong);
      int currentTimeInSong = (int) (getElapsedTime() - beginningOfSongTime);
      assert currentSong != null;

      int numberOfLoops = (int) ((currentTimeInSong + timeToAdd) / currentSong.getDuration());
      while (numberOfLoops != 0) {
        history.add(currentSong);
        numberOfLoops--;
      }

      // Make sure the playback doesn't go over the current song and loops inside it
      int repeatTimeInSong = (int) ((currentTimeInSong + timeToAdd) % currentSong.getDuration());
      setElapsedTime((long) (beginningOfSongTime + repeatTimeInSong));
    } else if (audioPlayer.getRepeatPlaylistStates() == PlayerPlaylistRepeatStates.REPEAT_ALL) {
      while (timeToAddCopy > 0) {
        long timeToFinish = Math.min(getRemainingTime(audioPlayer), timeToAdd);

        long timeToCheck = getRemainingTime(audioPlayer);
        setElapsedTime((getElapsedTime() + timeToFinish) % playlist.getDuration());

        if (timeToFinish >= timeToCheck) {
          Song currentSong = (Song) getPlayingAudioEntity(audioPlayer);
          history.add(currentSong);
        }

        timeToAddCopy -= timeToFinish;
      }
    } else {
      while (timeToAddCopy > 0) {
        long timeToFinish = Math.min(getRemainingTime(audioPlayer), timeToAdd);
        if (timeToFinish == 0)
          timeToFinish = timeToAddCopy;

        long timeToCheck = getRemainingTime(audioPlayer);
        setElapsedTime(getElapsedTime() + timeToFinish);

        Song currentSong = (Song) getPlayingAudioEntity(audioPlayer);
        if (currentSong != null && timeToFinish >= timeToCheck &&
                (currentSong.getDuration() == getRemainingTime(audioPlayer))) {
          history.add(currentSong);
        }
        timeToAddCopy -= timeToFinish;
      }
    }

    return history;
  }

  @Override
  public AudioEntity getPlayingAudioEntity(final AudioPlayer player) {
    long elapsedTime = getElapsedTime();
    Playlist playlist = getPlaylistFromPlayer(player);

    if (playlist == null || playlist.getSongs().isEmpty()) {
      return null;
    }

    if (player.getRepeatSongPodcastStates() == PlayerSongPodcastRepeatStates.NO_REPEAT) {
      return findCurrentSong(elapsedTime, playlist);
    }

    elapsedTime %= playlist.getDuration();
    return findCurrentSong(elapsedTime, playlist);
  }

  /**
   * Calculates the remaining time of the current song in the playlist.
   *
   * @param audioPlayer the audio player
   * @return the remaining time in seconds
   */
  @Override
  public long getRemainingTime(final AudioPlayer audioPlayer) {
    Playlist playlist = getPlaylistFromPlayer(audioPlayer);
    Song playingSong = (Song) getPlayingAudioEntity(audioPlayer);

    assert playlist != null;
    long elapsedTime = getElapsedTime();
    long remainingTime;

    remainingTime = playlist.getEndOfSongDuration(playingSong) - elapsedTime;
    return remainingTime;
  }

  /**
   * Retrieves the playlist from the given AudioPlayer.
   *
   * @param player The AudioPlayer instance.
   * @return The Playlist object obtained from the player.
   */
  private Playlist getPlaylistFromPlayer(final AudioPlayer player) {
    AudioEntity loadedTrack = player.getLoadedTrack();
    return (Playlist) loadedTrack;
  }

  /**
   * Finds the current song based on the elapsed time and the playlist.
   *
   * @param elapsedTime The elapsed time in seconds.
   * @param playlist The playlist containing the songs.
   * @return The current song or null if no song is found.
   */
  private AudioEntity findCurrentSong(final long elapsedTime, final Playlist playlist) {
    long currentTime = 0;
    Song song;

    for (Integer index : playlist.getPlayingOrder()) {
      song = playlist.getSongs().get(index);
      if (song.getDuration() + currentTime <= elapsedTime) {
        currentTime += song.getDuration();
      } else {
        return song;
      }
    }
    return null;
  }

  @Override
  public NextPrevOutputNode prev(final AudioPlayer player, final InputNode command) {
    if (!player.hasLoadedTrack()) {
      return new NextPrevOutputNode(command, Constants.PREV_ERROR_MESSAGE);
    }

    Playlist playlist = getPlaylist(player);
    Song playingSong = getPlayingSong(player);
    long timeToRewind = calculateRewindTime(playingSong, player);

    if (timeToRewind > 0) {
      rewindToPreviousSong(timeToRewind);
    } else {
      if (isFirstSong(playlist, playingSong)) {
        setElapsedTime(0L);
      } else {
        rewindToBeginningOfPreviousSong(player, playlist);
      }
    }

    player.setPlayPauseState(PlayerPlayPauseStates.PLAYING);

    if (player.hasLoadedTrack() && getRemainingTime(player) <= 0) {
      player.stopPlayback();
      return new NextPrevOutputNode(command, Constants.PREV_ERROR_MESSAGE);
    }

    return new NextPrevOutputNode(
        command,
        Constants.PREV_NO_ERROR_MESSAGE
            + getPlayingAudioEntity(player).getName()
            + Constants.PHRASE_TERMINATOR);
  }

  private Playlist getPlaylist(final AudioPlayer player) {
    return (Playlist) player.getLoadedTrack();
  }

  private Song getPlayingSong(final AudioPlayer player) {
    return (Song) getPlayingAudioEntity(player);
  }

  private long calculateRewindTime(final Song playingSong, final AudioPlayer player) {
    assert playingSong != null;
    long remainingTime = getRemainingTime(player);
    return Math.max(0, playingSong.getDuration() - remainingTime);
  }

  private void rewindToPreviousSong(final long timeToRewind) {
    setElapsedTime(getElapsedTime() - timeToRewind);
  }

  private boolean isFirstSong(final Playlist playlist, final Song playingSong) {
    return playlist.getIndexInPlayOrder(playingSong) == 0;
  }

  private void rewindToBeginningOfPreviousSong(final AudioPlayer player, final Playlist playlist) {
    Song playingSong = getPlayingSong(player);
    long timeToRewind = playlist.getBeginningOfSongDuration(playingSong) - 1;
    setElapsedTime(timeToRewind);

    playingSong = getPlayingSong(player);
    assert playingSong != null;
    timeToRewind = playlist.getBeginningOfSongDuration(playingSong);
    setElapsedTime(timeToRewind);
  }
}
