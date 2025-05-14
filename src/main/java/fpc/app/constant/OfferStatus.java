package fpc.app.constant;

public enum OfferStatus {
  ENABLED,
  DISABLED;

  public static OfferStatus get(boolean isPaused) {
    return isPaused ? DISABLED : ENABLED;
  }
}
