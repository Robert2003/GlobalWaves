package app.audio.player.timemanagement.concretestrategies;

import static app.audio.player.states.PlayerSongPodcastRepeatStates.NO_REPEAT;
import static app.audio.player.states.PlayerSongPodcastRepeatStates.REPEAT_INFINITE;
import static app.audio.player.states.PlayerSongPodcastRepeatStates.REPEAT_ONCE;

import app.Constants;
import app.audio.player.AudioPlayer;
import app.audio.player.states.PlayerPlayPauseStates;
import app.audio.player.timemanagement.TimeManagerStrategy;
import app.history.History;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.NextPrevOutputNode;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;

/**
 * The SongTimeManagerStrategy class is a concrete implementation of the TimeManagerStrategy
 * abstract class. It provides time management functionality for a song in an audio player.
 */
public final class SongTimeManagerStrategy extends TimeManagerStrategy {
  public SongTimeManagerStrategy(final Long startingTime) {
    super(startingTime);
  }

  @Override
  public History addTime(final AudioPlayer audioPlayer, final long timeToAdd) {
    Song song = (Song) getPlayingAudioEntity(audioPlayer);
    History history = new History();
    long timeToAddCopy = timeToAdd;

    if (audioPlayer.getRepeatSongPodcastStates() == REPEAT_INFINITE) {
      this.setElapsedTime((this.getElapsedTime() + timeToAddCopy) % song.getDuration());
    } else if (audioPlayer.getRepeatSongPodcastStates() == REPEAT_ONCE) {
      this.setElapsedTime(this.getElapsedTime() + timeToAddCopy);

      if (firstCyclePassed(song)) {
        audioPlayer.setRepeatSongPodcastStates(NO_REPEAT);
        this.setElapsedTime(this.getElapsedTime() % song.getDuration());
      }
    } else {
      if (audioPlayer.isAdBeingPlayed()) {
        if (timeToAddCopy >= audioPlayer.getAd().getLeftTimestamp()) {
          timeToAddCopy -= audioPlayer.getAd().getLeftTimestamp();
          audioPlayer.getAd().resetAd();
        } else {
          audioPlayer
                  .getAd()
                  .setLeftTimestamp(audioPlayer.getAd().getLeftTimestamp() - timeToAddCopy);
          return history;
        }
      }

      this.setElapsedTime(this.getElapsedTime() + timeToAddCopy);
      long currentTimestamp = getLastTimeUpdated() + timeToAddCopy;

      if (getRemainingTime(audioPlayer) <= 0 && audioPlayer.getAdShouldBePlayed()) {
        System.out.println(getRemainingTime(audioPlayer) + " " + song.getDuration() + " " + song.getName());
        audioPlayer.startAd(currentTimestamp);
      }
    }

    return history;
  }

  private boolean firstCyclePassed(final Song song) {
    return this.getElapsedTime() > song.getDuration();
  }

  @Override
  public long getRemainingTime(final AudioPlayer audioPlayer) {
    Song song = (Song) audioPlayer.getLoadedTrack();
    return song.getDuration() - this.getElapsedTime();
  }

  @Override
  public AudioEntity getPlayingAudioEntity(final AudioPlayer player) {
    return player.getLoadedTrack();
  }

  @Override
  public NextPrevOutputNode prev(final AudioPlayer player, final InputNode command) {
    if (!player.hasLoadedTrack()) {
      return new NextPrevOutputNode(command, Constants.PREV_ERROR_MESSAGE);
    }

    setElapsedTime(0L);
    player.setPlayPauseState(PlayerPlayPauseStates.PLAYING);

    return new NextPrevOutputNode(
        command,
        Constants.PREV_NO_ERROR_MESSAGE
            + getPlayingAudioEntity(player).getName()
            + Constants.PHRASE_TERMINATOR);
  }
}
