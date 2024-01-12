package app.audio.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ad {
	private boolean shouldAdBePlayed = false;
	private long adStartTimestamp;
	private boolean adPlaying = false;
	private int price;

	public Ad() {
		this.setShouldAdBePlayed(false);
		this.setAdStartTimestamp(0);
		this.setAdPlaying(false);
	}

	public void resetAd() {
		this.setShouldAdBePlayed(false);
		this.setAdStartTimestamp(0);
		this.setAdPlaying(false);
		this.price = 0;
	}
}
