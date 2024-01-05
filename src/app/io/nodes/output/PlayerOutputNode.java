package app.io.nodes.output;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * OutputNode used for almost every command that involves the audio player
 */
@Getter
@Setter
@JsonPropertyOrder({"command", "user", "timestamp", "message"})
public final class PlayerOutputNode extends Node {
  private String user;
  private String message;
  private StatusOutputNode stats = null;
  @JsonIgnore private AudioEntity loadedEntity;

  /**
   * Constructs a PlayerOutputNode object with the given command and message.
   * Used for errors
   *
   * @param command The input command node.
   * @param message The output message for the player.
   */
  public PlayerOutputNode(final InputNode command, final String message) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.setMessage(message);
  }

  /**
   * Constructs a PlayerOutputNode object with the given command and status node.
   *
   * @param command The input command node.
   * @param status The status node containing player statistics.
   */
  public PlayerOutputNode(final InputNode command, final StatusOutputNode status) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.stats = status;
  }
}
