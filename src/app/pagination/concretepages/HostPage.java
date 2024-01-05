package app.pagination.concretepages;

import app.io.nodes.input.InputNode;
import app.pagination.Page;
import app.pagination.enums.PageType;
import app.pagination.visitors.PageVisitable;
import app.pagination.visitors.PageVisitor;
import java.util.List;
import library.entities.Announcement;
import library.entities.audio.audio.collections.Podcast;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class HostPage extends Page implements PageVisitable {
  private String hostName;
  private List<Podcast> podcasts;
  private List<Announcement> announcements;

  public HostPage() {
    this.setType(PageType.HOST_PAGE);
  }

  @Override
  public void accept(final PageVisitor visitor, final InputNode command) {
    visitor.visit(this, command);
  }
}
