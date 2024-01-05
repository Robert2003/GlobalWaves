package library.entities.audio.audio.collections;

import app.helpers.PlaylistVisibilityState;
import app.io.nodes.input.InputNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongAudioCollection extends AudioCollection {
  private List<Song> songs;
  private PlaylistVisibilityState visibility = PlaylistVisibilityState.PUBLIC;
  private Integer followers = 0;
  private List<Integer> playingOrder = null;
  private long creationDate = 0;

  public SongAudioCollection() {
    this.songs = new ArrayList<>();
  }

  public SongAudioCollection(final InputNode command) {
    super(command);
    this.songs = new ArrayList<>();
    this.setCreationDate(command.getTimestamp());
  }

  /**
   * Checks if the playlist contains the specified song.
   *
   * @param song The song to check.
   * @return true if the playlist contains the song, false otherwise.
   */
  public boolean containsSong(final Song song) {
    if (songs == null) {
      return false;
    }
    return songs.contains(song);
  }

  /**
   * Adds a song to the playlist.
   *
   * @param song The song to add.
   */
  public void addSong(final Song song) {
    songs.add(song);
  }

  /**
   * Removes a song from the playlist.
   *
   * @param song The song to remove.
   */
  public void removeSong(final Song song) {
    songs.remove(song);
  }

  /**
   * Gets the total duration of the playlist.
   *
   * @return The duration in seconds.
   */
  public Integer getDuration() {
    if (getSongs() == null || getSongs().isEmpty()) {
      return 0;
    }

    int duration = 0;
    for (Song song : songs) {
      duration += song.getDuration();
    }
    return duration;
  }

  /**
   * Gets the duration from the beginning of the playlist to the end of the specified song.
   *
   * @param song The song to get the duration for.
   * @return The duration in seconds.
   */
  public Integer getEndOfSongDuration(final Song song) {
    Integer duration = 0;
    Song currentSong;

    for (Integer index : playingOrder) {
      currentSong = songs.get(index);
      duration += currentSong.getDuration();
      if (currentSong.equals(song)) {
        return duration;
      }
    }
    return duration;
  }

  /**
   * Gets the duration from the beginning of the playlist to the beginning of the specified song.
   * <br>
   * <b>Note:</b> This can be used to get the end of the previous playing song if 1 is subtracted
   * from the returned duration.
   *
   * @param song The song to get the duration for.
   * @return The duration in seconds.
   */
  public Integer getBeginningOfSongDuration(final Song song) {
    Integer duration = 0;
    Song currentSong;

    for (Integer index : playingOrder) {
      currentSong = songs.get(index);
      if (currentSong.equals(song)) {
        return duration;
      }
      duration += currentSong.getDuration();
    }
    return duration;
  }

  /**
   * Gets the index of the specified song in the playlist's playing order.
   *
   * @param song The song to get the index for.
   * @return The index of the song, or -1 if not found.
   */
  public Integer getIndexInPlayOrder(final Song song) {
    Song currentSong;

    for (int i = 0; i < playingOrder.size(); i++) {
      currentSong = songs.get(playingOrder.get(i));
      if (currentSong.equals(song)) {
        return i;
      }
    }
    return -1;
  }

  /** Switches the visibility state of the playlist. */
  public void switchVisibility() {
    this.setVisibility(getVisibility().next());
  }

  /**
   * Calculates and returns the total number of likes for all songs in the AudioCollection.
   *
   * @return The total number of likes for all songs in the playlist.
   */
  public int getTotalLikes() {
    int numberOfLikes = 0;

    for (Song s : this.getSongs()) {
      numberOfLikes += s.getNumberOfLikes();
    }

    return numberOfLikes;
  }

  /**
   * Checks if this SongAudioCollection is equal to another object.
   *
   * @param o The object to compare with.
   * @return true if the objects are equal, false otherwise.
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return super.equals(o);
  }

  /**
   * Generates a hash code for this SongAudioCollection.
   *
   * @return The hash code value for this SongAudioCollection.
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), songs);
  }
}
