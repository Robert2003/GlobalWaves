package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.wrapped.ArtistWrappedStrategy;
import app.wrapped.HostWrappedStrategy;
import app.wrapped.UserWrappedStrategy;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public class Wrapped implements Executable {

  @Override
  public Node execute(InputNode command) {
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

    public WrappedOutputNode(InputNode command) {
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

      public Result(
          Map<String, Integer> topArtists,
          Map<String, Integer> topGenres,
          Map<String, Integer> topSongs,
          Map<String, Integer> topAlbums,
          Map<String, Integer> topEpisodes) {
        this.topArtists = topArtists;
        this.topGenres = topGenres;
        this.topSongs = topSongs;
        this.topAlbums = topAlbums;
        this.topEpisodes = topEpisodes;
      }

      public Result() {
      }
    }
  }
}
