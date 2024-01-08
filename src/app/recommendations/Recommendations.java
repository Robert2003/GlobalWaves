package app.recommendations;

import app.Constants;
import app.audio.player.AudioPlayer;
import app.searchbar.SearchType;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Recommendations {
	List<Song> songRecommendations;
	List<Playlist> playlistRecommendations;

	public Recommendations() {
		setSongRecommendations(new ArrayList<>());
		setPlaylistRecommendations(new ArrayList<>());
	}


	public Song randomSong(User user) {
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
						.filter(song -> song.getGenre().equals(playingSong.getGenre()) && !song.equals(playingSong))
						.toList();

		if (sameGenreSongs.isEmpty()) {
			return null;
		}

		return sameGenreSongs.get(random.nextInt(sameGenreSongs.size()));
	}

	public Playlist randomPlaylist(User user) {
		return null;
	}
}
