package app.wrapped;

import static app.searchbar.SearchType.NOT_INITIALIZED;
import static app.searchbar.SearchType.SONG;

import app.Constants;
import app.commands.Executable;
import app.commands.executables.Wrapped;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import java.util.HashMap;
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

public class ArtistWrappedStrategy implements Executable {
  @Override
  public Node execute(InputNode command) {
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
      out.setMessage("No data to show for user " + command.getUsername() + ".");
    }

    return out;
  }

  public static Map<String, Integer> getTop5Songs(User artist) {
    Map<String, Integer> songs = new LinkedHashMap<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG) {
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

  public Map<String, Integer> getTop5Albums(User artist) {
    Map<String, Integer> albums = new LinkedHashMap<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG) {
          String albumName = ((Song) entry.getKey()).getAlbum();
          String artistName = ((Song) entry.getKey()).getArtist();

          if (!artist.getUsername().equals(artistName) || !artist.hasAlbum(albumName)) {
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

  public List<String> getTop5Fans(User artist) {
    Map<String, Integer> fans = new LinkedHashMap<>();
//    Set<String> fans = new HashSet<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG) {
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
            .sorted(Map.Entry.<String, Integer>comparingByValue()
                    .reversed()
                    .thenComparing(Map.Entry::getKey))
            .limit(Constants.PRINT_LIMIT)
            .map(Map.Entry::getKey)
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .stream()
            .collect(Collectors.toList());

//    return fans.stream()
//            .sorted(String::compareTo)
//            .limit(Constants.PRINT_LIMIT)
//            .collect(Collectors.toList());
  }

  public int getListeners (User artist) {
    Set<String> fans = new HashSet<>();

    for (User user : Library.getInstance().getNormalUsers()) {
      for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
        if (entry.getKey().getType() == SONG) {
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
