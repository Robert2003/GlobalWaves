package app.commands;

import app.commands.executables.AdBreak;
import app.commands.executables.AddAlbum;
import app.commands.executables.AddAnnouncement;
import app.commands.executables.AddEvent;
import app.commands.executables.AddMerch;
import app.commands.executables.AddPodcast;
import app.commands.executables.AddRemoveInPlaylist;
import app.commands.executables.AddUser;
import app.commands.executables.Backward;
import app.commands.executables.BuyMerch;
import app.commands.executables.BuyPremium;
import app.commands.executables.CancelPremium;
import app.commands.executables.ChangePage;
import app.commands.executables.CreatePlaylist;
import app.commands.executables.DeleteUser;
import app.commands.executables.Follow;
import app.commands.executables.Forward;
import app.commands.executables.GetAllUsers;
import app.commands.executables.GetNotifications;
import app.commands.executables.GetOnlineUsers;
import app.commands.executables.GetTop5Albums;
import app.commands.executables.GetTop5Artists;
import app.commands.executables.GetTop5Playlists;
import app.commands.executables.GetTop5Songs;
import app.commands.executables.Like;
import app.commands.executables.Load;
import app.commands.executables.LoadRecommendations;
import app.commands.executables.Next;
import app.commands.executables.NextPage;
import app.commands.executables.PlayPause;
import app.commands.executables.Prev;
import app.commands.executables.PreviousPage;
import app.commands.executables.PrintCurrentPage;
import app.commands.executables.RemoveAlbum;
import app.commands.executables.RemoveAnnouncement;
import app.commands.executables.RemoveEvent;
import app.commands.executables.RemovePodcast;
import app.commands.executables.Repeat;
import app.commands.executables.Search;
import app.commands.executables.SeeMerch;
import app.commands.executables.Select;
import app.commands.executables.ShowAlbums;
import app.commands.executables.ShowPlaylists;
import app.commands.executables.ShowPodcasts;
import app.commands.executables.ShowPreferredSongs;
import app.commands.executables.Shuffle;
import app.commands.executables.Status;
import app.commands.executables.Subscribe;
import app.commands.executables.SwitchConnectionStatus;
import app.commands.executables.SwitchVisibility;
import app.commands.executables.UpdateRecommendations;
import app.commands.executables.Wrapped;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.pagination.Page;
import library.Library;
import library.users.User;

/**
 * The CommandManager class is responsible for processing commands and delegating them to the
 * appropriate user methods.
 */
public final class CommandManager {

  private static CommandManager manager;

  private CommandManager() {}

  /**
   * Returns the singleton instance of the CommandManager.
   *
   * @return the CommandManager instance
   */
  public static CommandManager getManager() {
    if (manager == null) {
      manager = new CommandManager();
    }
    return manager;
  }

  /**
   * Processes the given command and delegates it to the appropriate user method.
   *
   * @param command the input command to be processed
   * @return the result of the command execution
   */
  public Node processCommand(final InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());
    assert user != null;
    String commandDescription = command.getCommand();

    CommandTypes commandType = CommandTypes.DEFAULT;
    for (CommandTypes type : CommandTypes.values()) {
      if (type.getDescription().equalsIgnoreCase(commandDescription)) {
        commandType = type;
        break;
      }
    }

    for (User u : Library.getInstance().getUsers()) {
      if (u.getUserType() == UserType.NORMAL) {
        u.getAudioPlayer().updateTime(command, u);
      }
    }

    return switch (commandType) {
      case SEARCH -> new Search().execute(command);
      case SELECT -> new Select().execute(command);
      case LOAD -> new Load().execute(command);
      case PLAY_PAUSE -> new PlayPause().execute(command);
      case REPEAT -> new Repeat().execute(command);
      case SHUFFLE -> new Shuffle().execute(command);
      case LIKE -> new Like().execute(command);
      case STATUS -> new Status().execute(command);
      case FORWARD -> new Forward().execute(command);
      case BACKWARD -> new Backward().execute(command);
      case CREATE_PLAYLIST -> new CreatePlaylist().execute(command);
      case ADD_REMOVE_IN_PLAYLIST -> new AddRemoveInPlaylist().execute(command);
      case SHOW_PLAYLISTS -> new ShowPlaylists().execute(command);
      case SHOW_PREFERRED_SONGS -> new ShowPreferredSongs().execute(command);
      case FOLLOW -> new Follow().execute(command);
      case GET_TOP_5_PLAYLISTS -> new GetTop5Playlists().execute(command);
      case SWITCH_VISIBILITY -> new SwitchVisibility().execute(command);
      case GET_TOP_5_SONGS -> new GetTop5Songs().execute(command);
      case NEXT -> new Next().execute(command);
      case PREV -> new Prev().execute(command);
      case SWITCH_CONNECTION_STATUS -> new SwitchConnectionStatus().execute(command);
      case GET_ONLINE_USERS -> new GetOnlineUsers().execute(command);
      case ADD_USER -> new AddUser().execute(command);
      case ADD_ALBUM -> new AddAlbum().execute(command);
      case SHOW_ALBUMS -> new ShowAlbums().execute(command);
      case PRINT_CURRENT_PAGE -> new PrintCurrentPage().execute(command);
      case CHANGE_PAGE -> new ChangePage().execute(command);
      case GET_ALL_USERS -> new GetAllUsers().execute(command);
      case ADD_EVENT -> new AddEvent().execute(command);
      case ADD_MERCH -> new AddMerch().execute(command);
      case DELETE_USER -> new DeleteUser().execute(command);
      case ADD_PODCAST -> new AddPodcast().execute(command);
      case ADD_ANNOUNCEMENT -> new AddAnnouncement().execute(command);
      case REMOVE_ANNOUNCEMENT -> new RemoveAnnouncement().execute(command);
      case SHOW_PODCASTS -> new ShowPodcasts().execute(command);
      case REMOVE_ALBUM -> new RemoveAlbum().execute(command);
      case REMOVE_PODCAST -> new RemovePodcast().execute(command);
      case REMOVE_EVENT -> new RemoveEvent().execute(command);
      case GET_TOP_5_ALBUMS -> new GetTop5Albums().execute(command);
      case GET_TOP_5_ARTISTS -> new GetTop5Artists().execute(command);
      case WRAPPED -> new Wrapped().execute(command);
      case BUY_MERCH -> new BuyMerch().execute(command);
      case SEE_MERCH -> new SeeMerch().execute(command);
      case UPDATE_RECOMMENDATIONS -> new UpdateRecommendations().execute(command);
      case NEXT_PAGE -> new NextPage().execute(command);
      case PREVIOUS_PAGE -> new PreviousPage().execute(command);
      case LOAD_RECOMMENDATIONS -> new LoadRecommendations().execute(command);
      case SUBSCRIBE -> new Subscribe().execute(command);
      case GET_NOTIFICATIONS -> new GetNotifications().execute(command);
      case BUY_PREMIUM -> new BuyPremium().execute(command);
      case CANCEL_PREMIUM -> new CancelPremium().execute(command);
      case AD_BREAK -> new AdBreak().execute(command);
      case DEFAULT -> null;
    };
  }
}
