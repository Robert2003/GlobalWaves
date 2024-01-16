package app.recommendations.strategy.concrete;

import static app.Constants.SONGS_FIRST_GENRE;
import static app.Constants.SONGS_SECOND_GENRE;
import static app.Constants.SONGS_THIRD_GENRE;
import static app.Constants.RANDOM_PLAYLIST_LIMIT;

import app.Constants;
import app.recommendations.strategy.RecommendationStrategy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import library.users.User;

public final class RandomPlaylistRecommendationStrategy implements RecommendationStrategy {
  private static void addLikedSongs(final User user, final Map<String, Integer> genres) {
    for (Song song : user.getLikedSongs()) {
      genres.put(song.getGenre(), genres.getOrDefault(song.getGenre(), 0) + 1);
    }
  }

  private static void addPlaylistsSongs(
      final List<Playlist> playlists, final Map<String, Integer> genres) {
    for (Playlist playlist : playlists) {
      for (Song song : playlist.getSongs()) {
        String genre = song.getGenre();

        if (genres.containsKey(genre)) {
          genres.put(genre, genres.get(genre) + 1);
        } else {
          genres.put(genre, 1);
        }
      }
    }
  }

  private boolean appendTopKSongsForGenre(
      final List<Song> result, final int k, final String genre) {
    List<Song> songs =
        Library.getInstance().getSongs().stream()
            .filter(song -> song.getGenre().equals(genre) && !result.contains(song))
            .sorted((s1, s2) -> s2.getNumberOfLikes() - s1.getNumberOfLikes())
            .limit(k)
            .collect(Collectors.toList());

    if (songs.isEmpty()) {
      return false;
    }

    result.addAll(songs);
    return true;
  }

  @Override
  public AudioEntity getRecommendation(final User user) {
    List<String> top3Genres = getTop3Genres(user);

    List<Song> playlistSongs = new ArrayList<>();
    List<Integer> limits = List.of(SONGS_FIRST_GENRE, SONGS_SECOND_GENRE, SONGS_THIRD_GENRE);

    for (int i = 0; i < top3Genres.size(); i++) {
      if (!appendTopKSongsForGenre(playlistSongs, limits.get(i), top3Genres.get(i))) {
        return null;
      }
    }

    if (playlistSongs.isEmpty()) {
      return null;
    }

    String playlistName = user.getUsername() + "'s recommendations";
    return new Playlist(playlistName, playlistSongs, user);
  }

  private List<String> getTop3Genres(final User user) {
    Map<String, Integer> genres = new LinkedHashMap<>();
    List<String> top3Genres = new ArrayList<>();

    addLikedSongs(user, genres);
    addPlaylistsSongs(user.getOwnedPlaylists(), genres);
    addPlaylistsSongs(user.getFollowedPlaylists(), genres);

    genres =
        genres.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(Constants.PRINT_LIMIT)
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    int cnt = 0;
    for (Map.Entry<String, Integer> entry : genres.entrySet()) {
      top3Genres.add(entry.getKey());

      cnt++;
      if (cnt == RANDOM_PLAYLIST_LIMIT) {
        break;
      }
    }

    return top3Genres;
  }
}
