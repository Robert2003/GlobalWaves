package app.io.nodes.output;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"user", "command", "timestamp", "message"})
public final class PrintCurrentPageOutputNode extends Node {
  private String user;
  private String message;

  public PrintCurrentPageOutputNode(final InputNode command, final String message) {
    this.setCommand(command.getCommand());
    this.setTimestamp(command.getTimestamp());
    this.setUser(command.getUsername());
    this.setMessage(message);
  }
}
