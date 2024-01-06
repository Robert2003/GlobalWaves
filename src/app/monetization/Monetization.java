package app.monetization;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import library.entities.Merch;
import library.entities.audio.AudioEntity;
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

  public void receivePayment(AudioEntity entity, double amount) {
    if (getSongRevenue().containsKey(entity)) {
      double newAmount = getSongRevenue().get(entity) + amount;
      getSongRevenue().put(entity, newAmount);
    } else {
      getSongRevenue().put(entity, amount);
    }
  }
  public void receivePayment(Merch merch) {
	  getMerchRevenue().add(merch);
      setTotalMerchRevenue(getTotalMerchRevenue() + merch.getPrice());
      setTotalRevenue(getTotalRevenue() + getTotalMerchRevenue());
  }
}
