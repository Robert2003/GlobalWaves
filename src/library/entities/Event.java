package library.entities;

import app.io.nodes.input.InputNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
  private String name;
  private String owner;
  private String description;
  private String date;

  public Event(final InputNode command) {
    this.setName(command.getName());
    this.setOwner(command.getUsername());
    this.setDescription(command.getDescription());
    this.setDate(command.getDate());
  }
}
