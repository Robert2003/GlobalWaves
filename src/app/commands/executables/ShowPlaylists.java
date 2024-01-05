package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import library.Library;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class ShowPlaylists implements Executable {
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

    return new ShowPlaylistOutputNode(command);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "result"})
  public final class ShowPlaylistOutputNode extends Node {
    private String user;
    private List<PlaylistNode> result;

    @Getter
    @Setter
    private static final class PlaylistNode {
      private String name;
      private List<String> songs;
      private String visibility;
      private Integer followers;

      PlaylistNode(final Playlist playlist) {
        this.name = playlist.getName();
        this.followers = playlist.getFollowers();
        this.visibility = playlist.getVisibility().getDescription();
        songs = new ArrayList<>();
        for (Song song : playlist.getSongs()) {
          songs.add(song.getName());
        }
      }
    }

    public ShowPlaylistOutputNode(final InputNode command) {
      this.setCommand(command.getCommand());
      this.setUser(command.getUsername());
      this.setTimestamp(command.getTimestamp());
      this.result = new ArrayList<>();

      User currentUser = Library.getInstance().getUserByName(command.getUsername());
      for (Playlist playlist : currentUser.getOwnedPlaylists()) {
        result.add(new PlaylistNode(playlist));
      }
    }
  }
}
