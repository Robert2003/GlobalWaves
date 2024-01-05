package app.pagination.enums;

import lombok.Getter;

@Getter
public enum PageType {
  HOME_PAGE("Home"),
  LIKED_CONTENT_PAGE("LikedContent"),
  ARTIST_PAGE("Artist"),
  HOST_PAGE("Host"),
  DEFAULT("default");

  private String description;

  PageType(final String description) {
    this.description = description;
  }
}
