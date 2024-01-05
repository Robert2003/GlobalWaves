package library.entities;

import app.io.nodes.input.InputNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Merch {
  private String name;
  private String owner;
  private String description;
  private int price;

  public Merch(final InputNode command) {
    this.setName(command.getName());
    this.setOwner(command.getUsername());
    this.setDescription(command.getDescription());
    this.setPrice(command.getPrice());
  }
}
