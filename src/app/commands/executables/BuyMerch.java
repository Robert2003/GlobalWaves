package app.commands.executables;

import app.Constants;
import app.commands.Executable;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.concretepages.ArtistPage;
import app.pagination.enums.PageType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.Merch;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

public final class BuyMerch implements Executable {
  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    String message;

    if (user == null) {
      message = Constants.THE_USERNAME + command.getUsername() + Constants.DOESNT_EXIST;
      return new BuyMerchOutputNode(command, message);
    }

    if (user.getCurrentPage().getType() != PageType.ARTIST_PAGE) {
      message = "Cannot buy merch from this page.";
      return new BuyMerchOutputNode(command, message);
    }

    String artistName = ((ArtistPage) user.getCurrentPage()).getArtistName();
    User artist = Library.getInstance().getUserByName(artistName);
    Merch merch = artist.getMerchByName(command.getName());

    if (merch == null) {
      message = "The merch " + command.getName() + " doesn't exist.";
      return new BuyMerchOutputNode(command, message);
    }

    user.getBoughtMerch().add(merch);
    artist.getMonetization().receivePayment(merch);

    message = command.getUsername() + " has added new merch successfully.";
    return new BuyMerchOutputNode(command, message);
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class BuyMerchOutputNode extends Node {
    private String user;
    private String message;

    BuyMerchOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
