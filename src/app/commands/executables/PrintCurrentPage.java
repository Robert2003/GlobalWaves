package app.commands.executables;

import static app.Constants.IS_OFFLINE_ERROR_MESSAGE;

import app.commands.Executable;
import app.helpers.ConnectionStatus;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.PrintCurrentPageOutputNode;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HomePage;
import app.pagination.concretepages.HostPage;
import app.pagination.concretepages.LikedContentPage;
import app.pagination.visitors.concrete.PrintPageVisitor;
import library.Library;
import library.users.User;

public final class PrintCurrentPage implements Executable {

  @Override
  public Node execute(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user.getConnectionStatus() == ConnectionStatus.OFFLINE) {
      return new PrintCurrentPageOutputNode(command, user.getUsername() + IS_OFFLINE_ERROR_MESSAGE);
    }

    PrintPageVisitor printVisitor = new PrintPageVisitor();
    return switch (user.getCurrentPage().getType()) {
      case HOME_PAGE -> printVisitor.visit((HomePage) user.getCurrentPage(), command);
      case ARTIST_PAGE -> printVisitor.visit((ArtistPage) user.getCurrentPage(), command);
      case HOST_PAGE -> printVisitor.visit((HostPage) user.getCurrentPage(), command);
      case LIKED_CONTENT_PAGE -> printVisitor.visit(
          (LikedContentPage) user.getCurrentPage(), command);
      default -> null;
    };
  }
}
