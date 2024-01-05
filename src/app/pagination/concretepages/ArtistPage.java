package app.pagination.concretepages;

import app.io.nodes.input.InputNode;
import app.pagination.Page;
import app.pagination.enums.PageType;
import app.pagination.visitors.PageVisitable;
import app.pagination.visitors.PageVisitor;
import java.util.List;
import library.entities.Event;
import library.entities.Merch;
import library.entities.audio.audio.collections.Album;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ArtistPage extends Page implements PageVisitable {
  private String artistName;
  private List<Album> albums;
  private List<Event> events;
  private List<Merch> merch;

  public ArtistPage() {
    this.setType(PageType.ARTIST_PAGE);
  }

  @Override
  public void accept(final PageVisitor visitor, final InputNode command) {
    visitor.visit(this, command);
  }
}
