package app.monetization.payment;

import library.entities.audio.AudioEntity;
import library.users.User;

import java.util.List;

public interface PaymentStrategy {
	void pay(User user);

	List<AudioEntity> getEntitiesToPayForArtist(User user, String artistName);
}
