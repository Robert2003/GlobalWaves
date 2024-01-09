package app.pagination;

import java.util.ArrayList;
import java.util.List;

public class PageHistory {
	List<Page> pages;
	int currentPageIndex;

	public PageHistory() {
		pages = new ArrayList<>();
		currentPageIndex = 0;
	}

	public Page getCurrentPage() {
		return pages.get(currentPageIndex);
	}

	public Page getPreviousPage() {
		if (currentPageIndex == 0) {
			return null;
		}
		currentPageIndex--;
		return pages.get(currentPageIndex);
	}

	public Page getNextPage() {
		if (currentPageIndex == pages.size() - 1) {
			return null;
		}
		currentPageIndex++;
		return pages.get(currentPageIndex);
	}

	public void addPage(Page page) {
		pages.add(page);
		currentPageIndex = pages.size() - 1;
	}
}
