package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import library.Library;
import library.entities.audio.audio.collections.Album;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class ShowAlbums implements Executable {
  /**
   * Shows the list of playlists available in the application based on the given input command.
   *
   * @param command The input command for showing playlists.
   * @return The output node containing the list of playlists.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return new ShowAlbumsOutputNode(command);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "result"})
  public static final class ShowAlbumsOutputNode extends Node {
    private String user;
    private List<AlbumNode> result;

    public ShowAlbumsOutputNode(final InputNode command) {
      this.setCommand(command.getCommand());
      this.setUser(command.getUsername());
      this.setTimestamp(command.getTimestamp());
      this.result = new ArrayList<>();

      for (Album album : Library.getInstance().getAlbums()) {
        if (album.getOwner().equals(command.getUsername())) {
          result.add(new AlbumNode(album));
        }
      }
    }

    @Getter
    @Setter
    private static final class AlbumNode {
      private String name;
      private List<String> songs;

      AlbumNode(final Album album) {
        this.name = album.getName();
        songs = new ArrayList<>();
        for (Song song : album.getSongs()) {
          songs.add(song.getName());
        }
      }
    }
  }
}
