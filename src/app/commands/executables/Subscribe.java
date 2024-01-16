package app.commands.executables;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.THE_USERNAME;
import static app.pagination.enums.PageType.HOST_PAGE;

import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.Page;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HostPage;
import app.pagination.enums.PageType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class Subscribe implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new SubscribeOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    Page currentPage = user.getCurrentPage();
    if (currentPage.getType() != HOST_PAGE && currentPage.getType() != PageType.ARTIST_PAGE) {
      return new SubscribeOutputNode(
          command, "To subscribe you need to be on the page of an artist or host.");
    }

    User pageOwner;

    if (currentPage.getType() == HOST_PAGE) {
      pageOwner = Library.getInstance().getUserByName(((HostPage) currentPage).getHostName());
    } else {
      pageOwner = Library.getInstance().getUserByName(((ArtistPage) currentPage).getArtistName());
    }

    if (pageOwner.getSubscribedUsers().contains(user)) {
      pageOwner.getSubscribedUsers().remove(user);
      pageOwner.removeObserver(user);
      return new SubscribeOutputNode(
          command,
          user.getUsername() + " unsubscribed from " + pageOwner.getUsername() + " successfully.");
    }
    pageOwner.getSubscribedUsers().add(user);
    pageOwner.addObserver(user);
    return new SubscribeOutputNode(
        command,
        user.getUsername() + " subscribed to " + pageOwner.getUsername() + " successfully.");
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class SubscribeOutputNode extends Node {
    private String user;
    private String message;

    SubscribeOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
