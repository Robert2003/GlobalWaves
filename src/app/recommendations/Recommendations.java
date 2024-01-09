package app.recommendations;

import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Recommendations {
	private List<Song> songRecommendations;
	private List<Playlist> playlistRecommendations;

	private AudioEntity lastRecommendation;

	public Recommendations() {
		setSongRecommendations(new ArrayList<>());
		setPlaylistRecommendations(new ArrayList<>());
	}

	public boolean hasRecommendations() {
		return !songRecommendations.isEmpty() || !playlistRecommendations.isEmpty();
	}

	public void add(AudioEntity audioEntity) {
		switch (audioEntity.getType()) {
			case SONG -> getSongRecommendations().add((Song) audioEntity);
			case PLAYLIST -> getPlaylistRecommendations().add((Playlist) audioEntity);
		}

		lastRecommendation = audioEntity;
	}
}
