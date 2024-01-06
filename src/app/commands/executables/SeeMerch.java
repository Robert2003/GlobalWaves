package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.concretepages.ArtistPage;
import app.pagination.enums.PageType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.Merch;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SeeMerch implements Executable {
  @Override
  public Node execute(InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    String message;

    if (user == null) {
      message = Constants.THE_USERNAME + command.getUsername() + Constants.DOESNT_EXIST;
      return new SeeMerchOutputNode(command, message);
    }

    List<String> merchName = new ArrayList<>();
    for (Merch merch : user.getBoughtMerch()) {
      merchName.add(merch.getName());
    }

    return new SeeMerchOutputNode(command, merchName);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class SeeMerchOutputNode extends Node {
    private String user;
    private String message;
    private List<String> result;

    SeeMerchOutputNode(final InputNode command, final List<String> result) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setResult(result);
    }

    SeeMerchOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
