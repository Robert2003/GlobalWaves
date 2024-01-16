package app.wrapped;

import static app.searchbar.SearchType.SONG;

import app.Constants;
import app.commands.Executable;
import app.commands.executables.Wrapped;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import library.users.User;

public final class ArtistWrappedStrategy implements Executable {
  /**
   * Retrieves the top 5 songs by the artist based on user history.
   *
   * @param artist The artist for whom the top songs are to be retrieved.
   * @return A map of song names and their respective play counts.
   */
  public static Map<String, Integer> getTop5Songs(final User artist) {
    Map<String, Integer> songs = new LinkedHashMap<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG
            && !entry.getKey().equals(Library.getInstance().getSongs().get(0))) {
          Song song = (Song) entry.getKey();
          String artistName = song.getArtist();

          if (!artist.getUsername().equals(artistName)) {
            continue;
          }

          if (songs.containsKey(song.getName())) {
            songs.put(song.getName(), songs.get(song.getName()) + entry.getValue());
          } else {
            songs.put(song.getName(), entry.getValue());
          }
        }
      }
    }

    return songs.entrySet().stream()
        .sorted(
            Map.Entry.<String, Integer>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry::getKey))
        .limit(Constants.PRINT_LIMIT)
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  @Override
  public Node execute(final InputNode command) {
    Wrapped.WrappedOutputNode out = new Wrapped.WrappedOutputNode(command);

    User artist = Library.getInstance().getUserByName(command.getUsername());

    out.getResult().setTopSongs(getTop5Songs(artist));
    out.getResult().setTopAlbums(getTop5Albums(artist));
    out.getResult().setTopFans(getTop5Fans(artist));
    out.getResult().setListeners(getListeners(artist));

    if (out.getResult().getTopSongs().size() == 0
        && out.getResult().getTopAlbums().size() == 0
        && out.getResult().getTopFans().size() == 0
        && out.getResult().getListeners() == 0) {
      out.setResult(null);
      out.setMessage("No data to show for artist " + command.getUsername() + ".");
    }

    return out;
  }

  /**
   * Retrieves the top 5 albums by the artist based on user history.
   *
   * @param artist The artist for whom the top albums are to be retrieved.
   * @return A map of album names and their respective play counts.
   */
  public Map<String, Integer> getTop5Albums(final User artist) {
    Map<String, Integer> albums = new LinkedHashMap<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG
            && !entry.getKey().equals(Library.getInstance().getSongs().get(0))) {
          String albumName = ((Song) entry.getKey()).getAlbum();
          String artistName = ((Song) entry.getKey()).getArtist();

          if (!(artist.getUsername().equals(artistName) && artist.hasAlbum(albumName))) {
            continue;
          }

          if (albums.containsKey(albumName)) {
            albums.put(albumName, albums.get(albumName) + entry.getValue());
          } else {
            albums.put(albumName, entry.getValue());
          }
        }
      }
    }

    return albums.entrySet().stream()
        .sorted(
            Map.Entry.<String, Integer>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry::getKey))
        .limit(Constants.PRINT_LIMIT)
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  /**
   * Retrieves the top 5 fans of the artist based on user history.
   *
   * @param artist The artist for whom the top fans are to be retrieved.
   * @return A list of usernames of the top fans.
   */
  public List<String> getTop5Fans(final User artist) {
    Map<String, Integer> fans = new LinkedHashMap<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG
            && !entry.getKey().equals(Library.getInstance().getSongs().get(0))) {
          Song song = (Song) entry.getKey();
          String artistName = song.getArtist();

          if (!artist.getUsername().equals(artistName)) {
            continue;
          }

          if (fans.containsKey(user.getUsername())) {
            int newCount = fans.get(user.getUsername()) + entry.getValue();
            fans.put(user.getUsername(), newCount);
          } else {
            fans.put(user.getUsername(), entry.getValue());
          }
        }
      }
    }

    return fans.entrySet().stream()
        .sorted(
            Map.Entry.<String, Integer>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry::getKey))
        .limit(Constants.PRINT_LIMIT)
        .map(Map.Entry::getKey)
        .collect(Collectors.toCollection(LinkedHashSet::new))
        .stream()
        .collect(Collectors.toList());
  }

  /**
   * Retrieves the number of listeners of the artist based on user history.
   *
   * @param artist The artist for whom the number of listeners is to be retrieved.
   * @return The number of listeners.
   */
  public int getListeners(final User artist) {
    Set<String> fans = new HashSet<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG
            && !entry.getKey().equals(Library.getInstance().getSongs().get(0))) {
          Song song = (Song) entry.getKey();
          String artistName = song.getArtist();

          if (!artist.getUsername().equals(artistName)) {
            continue;
          }

          fans.add(user.getUsername());
        }
      }
    }

    return fans.size();
  }
}
