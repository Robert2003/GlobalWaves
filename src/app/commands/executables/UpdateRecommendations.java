package app.commands.executables;

import app.commands.Executable;
import app.helpers.UserType;
import app.io.nodes.Node;
import app.io.nodes.input.InputNode;
import app.recommendations.RecommendationType;
import app.recommendations.strategy.concrete.FansPlaylistRecommendationStrategy;
import app.recommendations.strategy.concrete.RandomPlaylistRecommendationStrategy;
import app.recommendations.strategy.concrete.SongRecommendationStrategy;
import app.searchbar.SearchType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import library.Library;
import library.entities.audio.AudioEntity;
import library.entities.audio.audio.collections.Playlist;
import library.entities.audio.audioFiles.Song;
import library.users.User;
import lombok.Getter;
import lombok.Setter;

import static app.Constants.DOESNT_EXIST;
import static app.Constants.NOT_NORMAL_USER_ERROR_MESSAGE;
import static app.Constants.THE_USERNAME;

public class UpdateRecommendations implements Executable {
  @Override
  public Node execute(InputNode command) {
    User user = Library.getInstance().getUserByName(command.getUsername());

    if (user == null) {
      return new UpdateRecommendationsOutputNode(
          command, THE_USERNAME + command.getUsername() + DOESNT_EXIST);
    }


    if (user.getUserType() != UserType.NORMAL) {
      return new UpdateRecommendationsOutputNode(
          command, command.getUsername() + NOT_NORMAL_USER_ERROR_MESSAGE);
    }

    AudioEntity recommendation;

    RecommendationType type = RecommendationType.valueOf(command.getRecommendationType().toUpperCase());
    recommendation = switch (type) {
      case RANDOM_SONG -> new SongRecommendationStrategy().getRecommendation(user);
      case RANDOM_PLAYLIST -> new RandomPlaylistRecommendationStrategy().getRecommendation(user);
      case FANS_PLAYLIST -> new FansPlaylistRecommendationStrategy().getRecommendation(user);
    };

    if (recommendation == null) {
      return new UpdateRecommendationsOutputNode(
          command, "No new recommendations were found");
    }

    if (user.getRecommendations().hasRecommendations()) {
      switch (type) {
        case RANDOM_SONG:
          if (user.getRecommendations().getSongRecommendations().contains(recommendation)) {
            return new UpdateRecommendationsOutputNode(
                command, "No new recommendations were found");
          }
          break;
        case RANDOM_PLAYLIST:
        case FANS_PLAYLIST:
          for (Playlist playlist : user.getRecommendations().getPlaylistRecommendations()) {
            if (playlist.getSongs().containsAll(((Playlist) recommendation).getSongs())) {
              return new UpdateRecommendationsOutputNode(
                      command, "No new recommendations were found");
            }
          }
          if (user.getRecommendations().getLastRecommendation().getType() == SearchType.PLAYLIST && ((Playlist)user.getRecommendations().getLastRecommendation()).getSongs().containsAll(((Playlist) recommendation).getSongs())) {
            return new UpdateRecommendationsOutputNode(
                command, "No new recommendations were found");
          }
      }
    }

    user.getRecommendations().add(recommendation);

    return new UpdateRecommendationsOutputNode(
        command, "The recommendations for user " + user.getUsername() + " have been updated successfully.");
  }

  @Getter
  @Setter
  @JsonPropertyOrder({"command", "user", "timestamp", "message"})
  private final class UpdateRecommendationsOutputNode extends Node {
    private String user;
    private String message;

    UpdateRecommendationsOutputNode(final InputNode command, final String message) {
      this.setCommand(command.getCommand());
      this.setTimestamp(command.getTimestamp());
      this.setUser(command.getUsername());
      this.setMessage(message);
    }
  }
}
