package app.history;

import library.entities.audio.AudioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedHistory {
	private AudioEntity entity;
	private long addTimestamp;

	public OrderedHistory(AudioEntity entity, long addTimestamp) {
		this.entity = entity;
		this.addTimestamp = addTimestamp;
	}
}
