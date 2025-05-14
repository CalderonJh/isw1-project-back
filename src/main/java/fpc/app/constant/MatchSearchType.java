package fpc.app.constant;

public enum MatchSearchType {
  ALL,
  TICKET,
  SEASON_PASS;

  public static MatchSearchType fromString(String str) {
    if (str == null) return ALL;
    return switch (str) {
      case "T" -> TICKET;
      case "S" -> SEASON_PASS;
      default ->
          throw new IllegalArgumentException(
              "Invalid match search type: " + str + ". Use 'T' for TICKET or 'S' for SEASON_PASS");
    };
  }
}
