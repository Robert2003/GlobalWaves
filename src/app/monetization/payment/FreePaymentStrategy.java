package app.monetization.payment;

import java.util.List;
import library.Library;
import library.entities.audio.audioFiles.Song;
import library.users.User;

public final class FreePaymentStrategy implements PaymentStrategy {
  @Override
  public void pay(final User user, final double price) {
    List<Song> freeSongs = user.getHistory().getFreeSongs();

    List<String> artists = getAllUniqueArtistsFrom(freeSongs);

    for (String artistName : artists) {
      User artist = Library.getInstance().getUserByName(artistName);
      List<Song> entitiesToPay =
          freeSongs.stream().filter(song -> song.getArtist().equals(artistName)).toList();
      double value = price * entitiesToPay.size() / freeSongs.size();

      artist.getMonetization().receivePayment(entitiesToPay, value);
    }

    user.getHistory().getFreeSongs().clear();
  }
}
