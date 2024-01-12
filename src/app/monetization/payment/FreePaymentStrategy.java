package app.monetization.payment;

import library.entities.audio.AudioEntity;
import library.users.User;

import java.util.List;

public class FreePaymentStrategy implements PaymentStrategy{
	@Override
	public void pay(User user) {

	}

	@Override
	public List<AudioEntity> getEntitiesToPayForArtist(User user, String artistName) {
		return null;
	}
}
