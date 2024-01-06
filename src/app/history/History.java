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
}
