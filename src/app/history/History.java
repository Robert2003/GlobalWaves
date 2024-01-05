package app.history;

import app.Constants;
import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.searchbar.SearchType.NOT_INITIALIZED;
import static app.searchbar.SearchType.SONG;

@Getter
@Setter
public class History {
  private Map<AudioEntity, Integer> historyMap;

  public History() {
    this.setHistoryMap(new HashMap<>());
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

  public Map<String, Integer> getTop5Songs () {
    return historyMap.entrySet().stream()
            .filter(entry -> entry.getKey().getType() == SONG)
            .sorted(Map.Entry.<AudioEntity, Integer>comparingByValue().reversed())
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
  }

  public Map<String, Integer> getTop5Episodes () {
    return historyMap.entrySet().stream()
            .filter(entry -> entry.getKey().getType() == NOT_INITIALIZED)
            .sorted(Map.Entry.<AudioEntity, Integer>comparingByValue().reversed())
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
  }
}
