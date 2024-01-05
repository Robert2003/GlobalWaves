package app.searchbar.searchfunctionallity;

import app.io.nodes.input.InputNode;
import app.io.nodes.output.SearchOutputNode;
import library.entities.audio.AudioEntity;

/** The Searchable interface represents an entity that can be searched. */
public interface Searchable {
  /**
   * Searches for a specific song/playlist/podcast in the library based on command filters.
   *
   * @param command the input command containing the filters
   * @return the search node representing the result of the search
   */
  SearchOutputNode search(InputNode command);

  /**
   * Checks if the given audio entity matches the specified filter query.
   *
   * @param entity the audio entity to check
   * @param query the filter query to match against
   * @return true if the entity matches the query, false otherwise
   */
  boolean isMatch(AudioEntity entity, Filter query);
}
