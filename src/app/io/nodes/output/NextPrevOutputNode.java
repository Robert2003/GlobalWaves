package app.io.nodes.output;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"command", "user", "timestamp", "message"})
public final class NextPrevOutputNode extends Node {
  private String user;
  private String message;

  /**
   * Constructs a new NextOutputNode object used for next and prev commands.
   *
   * @param command The input command node.
   * @param message The output message.
   */
  public NextPrevOutputNode(final InputNode command, final String message) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.setMessage(message);
  }
}
