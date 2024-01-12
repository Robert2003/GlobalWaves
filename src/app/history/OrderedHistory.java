package app.history;

import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedHistory {
	private AudioEntity entity;
	private long addTimestamp;
	private int price;

	public OrderedHistory(AudioEntity entity, long addTimestamp) {
		this.entity = entity;
		this.addTimestamp = addTimestamp;
	}

	public OrderedHistory(AudioEntity entity, long addTimestamp, int price) {
		this.entity = entity;
		this.addTimestamp = addTimestamp;
		this.price = price;
	}
}
