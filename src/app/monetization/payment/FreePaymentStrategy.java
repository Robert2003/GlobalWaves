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
		List<Song> freeSongs = user.getHistory().getFreeSongs();

		List<String> artists = getArtists(freeSongs);

		for (String artistName : artists) {
			User artist = Library.getInstance().getUserByName(artistName);
			List<Song> entitiesToPay = freeSongs.stream().filter(song -> song.getArtist().equals(artistName)).toList();
			double value = price * entitiesToPay.size() / freeSongs.size();

			artist.getMonetization().receivePayment(entitiesToPay, value);
		}

		user.getHistory().getFreeSongs().clear();

	}

	private List<String> getArtists(List<Song> entities) {
		List<String> artists = new ArrayList<>();
		for (Song song : entities) {
			if (!artists.contains(song.getArtist())) {
				artists.add(song.getArtist());
			}
		}
		return artists;
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
