package library.users;

import app.audio.player.AudioPlayer;
import app.helpers.ConnectionStatus;
import app.helpers.UserType;
import app.history.History;
import app.io.nodes.input.InputNode;
import app.monetization.Monetization;
import app.pagination.Page;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HostPage;
import app.pagination.enums.PageType;
import app.pagination.factory.PageFactory;
import app.recommendations.Recommendations;
import app.searchbar.SearchBar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

import library.Library;
import library.entities.Announcement;
import library.entities.Event;
import library.entities.Merch;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

/** Represents a User in the music player application. */
@Getter
@Setter
public final class User {
  private String username;
  private int age;
  private String city;

  @JsonIgnore private SearchBar searchBar;
  @JsonIgnore private AudioPlayer audioPlayer;
  @JsonIgnore private List<Song> likedSongs;
  @JsonIgnore private List<Playlist> ownedPlaylists;
  @JsonIgnore private List<Playlist> followedPlaylists;

  @JsonIgnore private ConnectionStatus connectionStatus;
  @JsonIgnore private UserType userType;
  @JsonIgnore private Page currentPage;

  @JsonIgnore private List<Event> events;
  @JsonIgnore private List<Merch> merch;
  @JsonIgnore private List<Announcement> announcements;

  @JsonIgnore private long addOnPlatformTimestamp;

  @JsonIgnore private History history;
  @JsonIgnore private Monetization monetization;
  @JsonIgnore private List<Merch> boughtMerch;

  @JsonIgnore private Recommendations recommendations;

  public User() {
    this.setSearchBar(new SearchBar());
    this.setAudioPlayer(new AudioPlayer());
    this.setLikedSongs(new ArrayList<>());
    this.setOwnedPlaylists(new ArrayList<>());
    this.setFollowedPlaylists(new ArrayList<>());
    this.setConnectionStatus(ConnectionStatus.ONLINE);
    this.setUserType(UserType.NORMAL);
    this.setHistory(new History());
    this.setMonetization(new Monetization());
    this.setBoughtMerch(new ArrayList<>());
    this.setRecommendations(new Recommendations());
    changePage(PageType.HOME_PAGE);
  }

  public User(final InputNode command) {
    this.setUsername(command.getUsername());
    this.setAge(command.getAge());
    this.setCity(command.getCity());
    this.setSearchBar(new SearchBar());
    this.setAudioPlayer(new AudioPlayer());
    this.setLikedSongs(new ArrayList<>());
    this.setOwnedPlaylists(new ArrayList<>());
    this.setFollowedPlaylists(new ArrayList<>());
    this.setHistory(new History());
    this.setMonetization(new Monetization());
    this.setBoughtMerch(new ArrayList<>());
    this.setRecommendations(new Recommendations());
    this.setConnectionStatus(ConnectionStatus.ONLINE);
    this.setAddOnPlatformTimestamp(command.getTimestamp());
    switch (command.getType()) {
      case "user":
        this.setUserType(UserType.NORMAL);
        changePage(PageType.HOME_PAGE);
        break;
      case "artist":
        this.setUserType(UserType.ARTIST);
        this.setEvents(new ArrayList<>());
        this.setMerch(new ArrayList<>());
        this.changePage(PageType.ARTIST_PAGE);
        ((ArtistPage) this.getCurrentPage()).setArtistName(getUsername());
        break;
      case "host":
        this.setUserType(UserType.HOST);
        this.changePage(PageType.HOST_PAGE);
        this.setAnnouncements(new ArrayList<>());
        ((HostPage) this.getCurrentPage()).setHostName(getUsername());
        break;
      default:
    }
  }

  /**
   * Changes the current page of the user to the specified page type. The page type is used to
   * create a new page using the PageFactory. The new page is then set as the current page of the
   * user.
   *
   * @param type The type of the page to be set as the current page.
   */
  public void changePage(final PageType type) {
    Page newPage = PageFactory.createPage(type);
    this.setCurrentPage(newPage);
  }

  /**
   * Changes the current page of the user to the specified page. The provided page is directly set
   * as the current page of the user.
   *
   * @param newpage The page to be set as the current page.
   */
  public void changePage(final Page newpage) {
    this.setCurrentPage(newpage);
  }

  /**
   * Retrieves an announcement by its name from the list of announcements of the user. Iterates over
   * the list of announcements and returns the first announcement that matches the provided name. If
   * no announcement is found with the provided name, returns null.
   *
   * @param name The name of the announcement to be retrieved.
   * @return The announcement with the provided name, or null if no such announcement is found.
   */
  public Announcement getAnnouncementByName(final String name) {
    for (Announcement announcement : this.getAnnouncements()) {
      if (announcement.getName().equals(name)) {
        return announcement;
      }
    }
    return null;
  }

  /**
   * Retrieves an event by its name from the list of events of the user. Iterates over the list of
   * events and returns the first event that matches the provided name. If no event is found with
   * the provided name, returns null.
   *
   * @param name The name of the event to be retrieved.
   * @return The event with the provided name, or null if no such event is found.
   */
  public Event getEventByName(final String name) {
    for (Event event : this.getEvents()) {
      if (event.getName().equals(name)) {
        return event;
      }
    }
    return null;
  }

  public Merch getMerchByName(final String name) {
    for (Merch merch : this.getMerch()) {
      if (merch.getName().equals(name)) {
        return merch;
      }
    }
    return null;
  }

  public List<Album> getAlbums() {
    List<Album> albums = new ArrayList<>();

    for (Album album : Library.getInstance().getAlbums()) {
      if (album.getOwner().equals(this.getUsername())) {
        albums.add(album);
      }
    }
    return albums;
  }

  public boolean hasAlbum(String albumName) {
    List<Album> albums = getAlbums();

    for (Album album : albums) {
      if (album.getName().equals(albumName)) {
        return true;
      }
    }

    return false;
  }
}
