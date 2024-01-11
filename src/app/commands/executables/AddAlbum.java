package app.commands.executables;

import static app.Constants.ADD_ALBUM_NO_ERROR_MESSAGE;
import static app.Constants.DOESNT_EXIST;
import static app.Constants.HAS_ALBUM_ERROR_MESSAGE;
import static app.Constants.NOT_ARTIST_ERROR_MESSAGE;
import static app.Constants.NOT_UNIQUE_SONG_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.notifications.Notification;
import app.notifications.observer.NotificationType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashSet;
import java.util.Set;
import library.Library;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class AddAlbum implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new AddAlbumOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (user.getUserType() != UserType.ARTIST) {
      return new AddAlbumOutputNode(command, command.getUsername() + NOT_ARTIST_ERROR_MESSAGE);
    }

    if (hasAlbum(command)) {
      return new AddAlbumOutputNode(command, command.getUsername() + HAS_ALBUM_ERROR_MESSAGE);
    }

    if (!hasUniqueSongs(command)) {
      return new AddAlbumOutputNode(command, command.getUsername() + NOT_UNIQUE_SONG_ERROR_MESSAGE);
    }

    Album album = new Album(command);
    Library.getInstance().getAlbums().add(album);
    for (Song song : album.getSongs()) {
      song.setArtist(album.getOwner());
      if (!Library.getInstance().getSongs().contains(song)) {
        Library.getInstance().getSongs().add(song);
      }
    }

    Notification notification = new Notification(NotificationType.NEW_ALBUM, "New Album from " + user.getUsername() + ".");
    user.notifyObservers(notification);

    return new AddAlbumOutputNode(command, command.getUsername() + ADD_ALBUM_NO_ERROR_MESSAGE);
  }

  private boolean hasAlbum(final InputNode command) {
    for (Album album : Library.getInstance().getAlbums()) {
      if (album.getName().equals(command.getName())
          && album.getOwner().equals(command.getUsername())) {
        return true;
      }
    }
    return false;
  }

  private boolean hasUniqueSongs(final InputNode command) {
    Set<String> songsSet = new HashSet<>();

    for (Song song : command.getSongs()) {
      songsSet.add(song.getName());
    }

    return command.getSongs().size() == songsSet.size();
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private static final class AddAlbumOutputNode extends Node {
    private String user;
    private String message;

    AddAlbumOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
