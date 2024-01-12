package app.monetization.subscription;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

import static app.monetization.subscription.UserPremiumState.FREE;
import static app.monetization.subscription.UserPremiumState.PREMIUM;

@Getter
@Setter
public class PremiumHistory {
	private List<UserPremiumState> stateHistory;
	private List<Long> timestampHistory;

	public PremiumHistory() {
		setStateHistory(new LinkedList<>());
		setTimestampHistory(new LinkedList<>());

		addState(FREE, 0);
	}

	public PremiumHistory(final long timestamp) {
		setStateHistory(new LinkedList<>());
		setTimestampHistory(new LinkedList<>());

		addState(FREE, timestamp);
	}

	public void addState(final UserPremiumState state, final long timestamp) {
		getStateHistory().add(state);
		getTimestampHistory().add(timestamp);
	}

	public UserPremiumState getCurrentState() {
		return getStateHistory().get(getStateHistory().size() - 1);
	}

	public long getLastPremiumTimestamp() {
		if (!getStateHistory().contains(PREMIUM)) {
			return -1;
		}

		for (int i = getStateHistory().size() - 1; i >= 0; i--) {
			if (getStateHistory().get(i) == PREMIUM) {
				return getTimestampHistory().get(i);
			}
		}
		return -1;
	}
}
