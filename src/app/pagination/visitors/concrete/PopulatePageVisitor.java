package app.pagination.visitors.concrete;

import app.Constants;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HomePage;
import app.pagination.concretepages.HostPage;
import app.pagination.concretepages.LikedContentPage;
import app.pagination.visitors.PageVisitor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import library.Library;
import library.entities.Announcement;
import library.entities.Event;
import library.entities.Merch;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Song;
import library.users.User;

/**
 * This class implements the PageVisitor interface and provides the functionality to populate
 * different types of pages.
 */
public final class PopulatePageVisitor implements PageVisitor {
  @Override
  public Node visit(final HomePage homePage, final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    List<Song> top5Songs =
        user.getLikedSongs().stream()
            .sorted(Comparator.comparingInt(Song::getNumberOfLikes).reversed())
            .limit(Constants.PRINT_LIMIT)
            .toList();

    homePage.setTop5Songs(top5Songs);

    List<Playlist> top5Playlists =
        user.getFollowedPlaylists().stream()
            .sorted(Comparator.comparingInt(Playlist::getTotalLikes).reversed())
            .limit(Constants.PRINT_LIMIT)
            .toList();

    homePage.setTop5Playlists(top5Playlists);

    homePage.setSongRecommendations(user.getRecommendations().getSongRecommendations());
    homePage.setPlaylistRecommendations(user.getRecommendations().getPlaylistRecommendations());

    return null;
  }

  @Override
  public Node visit(final LikedContentPage likedContentPage, final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    likedContentPage.setLikedSongs(user.getLikedSongs());
    likedContentPage.setFollowedPlaylists(user.getFollowedPlaylists());

    return null;
  }

  @Override
  public Node visit(final ArtistPage artistPage, final InputNode command) {
    User artist = Library.getInstance().getUserByName(artistPage.getArtistName());
    artistPage.setAlbums(new ArrayList<>());
    artistPage.setEvents(new ArrayList<>());
    artistPage.setMerch(new ArrayList<>());

    if (artist == null) {
      return null;
    }

    for (Album album : Library.getInstance().getAlbums()) {
      if (album.getOwner().equals(artistPage.getArtistName())) {
        artistPage.getAlbums().add(album);
      }
    }

    for (Event event : artist.getEvents()) {
      if (event.getOwner().equals(artistPage.getArtistName())) {
        artistPage.getEvents().add(event);
      }
    }

    for (Merch merch : artist.getMerch()) {
      if (merch.getOwner().equals(artistPage.getArtistName())) {
        artistPage.getMerch().add(merch);
      }
    }

    return null;
  }

  @Override
  public Node visit(final HostPage hostPage, final InputNode command) {
    User host = Library.getInstance().getUserByName(hostPage.getHostName());
    hostPage.setPodcasts(new ArrayList<>());
    hostPage.setAnnouncements(new ArrayList<>());

    if (host == null) {
      return null;
    }

    for (Podcast podcast : Library.getInstance().getPodcasts()) {
      if (podcast.getOwner().equals(hostPage.getHostName())) {
        hostPage.getPodcasts().add(podcast);
      }
    }

    for (Announcement announcement : host.getAnnouncements()) {
      hostPage.getAnnouncements().add(announcement);
    }

    return null;
  }
}
