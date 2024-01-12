package app.monetization.payment;

import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import library.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static app.searchbar.SearchType.SONG;

public class PremiumPaymentStrategy implements PaymentStrategy{
	@Override
	public void pay(User user) {
		long lastPremiumTimestamp = user.getPremiumHistory().getLastPremiumTimestamp();

		if (lastPremiumTimestamp == -1) {
			return;
		}

		int lastPayIndex = user.getHistory().getIndexForTimestamp(lastPremiumTimestamp);
		int currentIndex = user.getHistory().getOrderHistoryMap().size() - 1;
		int songsListened = currentIndex - lastPayIndex + 1;

		Map<User, Integer> listenedArtists = user.getHistory().getListenedArtistsBetween(lastPayIndex, currentIndex);

		for (Map.Entry<User, Integer> entry : listenedArtists.entrySet()) {
			User artist = entry.getKey();
			int songsFromArtist = entry.getValue();

			double value = 1E6 * songsFromArtist / songsListened;

			List<AudioEntity> entitiesToPay = getEntitiesToPayForArtist(user, artist.getUsername());
			artist.getMonetization().receivePayment(entitiesToPay, value);
		}
	}

	@Override
	public List<AudioEntity> getEntitiesToPayForArtist(User user, String artistName) {
		List<AudioEntity> entitiesToPay = new ArrayList<>();

		long lastPremiumTimestamp = user.getPremiumHistory().getLastPremiumTimestamp();
		int lastPayIndex = user.getHistory().getIndexForTimestamp(lastPremiumTimestamp);
		int currentIndex = user.getHistory().getOrderHistoryMap().size() - 1;

		for (int i = lastPayIndex; i <= currentIndex; i++) {
			AudioEntity entity = user.getHistory().getOrderHistoryMap().get(i).getEntity();
			if (entity.getType() == SONG) {
				Song song = (Song) entity;
				if (song.getArtist().equals(artistName)) {
					entitiesToPay.add(song);
				}
			}
		}

		return entitiesToPay;
	}
}
