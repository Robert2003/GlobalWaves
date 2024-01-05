package app.commands.executables;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import library.Library;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Episode;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class ShowPodcasts implements Executable {
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

    return new ShowPodcastsOutputNode(command);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "result"})
  private static final class ShowPodcastsOutputNode extends Node {
    private String user;
    private List<PodcastNode> result;

    @Getter
    @Setter
    private static final class PodcastNode {
      private String name;
      private List<String> episodes;

      PodcastNode(final Podcast podcast) {
        this.name = podcast.getName();
        episodes = new ArrayList<>();
        for (Episode episode : podcast.getEpisodes()) {
          episodes.add(episode.getName());
        }
      }
    }

    ShowPodcastsOutputNode(final InputNode command) {
      this.setCommand(command.getCommand());
      this.setUser(command.getUsername());
      this.setTimestamp(command.getTimestamp());
      this.result = new ArrayList<>();

      for (Podcast podcast : Library.getInstance().getPodcasts()) {
        if (podcast.getOwner().equals(command.getUsername())) {
          result.add(new PodcastNode(podcast));
        }
      }
    }
  }
}
