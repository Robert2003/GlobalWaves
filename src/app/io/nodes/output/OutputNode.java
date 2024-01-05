package app.io.nodes.output;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputNode extends Node {
  private String user;
  private String message;
  private List<String> results;
  @JsonIgnore private List<AudioEntity> resultedEntities;

  /**
   * Constructs an OutputNode object with the given command and message.
   *
   * @param command The input command node.
   * @param message The output message.
   */
  public OutputNode(final InputNode command, final String message) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.setMessage(message);
  }
}
