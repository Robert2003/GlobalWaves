package app.pagination;

import java.util.ArrayList;
import java.util.List;

public final class PageHistory {
  private List<Page> pages;
  private int currentPageIndex;

  public PageHistory() {
    pages = new ArrayList<>();
    currentPageIndex = 0;
  }

  /**
   * Returns the previous page in the history. If there is no previous page, it returns null.
   *
   * @return The previous page or null.
   */
  public Page getPreviousPage() {
    if (currentPageIndex == 0) {
      return null;
    }
    currentPageIndex--;
    return pages.get(currentPageIndex);
  }

  /**
   * Returns the next page in the history. If there is no next page, it returns null.
   *
   * @return The next page or null.
   */
  public Page getNextPage() {
    if (currentPageIndex == pages.size() - 1) {
      return null;
    }
    currentPageIndex++;
    return pages.get(currentPageIndex);
  }

  /**
   * Adds a new page to the history. The new page becomes the current page.
   *
   * @param page The page to be added.
   */
  public void addPage(final Page page) {
    pages.add(page);
    currentPageIndex = pages.size() - 1;
  }
}
