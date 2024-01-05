package app.pagination.factory;

import app.pagination.Page;
import app.pagination.concretepages.ArtistPage;
import app.pagination.concretepages.HomePage;
import app.pagination.concretepages.HostPage;
import app.pagination.concretepages.LikedContentPage;
import app.pagination.enums.PageType;

/**
 * This class is a Factory class for creating Page objects of different types. <br>
 * This method implements the Factory * design pattern for creating the desired page.
 */
public final class PageFactory {
  private PageFactory() {
  }

  /**
   * Creates a new Page object based on the given PageType.
   *
   * @param pageType The type of the page to be created.
   * @return a new Page object of the type specified by the pageType parameter. If the pageType
   *      does not match any of the predefined types, the method returns null.
   */
  public static Page createPage(final PageType pageType) {
    return switch (pageType) {
      case HOME_PAGE -> new HomePage();
      case LIKED_CONTENT_PAGE -> new LikedContentPage();
      case ARTIST_PAGE -> new ArtistPage();
      case HOST_PAGE -> new HostPage();
      default -> null;
    };
  }
}
