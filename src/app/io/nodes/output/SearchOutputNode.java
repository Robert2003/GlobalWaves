package app.io.nodes.output;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.searchbar.SearchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import library.entities.audio.AudioEntity;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"command", "user", "timestamp", "message", "results"})
public final class SearchOutputNode extends Node {
  private String user;
  private String message;
  private List<String> results;
  @JsonIgnore private List<AudioEntity> resultedEntities;
  @JsonIgnore private List<User> resultedUsers;
  @JsonIgnore private SearchType type;

  /**
   * Constructs a SearchNode object with the given command and message. Used for errors
   *
   * @param command the input command
   * @param message the search message
   */
  public SearchOutputNode(final InputNode command, final String message) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.setMessage(message);
    this.setResults(new ArrayList<>());
  }

  /**
   * Constructs a SearchNode object with the given command, resulted entities, and message.
   *
   * @param command the input command
   * @param resultedEntities the list of resulted audio entities
   * @param message the search message
   */
  public SearchOutputNode(
      final InputNode command, final List<?> resultedEntities, final String message) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());

    this.setType(SearchType.valueOf(command.getType().toUpperCase()));
    this.setMessage(message);

    this.setResults(new ArrayList<>());
    if (getType() == SearchType.HOST || getType() == SearchType.ARTIST) {
      this.setResultedUsers((List<User>) new ArrayList<>(resultedEntities));
      for (User u : getResultedUsers()) {
        results.add(u.getUsername());
      }
    } else {
      this.setResultedEntities((List<AudioEntity>) new ArrayList<>(resultedEntities));
      for (AudioEntity entity : getResultedEntities()) {
        results.add(entity.getName());
      }
    }
  }
}
