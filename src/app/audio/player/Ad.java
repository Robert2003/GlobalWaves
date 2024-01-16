package app.audio.player;

import app.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Ad {
  private boolean shouldAdBePlayed = false;
  private long adStartTimestamp;
  private long leftTimestamp;
  private boolean adPlaying = false;
  private int price;

  public Ad() {
    this.setShouldAdBePlayed(false);
    this.setAdStartTimestamp(0);
    this.setAdPlaying(false);
    this.setLeftTimestamp(Constants.AD_LENGTH);
  }

  /** Resets the ad properties to their default values. */
  public void resetAd() {
    this.setShouldAdBePlayed(false);
    this.setAdStartTimestamp(0);
    this.setAdPlaying(false);
    this.setLeftTimestamp(Constants.AD_LENGTH);
    this.setPrice(0);
  }
}
