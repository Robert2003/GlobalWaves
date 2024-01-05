package library.entities;

import app.io.nodes.input.InputNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Announcement {
  private String name;
  private String owner;
  private String description;

  public Announcement(final InputNode command) {
    this.setName(command.getName());
    this.setOwner(command.getUsername());
    this.setDescription(command.getDescription());
  }
}
