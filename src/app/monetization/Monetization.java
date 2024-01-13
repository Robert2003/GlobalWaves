package app.monetization;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import library.entities.Merch;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Monetization {
  private Map<AudioEntity, Double> songRevenue;
  private List<Merch> merchRevenue;

  private double totalSongRevenue;
  private double totalMerchRevenue;
  private double totalRevenue;

  public Monetization() {
    this.setSongRevenue(new LinkedHashMap<>());
    this.setMerchRevenue(new ArrayList<>());
  }

  public void receivePayment(List<Song> paidEntities, double totalAmount) {
    double individualSongRevenue = totalAmount / paidEntities.size();

    setTotalSongRevenue(getTotalSongRevenue() + totalAmount);
    setTotalRevenue(getTotalRevenue() + totalAmount);

    for (AudioEntity entity : paidEntities) {
      double newAmount = getSongRevenue().getOrDefault(entity, 0.0) + individualSongRevenue;
      getSongRevenue().put(entity, newAmount);
    }
  }

  public void receivePayment(Merch merch) {
    getMerchRevenue().add(merch);
    setTotalMerchRevenue(getTotalMerchRevenue() + merch.getPrice());
    setTotalRevenue(getTotalRevenue() + getTotalMerchRevenue());
  }
}
