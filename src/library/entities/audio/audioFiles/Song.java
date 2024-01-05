package library.entities.audio.audioFiles;

import app.searchbar.SearchType;
import java.util.ArrayList;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Song extends AudioFile {
  private String album;
  private ArrayList<String> tags;
  private String lyrics;
  private String genre;
  private Integer releaseYear;
  private String artist;

  private Integer numberOfLikes = 0;

  public Song() {
    this.setType(SearchType.SONG);
  }

  public Song(final Song song) {
    super(song);
    this.setAlbum(song.getAlbum());
    this.setTags(new ArrayList<>(song.getTags()));
    this.setLyrics(song.getLyrics());
    this.setGenre(song.getGenre());
    this.setReleaseYear(song.getReleaseYear());
    this.setArtist(song.getArtist());
    this.setNumberOfLikes(song.getNumberOfLikes());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Song song = (Song) o;
    return
//        Objects.equals(album, song.album)
        Objects.equals(getName(), song.getName())
//        && Objects.equals(lyrics, song.lyrics)
//        && Objects.equals(genre, song.genre)
//        && Objects.equals(releaseYear, song.releaseYear)
        && Objects.equals(artist, song.artist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(), album, tags, lyrics, genre, releaseYear, artist, numberOfLikes);
  }

  @Override
  public String toString() {
    return "Song{ " + getName() + " }";
  }
}
