package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import library.Library;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class ShowPreferredSongs implements Executable {
  /**
   * Shows the list of preferred songs/liked songs of the user.
   *
   * @param command The input command for showing preferred songs.
   * @return The output node containing the list of preferred songs.
   */
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    return new ShowPreferredSongsOutputNode(command, user.getLikedSongs());
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "result"})
  private final class ShowPreferredSongsOutputNode extends Node {
    private String user;
    private List<String> result;

    ShowPreferredSongsOutputNode(final InputNode command, final List<Song> likedSongs) {
      this.setCommand(command.getCommand());
      this.setUser(command.getUsername());
      this.setTimestamp(command.getTimestamp());
      this.result = new ArrayList<>();

      if (likedSongs != null && !likedSongs.isEmpty()) {
        for (Song song : likedSongs) {
          if (song != null) {
            result.add(song.getName());
          }
        }
      }
    }
  }
}
