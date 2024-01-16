package app.recommendations.strategy.concrete;

import static app.searchbar.SearchType.SONG;

import app.Constants;
import app.recommendations.strategy.RecommendationStrategy;
import app.wrapped.ArtistWrappedStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import library.users.User;

public final class FansPlaylistRecommendationStrategy implements RecommendationStrategy {
  private void appendTop5SongsForUser(final List<Song> result, final User user) {
    List<Song> top5Songs =
        user.getLikedSongs().stream()
            .filter(s1 -> !result.contains(s1))
            .sorted((s1, s2) -> s2.getNumberOfLikes() - s1.getNumberOfLikes())
            .limit(Constants.PRINT_LIMIT)
            .collect(Collectors.toList());

    result.addAll(top5Songs);
  }

  @Override
  public AudioEntity getRecommendation(final User user) {
    AudioEntity playingEntity =
        user.getAudioPlayer().getTimeManager().getPlayingAudioEntity(user.getAudioPlayer());

    if (playingEntity == null || playingEntity.getType() != SONG) {
      return null;
    }

    User artist = Library.getInstance().getUserByName(((Song) playingEntity).getArtist());

    List<String> top5Fans = new ArtistWrappedStrategy().getTop5Fans(artist);
    List<Song> playlistSongs = new ArrayList<>();

    for (String fan : top5Fans) {
      User fanUser = Library.getInstance().getUserByName(fan);
      appendTop5SongsForUser(playlistSongs, fanUser);
    }

    if (playlistSongs.isEmpty()) {
      return null;
    }

    String playlistName = artist.getUsername() + " Fan Club recommendations";
    return new Playlist(playlistName, playlistSongs, user);
  }
}
