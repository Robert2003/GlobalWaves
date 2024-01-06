package app.io.nodes;

import app.io.nodes.input.InputNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Base node that is the base class for all input/output nodes
 */
@Getter
@Setter
public class Node {
  private String command;
  private Long timestamp;

  public Node() {
  }

  public Node(InputNode command) {
    this.setCommand(command.getCommand());
    this.setTimestamp(command.getTimestamp());
  }
}
