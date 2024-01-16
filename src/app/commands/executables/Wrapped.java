package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.wrapped.ArtistWrappedStrategy;
import app.wrapped.HostWrappedStrategy;
import app.wrapped.UserWrappedStrategy;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;
import library.Library;
import lombok.Getter;
import lombok.Setter;

public final class Wrapped implements Executable {

  @Override
  public Node execute(final InputNode command) {
    return switch (Library.getInstance().getUserByName(command.getUsername()).getUserType()) {
      case NORMAL -> new UserWrappedStrategy().execute(command);
      case ARTIST -> new ArtistWrappedStrategy().execute(command);
      case HOST -> new HostWrappedStrategy().execute(command);
    };
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "result"})
  public static class WrappedOutputNode extends Node {
    private String user;
    private Result result;
    private String message;

    public WrappedOutputNode(final InputNode command) {
      super(command);
      this.setUser(command.getUsername());
      this.setResult(new Result());
    }

    @Getter
    @Setter
    @JsonPropertyOrder({"topAlbums", "topSongs"})
    public static class Result {
      private Map<String, Integer> topArtists;
      private Map<String, Integer> topGenres;
      private Map<String, Integer> topSongs;
      private Map<String, Integer> topAlbums;
      private Map<String, Integer> topEpisodes;
      private List<String> topFans;
      private Integer listeners;

      public Result() {
      }
    }
  }
}
