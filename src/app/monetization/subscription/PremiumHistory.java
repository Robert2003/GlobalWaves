package app.monetization.subscription;

import static app.monetization.subscription.UserPremiumState.FREE;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PremiumHistory {
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

  /**
   * This method is used to add a new state to the user's premium state history. It takes a
   * UserPremiumState object and a timestamp as parameters. The state is added to the state history
   * and the timestamp is added to the timestamp history.
   *
   * @param state The new state to be added to the history.
   * @param timestamp The timestamp at which the state change occurred.
   */
  public void addState(final UserPremiumState state, final long timestamp) {
    getStateHistory().add(state);
    getTimestampHistory().add(timestamp);
  }

  /**
   * This method is used to return if the user has premium subscription or not.
   *
   * @return The current state of the user's premium subscription.
   */
  public UserPremiumState getCurrentState() {
    return getStateHistory().get(getStateHistory().size() - 1);
  }
}
