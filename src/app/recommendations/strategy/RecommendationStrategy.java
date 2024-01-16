package app.recommendations.strategy;

import library.entities.audio.AudioEntity;
import library.users.User;

public interface RecommendationStrategy {
  /**
   * Provides a recommendation based on the user's recommendations.
   *
   * @param user The user for whom the recommendation is to be made.
   * @return The recommended audio entity.
   */
  AudioEntity getRecommendation(User user);
}
