package app.pagination.visitors;

import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HomePage;
import app.pagination.concretepages.HostPage;
import app.pagination.concretepages.LikedContentPage;

public interface PageVisitor {
  /**
   * Visit method for HomePage. This method is used when a visitor visits the HomePage.
   *
   * @param homePage The HomePage object that the visitor is visiting.
   * @param command The InputNode object that represents the command given by the visitor.
   * @return Node object that represents the result of the visit.
   */
  Node visit(HomePage homePage, InputNode command);

  /**
   * Visit method for LikedContentPage. This method is used when a visitor visits the
   * LikedContentPage.
   *
   * @param likedContentPage The LikedContentPage object that the visitor is visiting.
   * @param command The InputNode object that represents the command given by the visitor.
   * @return Node object that represents the result of the visit.
   */
  Node visit(LikedContentPage likedContentPage, InputNode command);

  /**
   * Visit method for ArtistPage. This method is used when a visitor visits the ArtistPage.
   *
   * @param artistPage The ArtistPage object that the visitor is visiting.
   * @param command The InputNode object that represents the command given by the visitor.
   * @return Node object that represents the result of the visit.
   */
  Node visit(ArtistPage artistPage, InputNode command);

  /**
   * Visit method for HostPage. This method is used when a visitor visits the HostPage.
   *
   * @param hostPage The HostPage object that the visitor is visiting.
   * @param command The InputNode object that represents the command given by the visitor.
   * @return Node object that represents the result of the visit.
   */
  Node visit(HostPage hostPage, InputNode command);
}
