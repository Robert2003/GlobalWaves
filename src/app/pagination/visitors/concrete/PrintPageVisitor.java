package app.pagination.visitors.concrete;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.PrintCurrentPageOutputNode;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HomePage;
import app.pagination.concretepages.HostPage;
import app.pagination.concretepages.LikedContentPage;
import app.pagination.visitors.PageVisitor;
import java.util.Iterator;
import library.entities.Announcement;
import library.entities.Event;
import library.entities.Merch;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Episode;
import library.entities.audio.audioFiles.Song;

/**
 * This class implements the PageVisitor interface and provides the functionality to print different
 * types of pages. It uses the PopulatePageVisitor to populate the pages before printing them.
 */
public final class PrintPageVisitor implements PageVisitor {
  @Override
  public Node visit(final HomePage homePage, final InputNode command) {
    new PopulatePageVisitor().visit(homePage, command);

    StringBuilder messageBuilder = new StringBuilder();

    messageBuilder.append("Liked songs:").append("\n\t[");

    Iterator<Song> songIterator = homePage.getTop5Songs().iterator();
    while (songIterator.hasNext()) {
      Song song = songIterator.next();
      messageBuilder.append(song.getName());
      if (songIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]\n\n").append("Followed playlists:").append("\n\t[");

    Iterator<Playlist> playlistIterator = homePage.getTop5Playlists().iterator();
    while (playlistIterator.hasNext()) {
      Playlist playlist = playlistIterator.next();
      messageBuilder.append(playlist.getName());
      if (playlistIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]");
    return new PrintCurrentPageOutputNode(command, messageBuilder.toString());
  }

  @Override
  public Node visit(final LikedContentPage likedContentPage, final InputNode command) {
    new PopulatePageVisitor().visit(likedContentPage, command);

    StringBuilder messageBuilder = new StringBuilder();

    messageBuilder.append("Liked songs:").append("\n\t[");

    Iterator<Song> songIterator = likedContentPage.getLikedSongs().iterator();
    while (songIterator.hasNext()) {
      Song song = songIterator.next();
      messageBuilder.append(song.getName()).append(" - ").append(song.getArtist());
      if (songIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]\n\n").append("Followed playlists:").append("\n\t[");

    Iterator<Playlist> playlistIterator = likedContentPage.getFollowedPlaylists().iterator();
    while (playlistIterator.hasNext()) {
      Playlist playlist = playlistIterator.next();
      messageBuilder.append(playlist.getName()).append(" - ").append(playlist.getOwner());
      if (playlistIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]");
    return new PrintCurrentPageOutputNode(command, messageBuilder.toString());
  }

  @Override
  public Node visit(final ArtistPage artistPage, final InputNode command) {
    new PopulatePageVisitor().visit(artistPage, command);

    StringBuilder messageBuilder = new StringBuilder();

    messageBuilder.append("Albums:").append("\n\t[");

    Iterator<Album> albumIterator = artistPage.getAlbums().iterator();
    while (albumIterator.hasNext()) {
      Album album = albumIterator.next();
      messageBuilder.append(album.getName());
      if (albumIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]\n\n").append("Merch:").append("\n\t[");

    Iterator<Merch> merchIterator = artistPage.getMerch().iterator();
    while (merchIterator.hasNext()) {
      Merch merch = merchIterator.next();
      messageBuilder.append(merch.getName());
      messageBuilder.append(" - ");
      messageBuilder.append(merch.getPrice());
      messageBuilder.append(":\n\t");
      messageBuilder.append(merch.getDescription());
      if (merchIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]\n\n").append("Events:").append("\n\t[");

    Iterator<Event> eventIterator = artistPage.getEvents().iterator();
    while (eventIterator.hasNext()) {
      Event event = eventIterator.next();
      messageBuilder.append(event.getName());
      messageBuilder.append(" - ");
      messageBuilder.append(event.getDate());
      messageBuilder.append(":\n\t");
      messageBuilder.append(event.getDescription());
      if (eventIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]");

    return new PrintCurrentPageOutputNode(command, messageBuilder.toString());
  }

  @Override
  public Node visit(final HostPage hostPage, final InputNode command) {
    new PopulatePageVisitor().visit(hostPage, command);

    StringBuilder messageBuilder = new StringBuilder();

    messageBuilder.append("Podcasts:").append("\n\t[");

    Iterator<Podcast> podcastIterator = hostPage.getPodcasts().iterator();
    while (podcastIterator.hasNext()) {
      Podcast podcast = podcastIterator.next();
      messageBuilder.append(podcast.getName());

      messageBuilder.append(":\n\t[");
      Iterator<Episode> episodeIterator = podcast.getEpisodes().iterator();
      while (episodeIterator.hasNext()) {
        Episode episode = episodeIterator.next();

        messageBuilder.append(episode.getName());
        messageBuilder.append(" - ");
        messageBuilder.append(episode.getDescription());

        if (episodeIterator.hasNext()) {
          messageBuilder.append(", ");
        }
      }
      messageBuilder.append("]\n");

      if (podcastIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]\n\n").append("Announcements:").append("\n\t[");
    Iterator<Announcement> announcementIterator = hostPage.getAnnouncements().iterator();
    while (announcementIterator.hasNext()) {
      Announcement announcement = announcementIterator.next();
      messageBuilder.append(announcement.getName());
      messageBuilder.append(":\n\t");
      messageBuilder.append(announcement.getDescription());
      messageBuilder.append("\n");
      if (announcementIterator.hasNext()) {
        messageBuilder.append(", ");
      }
    }

    messageBuilder.append("]");

    return new PrintCurrentPageOutputNode(command, messageBuilder.toString());
  }
}
