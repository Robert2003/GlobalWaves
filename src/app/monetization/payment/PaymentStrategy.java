package app.monetization.payment;

import java.util.ArrayList;
import java.util.List;
import library.entities.audio.audioFiles.Song;
import library.users.User;

public interface PaymentStrategy {
  /**
   * This method is used to process a payment from a user. The method takes a User object and a
   * price as parameters. The implementation of this method will depend on the specific payment
   * strategy.
   *
   * @param user The user who is making the payment.
   * @param price The price of the item(s) the user is paying for.
   */
  void pay(User user, double price);

  /**
   * This method is used to get a list of all unique artists from a list of songs. The method
   * creates a new list of artists, then iterates over the list of songs. For each song, if its
   * artist is not already in the list of artists, it is added. The method then returns the list of
   * artists.
   *
   * @param entities The list of songs from which to get the artists.
   * @return A list of all unique artists from the provided list of songs.
   */
  default List<String> getAllUniqueArtistsFrom(List<Song> entities) {
    List<String> artists = new ArrayList<>();
    for (Song song : entities) {
      if (!artists.contains(song.getArtist())) {
        artists.add(song.getArtist());
      }
    }
    return artists;
  }
}
