package library;

import app.audio.player.AudioPlayer;
import app.helpers.UserType;
import app.searchbar.SearchBar;
import app.searchbar.SearchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

/**
 * The Library class represents a library that contains songs, podcasts, users, and playlists. It
 * follows the Singleton design pattern to ensure that only one instance of the library exists.
 */
@Getter
@Setter
public final class Library {
  private static Library library;
  private ArrayList<Song> songs;
  private ArrayList<Podcast> podcasts;
  private ArrayList<User> users;
  private ArrayList<Playlist> playlists;
  @JsonIgnore private ArrayList<Album> albums;

  private Library() {
  }

  /**
   * Returns the instance of the Library class. If the library instance does not exist, it creates a
   * new one.
   *
   * @return The instance of the Library class.
   */
  public static Library getInstance() {
    if (Library.library == null) {
      library = new Library();
    }
    return library;
  }

  /**
   * Sets the instance of the Library class to a new library object. Also initializes the playlists,
   * search bar, audio player, followed playlists, and liked songs for each user.
   *
   * @param newLibrary The new library object to set as the instance.
   */
  public static void setInstance(final Library newLibrary) {
    Library.library = newLibrary;
    library.setPlaylists(new ArrayList<>());
    library.setAlbums(new ArrayList<>());

    for (User user : getInstance().getUsers()) {
      user.setSearchBar(new SearchBar());
      user.setAudioPlayer(new AudioPlayer());
      user.setFollowedPlaylists(new ArrayList<>());
      user.setOwnedPlaylists(new ArrayList<>());
      user.setLikedSongs(new ArrayList<>());
    }
  }

  /**
   * Returns the user with the specified name.
   *
   * @param name The name of the user to search for.
   * @return The user with the specified name, or null if not found.
   */
  public User getUserByName(final String name) {
    for (User user : getUsers()) {
      if (user.getUsername().equals(name)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Returns the user with the specified name.
   *
   * @param name The name of the user to search for.
   * @return The user with the specified name, or null if not found.
   */
  public int getUserIndexByName(final String name) {
    int index = 0;

    for (User user : getUsers()) {
      if (user.getUsername().equals(name)) {
        return index;
      }
      index++;
    }
    return -1;
  }

  /**
   * Retrieves an Album object from the library by its name. This method iterates over all the
   * albums in the library and returns the first one that matches the provided name. If no album
   * with the provided name is found, the method returns null.
   *
   * @param name The name of the album to search for.
   * @return The Album object with the specified name, or null if not found.
   */
  public Album getAlbumByName(final String name) {
    for (Album album : getAlbums()) {
      if (album.getName().equals(name)) {
        return album;
      }
    }
    return null;
  }

  /**
   * Retrieves a Podcast object from the library by its name. This method iterates over all the
   * podcasts in the library and returns the first one that matches the provided name. If no podcast
   * with the provided name is found, the method returns null.
   *
   * @param name The name of the podcast to search for.
   * @return The Podcast object with the specified name, or null if not found.
   */
  public Podcast getPodcastByName(final String name) {
    for (Podcast podcast : getPodcasts()) {
      if (podcast.getName().equals(name)) {
        return podcast;
      }
    }
    return null;
  }

  public List<Podcast> getPodcastsByOwner(final String username) {
    List<Podcast> podcasts = new ArrayList<>();

    for (Podcast podcast : getPodcasts()) {
      if (podcast.getOwner().equals(username)) {
        podcasts.add(podcast);
      }
    }
    return podcasts;
  }

  /** Sets the object type for each song, podcast, and playlist in the library. */
  public void setObjectTypes() {
    if (this.songs != null) {
      for (Song song : songs) {
        song.setType(SearchType.SONG);
      }
    }
    if (this.podcasts != null) {
      for (Podcast podcast : podcasts) {
        podcast.setType(SearchType.PODCAST);
      }
    }
    if (this.playlists != null) {
      for (Playlist playlist : playlists) {
        playlist.setType(SearchType.PLAYLIST);
      }
    }
  }

  /**
   * Adds a playlist to the library.
   *
   * @param playlist The playlist to add.
   */
  public void addPlaylist(final Playlist playlist) {
    this.getPlaylists().add(playlist);
  }

  /**
   * Retrieves a list of all normal users in the library. This method filters the list of all users
   * and returns a new list containing only the users whose UserType is NORMAL.
   *
   * @return A List of User objects representing all normal users in the library.
   */
  public List<User> getNormalUsers() {
    return getUsers().stream().filter(user -> user.getUserType() == UserType.NORMAL).toList();
  }

  /**
   * Retrieves a list of all artists in the library. This method filters the list of all users and
   * returns a new list containing only the users whose UserType is ARTIST.
   *
   * @return A List of User objects representing all artists in the library.
   */
  public List<User> getArtists() {
    return getUsers().stream().filter(user -> user.getUserType() == UserType.ARTIST).toList();
  }

  /**
   * Retrieves a list of all hosts in the library. This method filters the list of all users and
   * returns a new list containing only the users whose UserType is HOST.
   *
   * @return A List of User objects representing all hosts in the library.
   */
  public List<User> getHosts() {
    return getUsers().stream().filter(user -> user.getUserType() == UserType.HOST).toList();
  }
}
