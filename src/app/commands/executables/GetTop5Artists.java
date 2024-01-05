package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.audio.collections.Album;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class GetTop5Artists implements Executable {
  /**
   * Returns the top 5 playlists based on followers and creation date.
   *
   * @param command The input command.
   * @return The top 5 playlists.
   */
  @Override
  public Node execute(final InputNode command) {
    List<User> top5Artists =
        Library.getInstance().getUsers().stream()
            .sorted(Comparator.comparingInt(user -> getTotalArtistLikes(user.getUsername())))
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toList());

    return new Top5ArtistsOutputNode(command, top5Artists);
  }

  private int getTotalArtistLikes(final String username) {
    int sum = 0;
    for (Album album
            : Library.getInstance().getAlbums().stream()
            .filter(album -> album.getOwner().equals(username))
            .toList()) {
      sum -= album.getTotalLikes();
    }

    return sum;
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message", "results"})
  private final class Top5ArtistsOutputNode extends Node {
    private String user;
    private List<String> result;
    @JsonIgnore private List<User> resultedEntities;
    @JsonIgnore private String type;

    Top5ArtistsOutputNode(final InputNode command, final List<User> resultedEntities) {
      this.setCommand(command.getCommand());
      this.setUser(command.getUsername());
      this.setTimestamp(command.getTimestamp());
      this.setType(command.getType());
      this.resultedEntities = new ArrayList<>(resultedEntities);
      this.result = new ArrayList<>();
      for (User u : getResultedEntities()) {
        result.add(u.getUsername());
      }
    }
  }
}
