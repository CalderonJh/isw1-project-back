package fpc.app.constant;

public enum OfferStatusType {
  ENABLED,
  DISABLED;

  public static OfferStatusType get(boolean isPaused) {
    return isPaused ? DISABLED : ENABLED;
  }
}
