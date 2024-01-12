package app.history;

import static app.searchbar.SearchType.SONG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class History {
  private Map<AudioEntity, Integer> historyMap;
  private List<OrderedHistory> orderHistoryMap;
  private List<OrderedHistory> adsHistory;

  public History() {
    this.setHistoryMap(new LinkedHashMap<>());
    this.setOrderHistoryMap(new ArrayList<>());
    this.setAdsHistory(new ArrayList<>());
  }

  public void add(AudioEntity entity, long timestamp) {
    if (entity == null) {
      return;
    }

    if (entity.equals(Library.getInstance().getSongs().get(0))) {
      getAdsHistory().add(new OrderedHistory(entity, timestamp));
      return;
    }

    if (entity.getType() == SONG && timestamp < 20668) {
      Song song = (Song) entity;

      if (song.getAlbum().equals("Greatest Hits")) {
        System.out.println("DA");
      }
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

  public void add(History history, long timestamp) {
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

  public int getCount(AudioEntity entity) {
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

  public int getIndexForTimestamp(long timestamp) {
    for (int i = 0; i < getOrderHistoryMap().size(); i++) {
      if (getOrderHistoryMap().get(i).getAddTimestamp() >= timestamp) {
        return i;
      }
    }

    return -1;
  }

  public Map<User, Integer> getListenedArtistsBetween(int index1, int index2) {
    Map<User, Integer> artists = new HashMap<>();

    for (int i = index1; i <= index2; i++) {
      AudioEntity entity = getOrderHistoryMap().get(i).getEntity();
      if (entity.getType() == SONG) {
        Song song = (Song) entity;
        User artist = Library.getInstance().getUserByName(song.getArtist());
        if (artist != null) {
          artists.put(artist, artists.getOrDefault(artist, 0) + 1);
        }
      }
    }

    return artists;
  }

  public long getLastAdTimestamp() {
    if (getAdsHistory().size() <= 1) {
      return -1;
    }

    return getAdsHistory().get(getAdsHistory().size() - 2).getAddTimestamp();
  }

  public void removeLastAd() {
    if (getAdsHistory().isEmpty()) {
      return;
    }
    getAdsHistory().remove(getAdsHistory().size() - 1);
  }
}
