package app.searchbar;

import app.Constants;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.SearchOutputNode;
import app.io.nodes.output.SelectOutputNode;
import app.searchbar.searchfunctionallity.SearchStrategyFactory;
import app.searchbar.searchfunctionallity.Searchable;
import app.searchbar.selectfunctionality.SelectCommand;
import library.Library;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

/**
 * The SearchBar class represents a search bar functionality in the music library application. It
 * allows users to execute searches and make selections based on the search results.
 */
@Getter
@Setter
public final class SearchBar {
  private SearchOutputNode lastSearch = null;
  private SelectOutputNode selectedTrack = null;

  /**
   * Executes a search operation based on the given command and returns a SearchNode object. If the
   * user is currently playing audio. Resets the last search, selects the search strategy based on
   * the search type, and executes the search operation using the selected strategy. Finally,
   * empties the audio player and returns the search result.
   *
   * @param command The command containing the search information.
   * @return The SearchResult containing the search results.
   */
  public SearchOutputNode executeSearch(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    assert user != null;

    this.setLastSearch(null);
    String searchType = command.getType();
    SearchType strategyType;
    Searchable strategy;

    try {
      strategyType = SearchType.valueOf(searchType.toUpperCase());
    } catch (IllegalArgumentException e) {
      strategyType = SearchType.NOT_INITIALIZED;
    }

    try {
      strategy = SearchStrategyFactory.createSearchStrategy(strategyType);
    } catch (IllegalArgumentException e) {
      return null;
    }

    SearchOutputNode out = strategy.search(command);

    this.setLastSearch(out);
    this.setSelectedTrack(null);

    user.getAudioPlayer().stopPlayback();
    return out;
  }

  /**
   * Executes the select operation based on the given command and returns a SelectNode object. If
   * there is no previous search, it returns a SelectNode object with an error message. If the
   * selected ID is too high, it returns a SelectNode object with an error message. It empties the
   * audio player of the user. It creates a select strategy based on the type of the last search. It
   * selects the desired item and sets it as the selected track. Finally, it sets the last search to
   * null.
   *
   * @param command The input node containing the command information.
   * @return A SelectNode object representing the result of the select operation.
   */
  public SelectOutputNode executeSelect(final InputNode command) {
    if (!this.hasLastSearch()) {
      return new SelectOutputNode(command, Constants.SELECT_ERROR_MESSAGE);
    }

    if (this.getLastSearch().getType().isAudio()) {
      if (lastSearch.getResultedEntities().size() < command.getItemNumber()) {
        return new SelectOutputNode(command, Constants.SELECTED_ID_TOO_HIGH_ERROR_MESSAGE);
      }

      User user = Library.getInstance().getUserByName(command.getUsername());
      assert user != null;
      user.getAudioPlayer().stopPlayback();

      SelectCommand selectCommand = new SelectCommand();
      SelectOutputNode out = selectCommand.select(command, lastSearch);

      this.setSelectedTrack(out);
      this.setLastSearch(null);
      return out;
    } else {
      if (lastSearch.getResultedUsers().size() < command.getItemNumber()) {
        return new SelectOutputNode(command, Constants.SELECTED_ID_TOO_HIGH_ERROR_MESSAGE);
      }

      User user = Library.getInstance().getUserByName(command.getUsername());
      assert user != null;
      user.getAudioPlayer().stopPlayback();

      User artist =
          Library.getInstance()
              .getUserByName(
                  lastSearch.getResultedUsers().get(command.getItemNumber() - 1).getUsername());
      user.changePage(artist.getCurrentPage());

      return new SelectOutputNode(
          command, "Successfully selected " + artist.getUsername() + "'s page.");
    }
  }

  /**
   * Checks if the search bar has performed a search before.
   *
   * @return true if the search bar has a last search result, false otherwise.
   */
  public boolean hasLastSearch() {
    if (this.getLastSearch() == null) {
      return false;
    }

    if (this.getLastSearch().getType() == SearchType.ARTIST
        || this.getLastSearch().getType() == SearchType.HOST) {
      return this.getLastSearch().getResultedUsers() != null;
    }

    return this.getLastSearch().getResultedEntities() != null;
  }

  /**
   * Checks if a track has been selected.
   *
   * @return true if a track has been selected, false otherwise.
   */
  public boolean hasSelectedTrack() {
    return this.getSelectedTrack() != null && this.getSelectedTrack().getSelectedEntity() != null;
  }
}
