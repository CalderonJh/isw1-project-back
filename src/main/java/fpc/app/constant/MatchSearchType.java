package fpc.app.constant;

public enum MatchSearchType {
  ALL,
  TICKET,
  SEASON_PASS;

  public static MatchSearchType fromString(String str) {
    if (str == null) return ALL;
    return switch (str) {
      case "ticket" -> TICKET;
      case "season" -> SEASON_PASS;
      default ->
          throw new IllegalArgumentException(
              "Invalid match search type: " + str + ". Use 'ticket' or 'season'");
    };
  }
}
