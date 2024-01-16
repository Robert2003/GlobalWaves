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
public final class Monetization {
  private Map<AudioEntity, Double> songRevenue;
  private List<Merch> merchRevenue;

  private double totalSongRevenue;
  private double totalMerchRevenue;
  private double totalRevenue;

  public Monetization() {
    this.setSongRevenue(new LinkedHashMap<>());
    this.setMerchRevenue(new ArrayList<>());
  }

  /**
   * This method is used to distribute the total payment received among the songs that were paid
   * for. It first calculates the individual revenue for each song by dividing the total amount by
   * the number of songs. It then increments the total song revenue and the total revenue by the
   * total amount. Finally, it iterates over the paid songs, and for each song, it increments its
   * revenue by the individual song revenue.
   *
   * @param paidEntities The list of songs that were paid for.
   * @param totalAmount The total amount of payment received.
   */
  public void receivePayment(final List<Song> paidEntities, final double totalAmount) {
    double individualSongRevenue = totalAmount / paidEntities.size();

    setTotalSongRevenue(getTotalSongRevenue() + totalAmount);
    setTotalRevenue(getTotalRevenue() + totalAmount);

    for (AudioEntity entity : paidEntities) {
      double newAmount = getSongRevenue().getOrDefault(entity, 0.0) + individualSongRevenue;
      getSongRevenue().put(entity, newAmount);
    }
  }

  /**
   * This method is used to process the payment received for a piece of merchandise. It first adds
   * the merchandise to the merchandise revenue list. It then increments the total merchandise
   * revenue and the total revenue by the price of the merchandise.
   *
   * @param merch The merchandise that was paid for.
   */
  public void receivePayment(final Merch merch) {
    getMerchRevenue().add(merch);
    setTotalMerchRevenue(getTotalMerchRevenue() + merch.getPrice());
    setTotalRevenue(getTotalRevenue() + getTotalMerchRevenue());
  }
}
