package app.commands.executables;

import app.Constants;
import app.audio.player.states.PlayerPlayPauseStates;
import app.commands.Executable;
import app.helpers.ConnectionStatus;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.io.nodes.output.PlayerOutputNode;
import app.monetization.subscription.UserPremiumState;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audioFiles.Song;
import library.users.User;

import static app.searchbar.SearchType.SONG;

public class LoadRecommendations implements Executable {
  @Override
  public Node execute(InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return null;
    }

    if (user.getConnectionStatus() == ConnectionStatus.OFFLINE) {
      return new PlayerOutputNode(
          command, command.getUsername() + Constants.IS_OFFLINE_ERROR_MESSAGE);
    }

    if (!user.getRecommendations().hasRecommendations()) {
      return new PlayerOutputNode(command, "No recommendations available.");
    }

    AudioEntity lastRecommendation = user.getRecommendations().getLastRecommendation();

    return load(command, lastRecommendation);
  }

  private PlayerOutputNode load(final InputNode command, AudioEntity lastRecommendation) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    user.getAudioPlayer().setLoadedTrack(lastRecommendation);
    user.getAudioPlayer().getLoadedTrack().setStartTimestamp(command.getTimestamp());
    user.getAudioPlayer().getTimeManager().setLastTimeUpdated(command.getTimestamp());
    user.getAudioPlayer().getTimeManager().setElapsedTime(0L);
    user.getAudioPlayer().setPlayPauseState(PlayerPlayPauseStates.PLAYING);
    user.getSearchBar().setSelectedTrack(null);

    AudioEntity playingEntity =
        user.getAudioPlayer().getTimeManager().getPlayingAudioEntity(user.getAudioPlayer());
    user.getHistory().add(lastRecommendation, command.getTimestamp());

    if (playingEntity.getType() == SONG) {
      if (user.getPremiumState() == UserPremiumState.PREMIUM) {
        user.getHistory().getPremiumSongs().add((Song) playingEntity);
      } else {
        user.getHistory().getFreeSongs().add((Song) playingEntity);
      }
    }

      user.getAudioPlayer().getAd().resetAd();

    return new PlayerOutputNode(command, Constants.LOAD_NO_ERROR_MESSAGE);
  }
}
