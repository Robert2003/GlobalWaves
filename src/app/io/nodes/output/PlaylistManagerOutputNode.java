package app.io.nodes.output;

import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"command", "user", "timestamp", "message"})
public final class PlaylistManagerOutputNode extends OutputNode {

  public PlaylistManagerOutputNode(final InputNode command, final String message) {
    super(command, message);
  }
}
