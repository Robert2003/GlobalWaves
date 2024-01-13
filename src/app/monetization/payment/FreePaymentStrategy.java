package app.monetization.payment;

import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import library.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static app.searchbar.SearchType.SONG;

public class FreePaymentStrategy implements PaymentStrategy{
	@Override
	public void pay(User user, double price) {
		long lastAdTimestamp = user.getHistory().getLastAdTimestamp();
		if (lastAdTimestamp == -1) {
			lastAdTimestamp = user.getPremiumHistory().getLastFreeTimestamp();
		}
		if (lastAdTimestamp == -1) {
			return;
		}

		int lastPayIndex = user.getHistory().getIndexForTimestamp(lastAdTimestamp);
		if (lastPayIndex == -1) {
			lastPayIndex = 0;
		}
		int currentIndex = user.getHistory().getOrderHistoryMap().size() - 1;
		int songsListened = currentIndex - lastPayIndex + 1;

		Map<User, Integer> listenedArtists = user.getHistory().getListenedArtistsBetween(lastPayIndex, currentIndex);

		for (Map.Entry<User, Integer> entry : listenedArtists.entrySet()) {
			User artist = entry.getKey();
			int songsFromArtist = entry.getValue();

			double value = price * songsFromArtist / songsListened;
			List<AudioEntity> entitiesToPay = getEntitiesToPayForArtist(user, artist.getUsername());

			artist.getMonetization().receivePayment(entitiesToPay, value);
		}

//		System.out.println();

	}

	@Override
	public List<AudioEntity> getEntitiesToPayForArtist(User user, String artistName) {
		List<AudioEntity> entitiesToPay = new ArrayList<>();

		long lastAdTimestamp = user.getHistory().getLastAdTimestamp();
		if (lastAdTimestamp == -1) {
			lastAdTimestamp = user.getPremiumHistory().getLastFreeTimestamp();
		}
		int lastPayIndex = user.getHistory().getIndexForTimestamp(lastAdTimestamp);
		if (lastPayIndex == -1) {
			lastPayIndex = 0;
		}
		int currentIndex = user.getHistory().getOrderHistoryMap().size() - 1;

		for (int i = lastPayIndex; i <= currentIndex; i++) {
			AudioEntity entity = user.getHistory().getOrderHistoryMap().get(i).getEntity();
			if (entity.getType() == SONG && !entity.equals(Library.getInstance().getSongs().get(0))) {
				Song song = (Song) entity;
				if (song.getArtist().equals(artistName)) {
					entitiesToPay.add(song);
				}
			}
		}

		return entitiesToPay;
	}
}
