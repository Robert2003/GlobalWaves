package app.recommendations.strategy.concrete;

import static app.searchbar.SearchType.SONG;

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

public class RandomPlaylistRecommendationStrategy implements RecommendationStrategy {
  private static void addLikedSongs(User user, Map<String, Integer> genres) {
    for (Song song : user.getLikedSongs()) {
      genres.put(song.getGenre(), genres.getOrDefault(song.getGenre(), 0) + 1);
    }
//    for (Map.Entry<AudioEntity, Integer> entry : user.getHistory().getHistoryMap().entrySet()) {
//      if (entry.getKey().getType() == SONG) {
//        String genre = ((Song) entry.getKey()).getGenre();
//
//        if (genres.containsKey(genre)) {
//          genres.put(genre, genres.get(genre) + entry.getValue());
//        } else {
//          genres.put(genre, entry.getValue());
//        }
//      }
//    }
  }

  private static void addPlaylistsSongs(List<Playlist> playlists, Map<String, Integer> genres) {
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

  private boolean appendTopKSongsForGenre(List<Song> result, int k, String genre) {
    List<Song> songs = Library.getInstance().getSongs().stream()
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
  public AudioEntity getRecommendation(User user) {
    List<String> top3Genres = getTop3Genres(user);

    List<Song> playlistSongs = new ArrayList<>();
    List<Integer> limits = List.of(5, 3, 2);

    for (int i = 0; i < top3Genres.size(); i++) {
      if (appendTopKSongsForGenre(playlistSongs, limits.get(i), top3Genres.get(i)) == false) {
        return null;
      }
    }

    if (playlistSongs.isEmpty()) {
      return null;
    }

    String playlistName = user.getUsername() + "'s recommendations";
    return new Playlist(playlistName, playlistSongs, user);
  }

  private List<String> getTop3Genres(User user) {
    Map<String, Integer> genres = new LinkedHashMap<>();
    List<String> top3Genres = new ArrayList<>();

//    addLikedSongs(user, genres);
//
//    Map<String, Integer> likedSongsGenres = genres.entrySet().stream()
//            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//            .limit(Constants.PRINT_LIMIT)
//            .collect(
//                    Collectors.toMap(
//                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//
//    if (!likedSongsGenres.isEmpty()) {
//      Map.Entry<String, Integer> firstEntry = likedSongsGenres.entrySet().iterator().next();
//
//      if (!top3Genres.contains(firstEntry.getKey())) {
//        top3Genres.add(firstEntry.getKey());
//      }
//    }
//
//    genres.clear();
//    addPlaylistsSongs(user.getOwnedPlaylists(), genres);
//
//    Map<String, Integer> ownedPlaylistsGenres = genres.entrySet().stream()
//            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//            .limit(Constants.PRINT_LIMIT)
//            .collect(
//                    Collectors.toMap(
//                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//
//    if (!ownedPlaylistsGenres.isEmpty()) {
//      Map.Entry<String, Integer> firstEntry = ownedPlaylistsGenres.entrySet().iterator().next();
//
//      if (!top3Genres.contains(firstEntry.getKey())) {
//        top3Genres.add(firstEntry.getKey());
//      }
//    }
//
//    genres.clear();
//    addPlaylistsSongs(user.getFollowedPlaylists(), genres);
//
//    Map<String, Integer> followedPlaylistsGenres = genres.entrySet().stream()
//            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//            .limit(Constants.PRINT_LIMIT)
//            .collect(
//                    Collectors.toMap(
//                            Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//
//    if (!followedPlaylistsGenres.isEmpty()) {
//      Map.Entry<String, Integer> firstEntry = followedPlaylistsGenres.entrySet().iterator().next();
//
//      if (!top3Genres.contains(firstEntry.getKey())) {
//        top3Genres.add(firstEntry.getKey());
//      }
//    }

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
      if (cnt == 3) {
        break;
      }
    }

    return top3Genres;
  }
}
