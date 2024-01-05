package app.commands.executables;

import static app.Constants.ADD_PODCAST_NO_ERROR_MESSAGE;
import static app.Constants.DOESNT_EXIST;
import static app.Constants.HAS_PODCAST_ERROR_MESSAGE;
import static app.Constants.NOT_HOST_ERROR_MESSAGE;
import static app.Constants.NOT_UNIQUE_EPISODE_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashSet;
import java.util.Set;
import library.Library;
import library.entities.audio.audio.collections.Podcast;
import library.entities.audio.audioFiles.Episode;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class AddPodcast implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new AddPodcastOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (user.getUserType() != UserType.HOST) {
      return new AddPodcastOutputNode(command, command.getUsername() + NOT_HOST_ERROR_MESSAGE);
    }

    if (hasPodcast(command)) {
      return new AddPodcastOutputNode(command, command.getUsername() + HAS_PODCAST_ERROR_MESSAGE);
    }

    if (!hasUniqueEpisodes(command)) {
      return new AddPodcastOutputNode(
          command, command.getUsername() + NOT_UNIQUE_EPISODE_ERROR_MESSAGE);
    }

    Podcast podcast = new Podcast(command);
    Library.getInstance().getPodcasts().add(podcast);
    return new AddPodcastOutputNode(command, command.getUsername() + ADD_PODCAST_NO_ERROR_MESSAGE);
  }

  private boolean hasPodcast(final InputNode command) {
    for (Podcast podcast : Library.getInstance().getPodcasts()) {
      if (podcast.getName().equals(command.getName())
          && podcast.getOwner().equals(command.getUsername())) {
        return true;
      }
    }
    return false;
  }

  private boolean hasUniqueEpisodes(final InputNode command) {
    Set<String> episodeSet = new HashSet<>();

    for (Episode episode : command.getEpisodes()) {
      episodeSet.add(episode.getName());
    }

    return command.getEpisodes().size() == episodeSet.size();
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class AddPodcastOutputNode extends Node {
    private String user;
    private String message;

    AddPodcastOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
