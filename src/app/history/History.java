package app.history;

import static app.searchbar.SearchType.NOT_INITIALIZED;
import static app.searchbar.SearchType.SONG;

import app.Constants;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class History {
  private Map<AudioEntity, Integer> historyMap;

  public History() {
    this.setHistoryMap(new LinkedHashMap<>());
  }

  public void add(AudioEntity entity) {
    if (entity == null) {
      return;
    }

    if (getHistoryMap().containsKey(entity)) {
      int newCount = getHistoryMap().get(entity) + 1;
      getHistoryMap().put(entity, newCount);
    } else {
      getHistoryMap().put(entity, 1);
    }
  }

  public void add(History history) {
    for (Map.Entry<AudioEntity, Integer> entry : history.getHistoryMap().entrySet()) {
      if (getHistoryMap().containsKey(entry.getKey())) {
        int newCount = getHistoryMap().get(entry.getKey()) + entry.getValue();
        getHistoryMap().put(entry.getKey(), newCount);
      } else {
        getHistoryMap().put(entry.getKey(), entry.getValue());
      }
    }
  }

  public Map<String, Integer> getTop5Songs() {
    return historyMap.entrySet().stream()
        .filter(entry -> entry.getKey().getType() == SONG)
            .sorted(
                    Map.Entry.<AudioEntity, Integer>comparingByValue().reversed()
                            .thenComparing(entry -> entry.getKey().getName()).reversed()
                            .reversed())
            .limit(Constants.PRINT_LIMIT)
            .collect(
                    Collectors.toMap(
                            entry -> entry.getKey().getName(),
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
  }

  public Map<String, Integer> getTop5Episodes() {
    return historyMap.entrySet().stream()
        .filter(entry -> entry.getKey().getType() == NOT_INITIALIZED)
        .sorted(
            Map.Entry.<AudioEntity, Integer>comparingByValue().reversed()
                .thenComparing(entry -> entry.getKey().getName()).reversed()
                .reversed())
        .limit(Constants.PRINT_LIMIT)
        .collect(
            Collectors.toMap(
                entry -> entry.getKey().getName(),
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new));
  }

  public Map<String, Integer> getTop5Albums() {
    Map<String, Integer> albums = new HashMap<>();

    for (Map.Entry<AudioEntity, Integer> entry : getHistoryMap().entrySet()) {
      if (entry.getKey().getType() == SONG) {
        String albumName = ((Song) entry.getKey()).getAlbum();

        if (albums.containsKey(albumName)) {
          albums.put(albumName, albums.get(albumName) + entry.getValue());
        } else {
          albums.put(albumName, entry.getValue());
        }
      }
    }

    return albums.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(Constants.PRINT_LIMIT)
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  public Map<String, Integer> getTop5Genres() {
    Map<String, Integer> albums = new HashMap<>();

    for (Map.Entry<AudioEntity, Integer> entry : getHistoryMap().entrySet()) {
      if (entry.getKey().getType() == SONG) {
        String genre = ((Song) entry.getKey()).getGenre();

        if (albums.containsKey(genre)) {
          albums.put(genre, albums.get(genre) + entry.getValue());
        } else {
          albums.put(genre, entry.getValue());
        }
      }
    }

    return albums.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(Constants.PRINT_LIMIT)
            .collect(
                    Collectors.toMap(
                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }
}
