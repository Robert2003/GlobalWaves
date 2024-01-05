package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public class Wrapped implements Executable {

  @Override
  public Node execute(InputNode command) {
    WrappedOutputNode out = new WrappedOutputNode(command);

    User user = Library.getInstance().getUserByName(command.getUsername());
    out.getResult().setTopSongs(user.getHistory().getTop5Songs());
    out.getResult().setTopEpisodes(user.getHistory().getTop5Episodes());
    out.getResult().setTopAlbums(user.getHistory().getTop5Albums());
    out.getResult().setTopGenres(user.getHistory().getTop5Genres());

    return out;
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "result"})
  private static class WrappedOutputNode extends Node {
    private String user;
    private Result result;

    WrappedOutputNode(InputNode command) {
      super(command);
      this.setUser(command.getUsername());
      this.setResult(new Result());
    }

    @Getter
    @Setter
    private static class Result {
      private Map<String, Integer> topArtists;
      private Map<String, Integer> topGenres;
      private Map<String, Integer> topSongs;
      private Map<String, Integer> topAlbums;
      private Map<String, Integer> topEpisodes;

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
        this.setTopAlbums(new HashMap<>());
        this.setTopArtists(new HashMap<>());
        this.setTopEpisodes(new HashMap<>());
        this.setTopGenres(new HashMap<>());
        this.setTopSongs(new HashMap<>());
      }
    }
  }
}
