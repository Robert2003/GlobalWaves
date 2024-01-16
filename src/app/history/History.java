package app.history;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class History {
  private Map<AudioEntity, Integer> historyMap;
  private List<OrderedHistory> orderHistoryMap;
  private List<OrderedHistory> adsHistory;
  private List<Song> premiumSongs;
  private List<Song> freeSongs;

  public History() {
    this.setHistoryMap(new LinkedHashMap<>());
    this.setOrderHistoryMap(new ArrayList<>());
    this.setAdsHistory(new ArrayList<>());
    setFreeSongs(new ArrayList<>());
    setPremiumSongs(new ArrayList<>());
  }

  /**
   * This method is used to add an AudioEntity to the history. If the entity is null, the method
   * will return without doing anything. If the entity is the first song in the library, it is
   * considered an ad and added to the ads history. If the entity is already in the history, its
   * count is incremented and it is added to the ordered history with the current timestamp. If the
   * entity is not in the history, it is added with a count of 1 and also added to the ordered
   * history with the current timestamp.
   *
   * @param entity The AudioEntity to be added to the history.
   * @param timestamp The timestamp at which the entity is added.
   */
  public void add(final AudioEntity entity, final long timestamp) {
    if (entity == null) {
      return;
    }

    if (entity.equals(Library.getInstance().getSongs().get(0))) {
      getAdsHistory().add(new OrderedHistory(entity, timestamp));
      return;
    }

    if (getHistoryMap().containsKey(entity)) {
      int newCount = getHistoryMap().get(entity) + 1;
      getHistoryMap().put(entity, newCount);
      getOrderHistoryMap().add(new OrderedHistory(entity, timestamp));
    } else {
      getHistoryMap().put(entity, 1);
      getOrderHistoryMap().add(new OrderedHistory(entity, timestamp));
    }
  }

  /**
   * This method is used to merge another History object into this one. For each AudioEntity in the
   * other history, if it is already in this history, its count is incremented by the count in the
   * other history and it is added to the ordered history with the current timestamp. If it is not
   * in this history, it is added with the count from the other history and also added to the
   * ordered history with the current timestamp.
   *
   * @param history The History object to be merged into this one.
   * @param timestamp The timestamp at which the merge is happening.
   */
  public void add(final History history, final long timestamp) {
    for (Map.Entry<AudioEntity, Integer> entry : history.getHistoryMap().entrySet()) {
      if (getHistoryMap().containsKey(entry.getKey())) {
        int newCount = getHistoryMap().get(entry.getKey()) + entry.getValue();
        getHistoryMap().put(entry.getKey(), newCount);
        getOrderHistoryMap().add(new OrderedHistory(entry.getKey(), timestamp));
      } else {
        getHistoryMap().put(entry.getKey(), entry.getValue());
        getOrderHistoryMap().add(new OrderedHistory(entry.getKey(), timestamp));
      }
    }
  }

  /**
   * This method is used to get the count of a specific AudioEntity in the ordered history. If the
   * entity is null, the method will return 0. The method iterates over the ordered history and
   * increments a counter each time it encounters the specified entity.
   *
   * @param entity The AudioEntity whose count is to be retrieved.
   * @return The count of the specified AudioEntity in the ordered history.
   */
  public int getCount(final AudioEntity entity) {
    if (entity == null) {
      return 0;
    }

    int cnt = 0;
    for (OrderedHistory order : getOrderHistoryMap()) {
      if (order.getEntity().getName().equals(entity.getName())) {
        cnt++;
      }
    }

    return cnt;
  }
}
