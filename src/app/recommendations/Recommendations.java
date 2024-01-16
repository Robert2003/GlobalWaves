package app.recommendations;

import java.util.ArrayList;
import java.util.List;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Recommendations {
  private List<Song> songRecommendations;
  private List<Playlist> playlistRecommendations;

  private AudioEntity lastRecommendation;

  public Recommendations() {
    setSongRecommendations(new ArrayList<>());
    setPlaylistRecommendations(new ArrayList<>());
  }

  /**
   * Checks if there are any song or playlist recommendations.
   *
   * @return True if there are recommendations, false otherwise.
   */
  public boolean hasRecommendations() {
    return !songRecommendations.isEmpty() || !playlistRecommendations.isEmpty();
  }

  /**
   * Adds an audio entity to the appropriate list of recommendations.
   *
   * @param audioEntity The audio entity to be added.
   */
  public void add(final AudioEntity audioEntity) {
    switch (audioEntity.getType()) {
      case SONG -> getSongRecommendations().add((Song) audioEntity);
      case PLAYLIST -> getPlaylistRecommendations().add((Playlist) audioEntity);
      default -> {
        return;
      }
    }

    lastRecommendation = audioEntity;
  }
}
