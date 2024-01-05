package app.io.nodes.output;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"command", "user", "timestamp", "message", "results"})
public final class Top5OutputNode extends Node {
  private String user;
  private List<String> result;
  @JsonIgnore private List<AudioEntity> resultedEntities;
  @JsonIgnore private String type;

  public Top5OutputNode(final InputNode command, final List<AudioEntity> resultedEntities) {
    this.setCommand(command.getCommand());
    this.setUser(command.getUsername());
    this.setTimestamp(command.getTimestamp());
    this.setType(command.getType());
    this.resultedEntities = new ArrayList<>(resultedEntities);
    this.result = new ArrayList<>();
    for (AudioEntity entity : getResultedEntities()) {
      result.add(entity.getName());
    }
  }
}
