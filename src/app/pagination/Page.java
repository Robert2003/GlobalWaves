package app.pagination;

import app.pagination.enums.PageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {
  private PageType type;
}
