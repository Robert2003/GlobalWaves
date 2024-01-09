package app.recommendations.strategy;

import library.entities.audio.AudioEntity;
import library.users.User;

public interface RecommendationStrategy {
	public AudioEntity getRecommendation(User user);
}
