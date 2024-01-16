package app.monetization.payment;

import java.util.List;
import library.Library;
import library.entities.audio.audioFiles.Song;
import library.users.User;

public final class PremiumPaymentStrategy implements PaymentStrategy {
  @Override
  public void pay(final User user, final double price) {
    List<Song> premiumSongs = user.getHistory().getPremiumSongs();

    List<String> artists = getAllUniqueArtistsFrom(premiumSongs);

    for (String artistName : artists) {
      User artist = Library.getInstance().getUserByName(artistName);
      List<Song> entitiesToPay =
          premiumSongs.stream().filter(song -> song.getArtist().equals(artistName)).toList();
      double value = price * entitiesToPay.size() / premiumSongs.size();

      artist.getMonetization().receivePayment(entitiesToPay, value);
    }

    user.getHistory().getPremiumSongs().clear();
  }
}
