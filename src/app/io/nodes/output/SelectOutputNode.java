package app.io.nodes.output;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"command", "user", "timestamp", "message"})
public final class SelectOutputNode extends Node {
  private String user;
  private String message;
  @JsonIgnore private AudioEntity selectedEntity;

  /**
   * Constructs a SelectNode object with the given command and message.
   * Used for errors and outputs that have only a message
   *
   * @param command The input command node.
   * @param message The message associated with the select node.
   */
  public SelectOutputNode(final InputNode command, final String message) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.setMessage(message);
  }

  /**
   * Constructs a SelectNode object with the given command, message, and last search node.
   *
   * @param command The input command node.
   * @param message The message associated with the select node.
   * @param lastSearch The last search node.
   */
  public SelectOutputNode(
      final InputNode command, final String message, final SearchOutputNode lastSearch) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.setMessage(message);
    this.setSelectedEntity(lastSearch.getResultedEntities().get(command.getItemNumber() - 1));
  }
}
