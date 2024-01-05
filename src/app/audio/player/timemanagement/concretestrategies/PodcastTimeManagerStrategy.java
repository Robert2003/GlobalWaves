package app.audio.player.timemanagement.concretestrategies;

import app.Constants;
import app.audio.player.AudioPlayer;
import app.audio.player.states.PlayerPlayPauseStates;
import app.audio.player.states.PlayerSongPodcastRepeatStates;
import app.audio.player.timemanagement.TimeManagerStrategy;
import app.history.History;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.NextPrevOutputNode;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Episode;
import library.entities.audio.audioFiles.Song;

/**
 * The PodcastTimeManagerStrategy class is a concrete implementation of the TimeManagerStrategy
 * abstract class. It provides time management functionality for a podcast in an audio player.
 */
public final class PodcastTimeManagerStrategy extends TimeManagerStrategy {

  public PodcastTimeManagerStrategy(final Long startingTime) {
    super(startingTime);
  }

  @Override
  public History addTime(final AudioPlayer audioPlayer, final long timeToAdd) {
    History history = new History();
    Podcast podcast = getPodcastFromPlayer(audioPlayer);
//    setElapsedTime(getElapsedTime() + timeToAdd);
    long timeToAddCopy = timeToAdd;

    while (timeToAddCopy > 0) {
      long timeToFinish = Math.min(getRemainingTime(audioPlayer), timeToAdd);
      setElapsedTime((getElapsedTime() + timeToFinish) % podcast.getDuration());

      Episode currentEpisode = (Episode) getPlayingAudioEntity(audioPlayer);
      history.add(currentEpisode);
      timeToAddCopy -= timeToFinish;
    }
    return history;
  }

  @Override
  public AudioEntity getPlayingAudioEntity(final AudioPlayer player) {
    long elapsedTime = getElapsedTime();
    Podcast podcast = getPodcastFromPlayer(player);

    if (podcast == null
        || player.getRepeatSongPodcastStates() == PlayerSongPodcastRepeatStates.NO_REPEAT) {
      assert podcast != null;
      return findCurrentEpisode(elapsedTime, podcast);
    }

    elapsedTime %= podcast.getDuration();
    return findCurrentEpisode(elapsedTime, podcast);
  }

  /**
   * Calculates the remaining time of the current playing episode in the podcast.
   *
   * @param audioPlayer The audio player.
   * @return The remaining time in seconds.
   */
  public long getRemainingTime(final AudioPlayer audioPlayer) {
    Podcast podcast = getPodcastFromPlayer(audioPlayer);
    Episode playingEpisode = (Episode) getPlayingAudioEntity(audioPlayer);

    assert podcast != null;
    long elapsedTime = getElapsedTime();
    long remainingTime;

    if (audioPlayer.getRepeatSongPodcastStates() == PlayerSongPodcastRepeatStates.REPEAT_ONCE) {
      if (elapsedTime > 2L * podcast.getDuration()) {
        return 0;
      }

      if (elapsedTime > podcast.getDuration()) {
        audioPlayer.setRepeatSongPodcastStates(PlayerSongPodcastRepeatStates.NO_REPEAT);
        setElapsedTime(elapsedTime % podcast.getDuration());
      }

      remainingTime =
          podcast.getEndOfEpisodeDuration(playingEpisode) - elapsedTime % podcast.getDuration();
      return remainingTime;
    }

    if (audioPlayer.getRepeatSongPodcastStates() == PlayerSongPodcastRepeatStates.REPEAT_INFINITE) {
      remainingTime =
          podcast.getEndOfEpisodeDuration(playingEpisode) - elapsedTime % podcast.getDuration();
      return remainingTime;
    }

    remainingTime = podcast.getEndOfEpisodeDuration(playingEpisode) - elapsedTime;
    return remainingTime;
  }

  private Podcast getPodcastFromPlayer(final AudioPlayer player) {
    AudioEntity loadedTrack = player.getLoadedTrack();
    return (Podcast) loadedTrack;
  }

  private AudioEntity findCurrentEpisode(final long elapsedTime, final Podcast podcast) {
    long currentTime = 0;
    for (Episode episode : podcast.getEpisodes()) {
      if (episode.getDuration() + currentTime <= elapsedTime) {
        currentTime += episode.getDuration();
      } else {
        return episode;
      }
    }
    return null;
  }

  @Override
  public NextPrevOutputNode prev(final AudioPlayer player, final InputNode command) {
    if (!player.hasLoadedTrack()) {
      return new NextPrevOutputNode(command, Constants.PREV_ERROR_MESSAGE);
    }

    long timeToRewind = calculateTimeToRewind(player);
    adjustElapsedTimeAndSetPlayPauseState(timeToRewind, player);

    return new NextPrevOutputNode(
        command,
        Constants.PREV_NO_ERROR_MESSAGE
            + getPlayingAudioEntity(player).getName()
            + Constants.PHRASE_TERMINATOR);
  }

  // Helper methods
  private Podcast getPodcast(final AudioPlayer player) {
    return (Podcast) player.getLoadedTrack();
  }

  private Episode getPlayingEpisode(final AudioPlayer player) {
    return (Episode) getPlayingAudioEntity(player);
  }

  private long calculateTimeToRewind(final AudioPlayer player) {
    Podcast podcast = getPodcast(player);
    Episode playingEpisode = getPlayingEpisode(player);

    long beginningDuration = podcast.getBeginningOfEpisodeDuration(playingEpisode);
    if (beginningDuration != 0) {
      if (playingEpisode.getDuration() > getRemainingTime(player)) {
        long timeToRewind = podcast.getBeginningOfEpisodeDuration(playingEpisode) - 1;
        setElapsedTime(timeToRewind);

        playingEpisode = getPlayingEpisode(player);
        timeToRewind = podcast.getBeginningOfEpisodeDuration(playingEpisode);
        setElapsedTime(timeToRewind);
      } else {
        return beginningDuration;
      }
    } else {
      return getRemainingTime(player) - playingEpisode.getDuration();
    }
    return 0;
  }

  private void adjustElapsedTimeAndSetPlayPauseState(
      final long timeToRewind, final AudioPlayer player) {
    setElapsedTime(getElapsedTime() - timeToRewind);
    player.setPlayPauseState(PlayerPlayPauseStates.PLAYING);
  }
}
