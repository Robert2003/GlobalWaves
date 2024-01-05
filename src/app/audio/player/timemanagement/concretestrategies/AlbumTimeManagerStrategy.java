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
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audioFiles.Song;

/**
 * The AlbumTimeManagerStrategy class is a concrete implementation of the TimeManagerStrategy
 * abstract class. It provides time management functionality for a album in an audio player.
 */
public final class AlbumTimeManagerStrategy extends TimeManagerStrategy {
  public AlbumTimeManagerStrategy(final Long startingTime) {
    super(startingTime);
  }

  /**
   * Adds the specified time to the current elapsed time of the audio player. The behavior of adding
   * time depends on the repeat album state of the audio player:
   *
   * <ul>
   *   <li>
   *       <p>If the repeat album state is set to REPEAT_CURRENT_SONG, the time is added in a way so
   *       elapsedTime doesn't go over the current playing song.
   *   <li>If the repeat album state is set to REPEAT_ALL, the time is added to the overall album
   *       elapsed time and then modulo is applied for keeping it cyclic.
   *   <li>If the repeat album state is set to NO_REPEAT, the time is simply added to the current
   *       elapsed time.
   * </ul>
   *
   * @param audioPlayer The audio player instance.
   * @param timeToAdd   The time to add, in seconds.
   * @return
   */
//  @Override
//  public History addTime(final AudioPlayer audioPlayer, final long timeToAdd) {
//    Album album = getAlbumFromPlayer(audioPlayer);
//    History history = new History();
//
//    if (audioPlayer.getRepeatPlaylistStates() == PlayerPlaylistRepeatStates.REPEAT_CURRENT_SONG) {
//      Song currentSong = (Song) getPlayingAudioEntity(audioPlayer);
//      int beginningOfSongTime = album.getBeginningOfSongDuration(currentSong);
//      int currentTimeInSong = (int) (getElapsedTime() - beginningOfSongTime);
//      assert currentSong != null;
//      // Make sure the playback doesn't go over the current song and loops inside it
//      int repeatTimeInSong = (int) ((currentTimeInSong + timeToAdd) % currentSong.getDuration());
//      setElapsedTime((long) (beginningOfSongTime + repeatTimeInSong));
//    } else if (audioPlayer.getRepeatPlaylistStates() == PlayerPlaylistRepeatStates.REPEAT_ALL) {
//      setElapsedTime((getElapsedTime() + timeToAdd) % album.getDuration());
//    } else {
//      setElapsedTime(getElapsedTime() + timeToAdd);
//    }
//
//    return history;
//  }

  @Override
  public History addTime(final AudioPlayer audioPlayer, final long timeToAdd) {
    long timeToAddCopy = timeToAdd;
    Album album = getAlbumFromPlayer(audioPlayer);
    History history = new History();

    if (audioPlayer.getRepeatPlaylistStates() == PlayerPlaylistRepeatStates.REPEAT_CURRENT_SONG) {
      Song currentSong = (Song) getPlayingAudioEntity(audioPlayer);
      int beginningOfSongTime = album.getBeginningOfSongDuration(currentSong);
      int currentTimeInSong = (int) (getElapsedTime() - beginningOfSongTime);
      assert currentSong != null;

      // Update history
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
        setElapsedTime((getElapsedTime() + timeToFinish) % album.getDuration());

        Song currentSong = (Song) getPlayingAudioEntity(audioPlayer);
        history.add(currentSong);
        timeToAddCopy -= timeToFinish;
      }
//      setElapsedTime((getElapsedTime() + timeToAdd) % album.getDuration());
    } else {
      while (timeToAddCopy > 0) {
        long timeToFinish = Math.min(getRemainingTime(audioPlayer), timeToAdd);
        if (timeToFinish == 0)
          timeToFinish = timeToAddCopy;

        setElapsedTime(getElapsedTime() + timeToFinish);

        Song currentSong = (Song) getPlayingAudioEntity(audioPlayer);
        history.add(currentSong);
        timeToAddCopy -= timeToFinish;
      }
//      setElapsedTime(getElapsedTime() + timeToAdd);
    }

    return history;
  }
  @Override
  public AudioEntity getPlayingAudioEntity(final AudioPlayer player) {
    long elapsedTime = getElapsedTime();
    Album album = getAlbumFromPlayer(player);

    if (album == null || album.getSongs().isEmpty()) {
      return null;
    }

    if (player.getRepeatSongPodcastStates() == PlayerSongPodcastRepeatStates.NO_REPEAT) {
      return findCurrentSong(elapsedTime, album);
    }

    elapsedTime %= album.getDuration();
    return findCurrentSong(elapsedTime, album);
  }

  /**
   * Calculates the remaining time of the current song in the album.
   *
   * @param audioPlayer the audio player
   * @return the remaining time in seconds
   */
  @Override
  public long getRemainingTime(final AudioPlayer audioPlayer) {
    Album album = getAlbumFromPlayer(audioPlayer);
    Song playingSong = (Song) getPlayingAudioEntity(audioPlayer);

    if (playingSong == null)
      return 0;

    assert album != null;
    long elapsedTime = getElapsedTime();
    long remainingTime;

    remainingTime = album.getEndOfSongDuration(playingSong) - elapsedTime;
    return remainingTime;
  }

  /**
   * Retrieves the album from the given AudioPlayer.
   *
   * @param player The AudioPlayer instance.
   * @return The Album object obtained from the player.
   */
  private Album getAlbumFromPlayer(final AudioPlayer player) {
    AudioEntity loadedTrack = player.getLoadedTrack();
    return (Album) loadedTrack;
  }

  /**
   * Finds the current song based on the elapsed time and the album.
   *
   * @param elapsedTime The elapsed time in seconds.
   * @param album The album containing the songs.
   * @return The current song or null if no song is found.
   */
  private AudioEntity findCurrentSong(final long elapsedTime, final Album album) {
    long currentTime = 0;
    Song song;

    for (Integer index : album.getPlayingOrder()) {
      song = album.getSongs().get(index);
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

    Album album = getAlbum(player);
    Song playingSong = getPlayingSong(player);
    long timeToRewind = calculateRewindTime(playingSong, player);

    if (timeToRewind > 0) {
      rewindToPreviousSong(timeToRewind);
    } else {
      if (isFirstSong(album, playingSong)) {
        setElapsedTime(0L);
      } else {
        rewindToBeginningOfPreviousSong(player, album);
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

  private Album getAlbum(final AudioPlayer player) {
    return (Album) player.getLoadedTrack();
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

  private boolean isFirstSong(final Album album, final Song playingSong) {
    return album.getIndexInPlayOrder(playingSong) == 0;
  }

  private void rewindToBeginningOfPreviousSong(final AudioPlayer player, final Album album) {
    Song playingSong = getPlayingSong(player);
    long timeToRewind = album.getBeginningOfSongDuration(playingSong) - 1;
    setElapsedTime(timeToRewind);

    playingSong = getPlayingSong(player);
    assert playingSong != null;
    timeToRewind = album.getBeginningOfSongDuration(playingSong);
    setElapsedTime(timeToRewind);
  }
}
