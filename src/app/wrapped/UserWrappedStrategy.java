package app.wrapped;

import app.Constants;
import app.commands.Executable;
import app.commands.executables.Wrapped;
import app.history.OrderedHistory;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import library.users.User;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static app.searchbar.SearchType.NOT_INITIALIZED;
import static app.searchbar.SearchType.SONG;

public class UserWrappedStrategy implements Executable {
	@Override
	public Node execute(InputNode command) {
		Wrapped.WrappedOutputNode out = new Wrapped.WrappedOutputNode(command);

		User user = Library.getInstance().getUserByName(command.getUsername());

		out.getResult().setTopSongs(getTop5Songs(user));
		out.getResult().setTopEpisodes(getTop5Episodes(user));
		out.getResult().setTopAlbums(getTop5Albums(user));
		out.getResult().setTopGenres(getTop5Genres(user));
		out.getResult().setTopArtists(getTop5Artists(user));

		if (out.getResult().getTopSongs().isEmpty()
				&& out.getResult().getTopAlbums().isEmpty()
				&& out.getResult().getTopGenres().isEmpty()
				&& out.getResult().getTopEpisodes().isEmpty()
				&& out.getResult().getTopArtists().isEmpty()) {
			out.setResult(null);
			out.setMessage("No data to show for user " + command.getUsername() + ".");
		}

		return out;
	}

	public Map<String, Integer> getTop5Songs(final User user) {
        Map<String, Integer> top5Songs = new LinkedHashMap<>();
		for (OrderedHistory order : user.getHistory().getOrderHistoryMap()) {
			if (order.getEntity().getType() == SONG) {
				Song song = (Song) order.getEntity();
				top5Songs.put(song.getName(), user.getHistory().getCount(song));
			}
		}

		return top5Songs.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
				.limit(Constants.PRINT_LIMIT)
				.collect(
						Collectors.toMap(
								Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


//		return user.getHistory().getHistoryMap().entrySet().stream()
//				.filter(entry -> entry.getKey().getType() == SONG)
//				.sorted(
//						Map.Entry.<AudioEntity, Integer>comparingByValue().reversed()
//								.thenComparing(entry -> entry.getKey().getName()).reversed()
//								.reversed())
//				.limit(Constants.PRINT_LIMIT)
//				.collect(
//						Collectors.toMap(
//								entry -> entry.getKey().getName(),
//								Map.Entry::getValue,
//								(e1, e2) -> e1,
//								LinkedHashMap::new));
	}

	public Map<String, Integer> getTop5Episodes(final User user) {
		return user.getHistory().getHistoryMap().entrySet().stream()
				.filter(entry -> entry.getKey().getType() == NOT_INITIALIZED)
				.sorted(
						Map.Entry.<AudioEntity, Integer>comparingByValue().reversed()
								.thenComparing(entry -> entry.getKey().getName()).reversed()
								.reversed())
				.limit(Constants.PRINT_LIMIT)
				.collect(
						Collectors.toMap(
								entry -> entry.getKey().getName(),
								Map.Entry::getValue,
								(e1, e2) -> e1,
								LinkedHashMap::new));
	}

	public Map<String, Integer> getTop5Albums(final User user) {
		Map<String, Integer> albums = new HashMap<>();

		for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
			if (entry.getKey().getType() == SONG) {
				String albumName = ((Song) entry.getKey()).getAlbum();

				if (albums.containsKey(albumName)) {
					albums.put(albumName, albums.get(albumName) + entry.getValue());
				} else {
					albums.put(albumName, entry.getValue());
				}
			}
		}

		return albums.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.limit(Constants.PRINT_LIMIT)
				.collect(
						Collectors.toMap(
								Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public Map<String, Integer> getTop5Genres(final User user) {
		Map<String, Integer> albums = new HashMap<>();

		for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
			if (entry.getKey().getType() == SONG) {
				String genre = ((Song) entry.getKey()).getGenre();

				if (albums.containsKey(genre)) {
					albums.put(genre, albums.get(genre) + entry.getValue());
				} else {
					albums.put(genre, entry.getValue());
				}
			}
		}

		return albums.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.limit(Constants.PRINT_LIMIT)
				.collect(
						Collectors.toMap(
								Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public Map<String, Integer> getTop5Artists(final User user) {
		Map<String, Integer> artists = new HashMap<>();

		for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
			if (entry.getKey().getType() == SONG) {
				String artistName = ((Song) entry.getKey()).getArtist();

				if (artists.containsKey(artistName)) {
					artists.put(artistName, artists.get(artistName) + entry.getValue());
				} else {
					artists.put(artistName, entry.getValue());
				}
			}
		}

		return artists.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry::getKey))
				.limit(Constants.PRINT_LIMIT)
				.collect(
						Collectors.toMap(
								Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
}
