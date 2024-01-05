package app.pagination.concretepages;

import app.io.nodes.input.InputNode;
import app.pagination.Page;
import app.pagination.enums.PageType;
import app.pagination.visitors.PageVisitable;
import app.pagination.visitors.PageVisitor;
import java.util.List;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class HomePage extends Page implements PageVisitable {
  private List<Song> top5Songs;
  private List<Playlist> top5Playlists;

  public HomePage() {
    this.setType(PageType.HOME_PAGE);
  }

  @Override
  public void accept(final PageVisitor visitor, final InputNode command) {
    visitor.visit(this, command);
  }
}
