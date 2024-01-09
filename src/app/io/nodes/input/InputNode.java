package app.io.nodes.input;

import app.io.nodes.Node;
import java.util.List;
import java.util.Map;
import library.entities.audio.audioFiles.Episode;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an input command. This node contains information about the username,
 * type, filters, item number, playlist name, playlist ID, and seed for a command.
 */
@Getter
@Setter
public final class InputNode extends Node {
  private String username;
  private String type;
  private Map<String, Object> filters;
  private Integer itemNumber;
  private String playlistName;
  private Integer playlistId;
  private Integer seed;
  private Integer age;
  private String city;
  private String name;
  private Integer releaseYear;
  private String description;
  private List<Song> songs;
  private String date;
  private Integer price;
  private List<Episode> episodes;
  private String nextPage;
  private String recommendationType;
}
