package library.users;

import static app.Constants.PREMIUM_CREDIT;
import static app.monetization.subscription.UserPremiumState.FREE;

import app.audio.player.AudioPlayer;
import app.helpers.ConnectionStatus;
import app.helpers.UserType;
import app.history.History;
import app.io.nodes.input.InputNode;
import app.monetization.Monetization;
import app.monetization.payment.PremiumPaymentStrategy;
import app.monetization.subscription.PremiumHistory;
import app.monetization.subscription.UserPremiumState;
import app.notifications.Notification;
import app.notifications.observer.Observer;
import app.notifications.observer.Subject;
import app.pagination.Page;
import app.pagination.PageHistory;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HostPage;
import app.pagination.enums.PageType;
import app.pagination.factory.PageFactory;
import app.recommendations.Recommendations;
import app.searchbar.SearchBar;
import app.searchbar.SearchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import library.Library;
import library.entities.Announcement;
import library.entities.Event;
import library.entities.Merch;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

/** Represents a User in the music player application. */
@Getter
@Setter
public final class User implements Subject, Observer {
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
  @JsonIgnore private PageHistory pageHistory;

  @JsonIgnore private List<Observer> observers;
  @JsonIgnore private List<User> subscribedUsers;
  @JsonIgnore private List<Notification> notifications;

  @JsonIgnore private PremiumHistory premiumHistory;

  public User() {
    this.setSearchBar(new SearchBar());
    this.setAudioPlayer(new AudioPlayer());
    getAudioPlayer().setOwner(this);
    this.setLikedSongs(new ArrayList<>());
    this.setOwnedPlaylists(new ArrayList<>());
    this.setFollowedPlaylists(new ArrayList<>());
    this.setConnectionStatus(ConnectionStatus.ONLINE);
    this.setUserType(UserType.NORMAL);
    this.setHistory(new History());
    this.setMonetization(new Monetization());
    this.setBoughtMerch(new ArrayList<>());
    this.setRecommendations(new Recommendations());
    this.setPageHistory(new PageHistory());
    this.setObservers(new ArrayList<>());
    this.setSubscribedUsers(new ArrayList<>());
    this.setNotifications(new ArrayList<>());
    this.setPremiumHistory(new PremiumHistory());
    changePage(PageType.HOME_PAGE, true);
  }

  public User(final InputNode command) {
    this.setUsername(command.getUsername());
    this.setAge(command.getAge());
    this.setCity(command.getCity());
    this.setSearchBar(new SearchBar());
    this.setAudioPlayer(new AudioPlayer());
    getAudioPlayer().setOwner(this);
    this.setLikedSongs(new ArrayList<>());
    this.setOwnedPlaylists(new ArrayList<>());
    this.setFollowedPlaylists(new ArrayList<>());
    this.setHistory(new History());
    this.setMonetization(new Monetization());
    this.setBoughtMerch(new ArrayList<>());
    this.setRecommendations(new Recommendations());
    this.setPageHistory(new PageHistory());
    this.setObservers(new ArrayList<>());
    this.setSubscribedUsers(new ArrayList<>());
    this.setNotifications(new ArrayList<>());
    this.setPremiumHistory(new PremiumHistory(command.getTimestamp()));
    this.setConnectionStatus(ConnectionStatus.ONLINE);
    this.setAddOnPlatformTimestamp(command.getTimestamp());
    switch (command.getType()) {
      case "user":
        this.setUserType(UserType.NORMAL);
        changePage(PageType.HOME_PAGE, true);
        break;
      case "artist":
        this.setUserType(UserType.ARTIST);
        this.setEvents(new ArrayList<>());
        this.setMerch(new ArrayList<>());
        this.changePage(PageType.ARTIST_PAGE, true);
        ((ArtistPage) this.getCurrentPage()).setArtistName(getUsername());
        break;
      case "host":
        this.setUserType(UserType.HOST);
        this.changePage(PageType.HOST_PAGE, true);
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
  public void changePage(final PageType type, final boolean addToHistory) {
    Page newPage = PageFactory.createPage(type);

    if (type == PageType.HOST_PAGE) {
      String hostName = null;
      if (getAudioPlayer().hasLoadedTrack()
          && getAudioPlayer().getLoadedTrack().getType() == SearchType.PODCAST) {
        hostName = ((Podcast) getAudioPlayer().getLoadedTrack()).getOwner();
      }
      ((HostPage) newPage).setHostName(hostName);
    } else if (type == PageType.ARTIST_PAGE) {
      String artistName = null;
      if (getAudioPlayer().hasLoadedTrack()
          && getAudioPlayer().getLoadedTrack().getType() == SearchType.ALBUM) {
        artistName = ((Podcast) getAudioPlayer().getLoadedTrack()).getOwner();
      }
      ((ArtistPage) newPage).setArtistName(artistName);
    }

    if (addToHistory) {
      pageHistory.addPage(newPage);
    }
    this.setCurrentPage(newPage);
  }

  /**
   * Changes the current page of the user to the specified page. The provided page is directly set
   * as the current page of the user.
   *
   * @param newPage The page to be set as the current page.
   */
  public void changePage(final Page newPage, final boolean addToHistory) {
    this.setCurrentPage(newPage);
    if (addToHistory) {
      pageHistory.addPage(newPage);
    }
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

  /**
   * Retrieves a Merch object by its name from the list of merch of the user. Iterates over the list
   * of merch and returns the first merch that matches the provided name. If no merch is found with
   * the provided name, returns null.
   *
   * @param name The name of the merch to be retrieved.
   * @return The merch with the provided name, or null if no such merch is found.
   */
  public Merch getMerchByName(final String name) {
    for (Merch currentMerch : this.getMerch()) {
      if (currentMerch.getName().equals(name)) {
        return currentMerch;
      }
    }
    return null;
  }

  /**
   * Retrieves a list of Album objects owned by a specific user. This method iterates over all the
   * albums in the library and adds to the list those that are owned by the user.
   *
   * @return A list of Album objects owned by the user.
   */
  public List<Album> getAlbums() {
    List<Album> albums = new ArrayList<>();

    for (Album album : Library.getInstance().getAlbums()) {
      if (album.getOwner().equals(this.getUsername())) {
        albums.add(album);
      }
    }
    return albums;
  }

  /**
   * Checks if the user owns an album with the specified name. This method iterates over all the
   * albums owned by the user and returns true if an album with the provided name is found.
   *
   * @param albumName The name of the album to search for.
   * @return True if the user owns an album with the specified name, false otherwise.
   */
  public boolean hasAlbum(final String albumName) {
    List<Album> albums = getAlbums();

    for (Album album : albums) {
      if (album.getName().equals(albumName)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void addObserver(final Observer observer) {
    getObservers().add(observer);
  }

  @Override
  public void removeObserver(final Observer observer) {
    getObservers().remove(observer);
  }

  @Override
  public void notifyObservers(final Notification notification) {
    for (Observer observer : observers) {
      observer.update(notification);
    }
  }

  @Override
  public void update(final Notification notification) {
    getNotifications().add(notification);
  }

  public UserPremiumState getPremiumState() {
    return getPremiumHistory().getCurrentState();
  }

  /**
   * Toggles the premium state of the user. If the user is currently a free user, their state is
   * changed to premium. If the user is currently a premium user, their state is changed to free,
   * and a payment is made using the PremiumPaymentStrategy.
   *
   * @param currentTimestamp The current timestamp, used to record when the state change occurred.
   */
  public void togglePremiumState(final long currentTimestamp) {
    if (getPremiumState() == FREE) {
      getPremiumHistory().addState(UserPremiumState.PREMIUM, currentTimestamp);
    } else {
      getPremiumHistory().addState(UserPremiumState.FREE, currentTimestamp);
      new PremiumPaymentStrategy().pay(this, PREMIUM_CREDIT);
    }
  }
}
