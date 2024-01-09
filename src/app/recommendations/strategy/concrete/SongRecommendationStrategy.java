package app.recommendations.strategy.concrete;

import app.Constants;
import app.audio.player.AudioPlayer;
import app.recommendations.strategy.RecommendationStrategy;
import app.searchbar.SearchType;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import library.users.User;

import java.util.List;
import java.util.Random;

public class SongRecommendationStrategy implements RecommendationStrategy {
	@Override
	public AudioEntity getRecommendation(User user) {
		Random random;
		AudioPlayer player = user.getAudioPlayer();
		AudioEntity audioEntity = player.getTimeManager().getPlayingAudioEntity(player);

		if (audioEntity == null && audioEntity.getType() != SearchType.SONG) {
			return null;
		}

		Song playingSong = (Song) audioEntity;

		int songDuration = playingSong.getDuration();
		long remainingTime = player.getTimeManager().getRemainingTime(player);
		int playedTime = songDuration - (int) remainingTime;

		if (playedTime < Constants.RECOMMENDATION_TIME) {
			return null;
		}

		random = new Random(playedTime);

		List<Song> sameGenreSongs =
				Library.getInstance().getSongs().stream()
						.filter(song -> song.getGenre().equals(playingSong.getGenre()))
						.toList();

		if (sameGenreSongs.isEmpty()) {
			return null;
		}

		return sameGenreSongs.get(random.nextInt(sameGenreSongs.size()));
	}
}
