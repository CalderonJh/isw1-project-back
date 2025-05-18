package fpc.app.util;

import fpc.app.dto.util.Suggestion;
import fpc.app.exception.ValidationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tools {

  private Tools() {}

  public static Suggestion mapSuggestion(Object id, String description) {
    return new Suggestion(id, description);
  }

  public static boolean hasText(String text) {
    return text != null && !text.isBlank();
  }

  public static String removeExtraSpaces(String str) {
    if (str == null) return null;
    return str.replaceAll("\\s+", " ").trim();
  }

  public static boolean equalsText(@NonNull String t1, @NonNull String t2) {
    return removeExtraSpaces(t1).equalsIgnoreCase(removeExtraSpaces(t2));
  }

  public static LocalDateTime getColTime() {
    return LocalDateTime.now(ZoneId.of("America/Bogota"));
  }

  public static void validateIsFutureDate(LocalDateTime date) {
    validateIsFutureDate(date, "Date must be in the future");
  }

  public static void validateIsFutureDate(LocalDateTime date, String message) {
    if (!isFutureDate(date)) {
      throw new ValidationException(message);
    }
  }

  public static boolean isFutureDate(LocalDateTime date) {
    return date.isAfter(getColTime());
  }

  public static void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    if (startDate.isAfter(endDate)) {
      throw new ValidationException("Start date must be before end date");
    }
  }

}
