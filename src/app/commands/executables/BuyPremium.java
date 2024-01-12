package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.THE_USERNAME;
import static app.monetization.subscription.UserPremiumState.FREE;

public class BuyPremium implements Executable {
  @Override
  public Node execute(InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new BuyPremiumOutputNode(command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }

    if (user.getPremiumState() != FREE) {
      return new BuyPremiumOutputNode(
          command, command.getUsername() + " is already a premium user.");
    }

    user.togglePremiumState(command.getTimestamp());
    return new BuyPremiumOutputNode(
        command, command.getUsername() + " bought the subscription successfully.");
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class BuyPremiumOutputNode extends Node {
    private String user;
    private String message;

    BuyPremiumOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}