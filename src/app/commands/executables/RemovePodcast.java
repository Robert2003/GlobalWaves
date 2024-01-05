package app.commands.executables;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.NOT_HOST_ERROR_MESSAGE;
import static app.Constants.NO_PODCAST_ERROR_MESSAGE;
import static app.Constants.REMOVE_PODCAST_ERROR_MESSAGE;
import static app.Constants.REMOVE_PODCAST_NO_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.audio.audio.collections.Podcast;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class RemovePodcast implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User host = Library.getInstance().getUserByName(command.getUsername());

    if (host == null) {
      return new RemovePodcastOutputNode(
          command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (host.getUserType() != UserType.HOST) {
      return new RemovePodcastOutputNode(command, command.getUsername() + NOT_HOST_ERROR_MESSAGE);
    }

    if (!hostHasPodcast(command)) {
      return new RemovePodcastOutputNode(
          command, command.getUsername() + NO_PODCAST_ERROR_MESSAGE);
    }

    Podcast podcast = Library.getInstance().getPodcastByName(command.getName());
    if (isSomeoneInteracting(podcast)) {
      return new RemovePodcastOutputNode(
          command, command.getUsername() + REMOVE_PODCAST_ERROR_MESSAGE);
    }

    Library.getInstance().getPodcasts().remove(podcast);
    return new RemovePodcastOutputNode(
        command, command.getUsername() + REMOVE_PODCAST_NO_ERROR_MESSAGE);
  }

  private boolean hostHasPodcast(final InputNode command) {
    for (Podcast podcast : Library.getInstance().getPodcasts()) {
      if (podcast.getName().equals(command.getName())
          && podcast.getOwner().equals(command.getUsername())) {
        return true;
      }
    }
    return false;
  }

  private boolean isSomeoneInteracting(final Podcast podcastToBeDeleted) {
    // Parse all users and check if any of them has loaded the podcastToBeDeleted
    for (User user : Library.getInstance().getUsers()) {
      if (user.getAudioPlayer().hasLoadedTrack()
          && user.getAudioPlayer().getLoadedTrack().equals(podcastToBeDeleted)) {
        return true;
      }
    }

    return false;
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class RemovePodcastOutputNode extends Node {
    private String user;
    private String message;

    RemovePodcastOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
