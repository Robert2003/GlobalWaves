package app.io.nodes.output;

import app.audio.player.AudioPlayer;
import app.audio.player.states.PlayerPlayPauseStates;
import library.entities.audio.audioFiles.Episode;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class StatusOutputNode {
  private String name;
  private Long remainedTime;
  private String repeat;
  private boolean shuffle;
  private boolean paused;

  public StatusOutputNode(final AudioPlayer player) {
    Song playingSong;
    switch (player.getLoadedTrack().getType()) {
      case PODCAST:
        Episode playingEpisode = (Episode) player.getTimeManager().getPlayingAudioEntity(player);
        if (player.getPlayPauseState().equals(PlayerPlayPauseStates.STOPPED)) {
          this.setRemainedTime(0L);
          this.setName("");
        } else {
          this.setRemainedTime(player.getTimeManager().getRemainingTime(player));
          this.setName(playingEpisode.getName());
        }
        this.setRepeat(player.getRepeatSongPodcastStates().getDescription());
        break;
      case SONG:
        playingSong = (Song) player.getLoadedTrack();
        this.setRepeat(player.getRepeatSongPodcastStates().getDescription());
        if (player.getPlayPauseState().equals(PlayerPlayPauseStates.STOPPED)) {
          this.setRemainedTime(0L);
          this.setName("");
        } else {
          this.setRemainedTime(player.getTimeManager().getRemainingTime(player));
          this.setName(playingSong.getName());
        }
        break;
      case PLAYLIST:
      case ALBUM:
        playingSong = (Song) player.getTimeManager().getPlayingAudioEntity(player);
        this.setRepeat(player.getRepeatPlaylistStates().getDescription());
        if (player.getPlayPauseState().equals(PlayerPlayPauseStates.STOPPED)) {
          this.setRemainedTime(0L);
          this.setName("");
        } else {
          this.setName(playingSong.getName());
          this.setRemainedTime(player.getTimeManager().getRemainingTime(player));
        }
      default:
        break;
    }
    this.setShuffle(player.getShuffleStates().isShuffled());
    this.setPaused(player.getPlayPauseState().isPaused());
  }
}
