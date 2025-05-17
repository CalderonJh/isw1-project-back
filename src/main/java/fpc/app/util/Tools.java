package fpc.app.util;

import fpc.app.dto.util.Suggestion;
import fpc.app.exception.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tools {

  /**
   * Maps an object to a Suggestion object. The source object must have <code>getId()</code> and
   * <code>getDescription()</code> methods.
   */
  public static Suggestion mapSuggestion(Object from) {
    Class<?> clazz = from.getClass();
    Suggestion suggestion = new Suggestion();

    try {
      Method getId = clazz.getMethod("getId");
      Method getDescription = clazz.getMethod("getDescription");

      Object idValue = getId.invoke(from);
      Object descriptionValue = getDescription.invoke(from);

      suggestion.setId(idValue);
      suggestion.setDescription((String) descriptionValue);

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      log.error(e.getMessage());
    }

    return suggestion;
  }

  public static Suggestion mapSuggestion(Object id, String description) {
    return new Suggestion(id, description);
  }

  public static boolean hasText(String text) {
    return text != null && !text.isBlank();
  }

  private Tools() {}

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

  public static boolean isActive(@NonNull LocalDateTime startDate, LocalDateTime endDate) {
    var time = getColTime();
    if (startDate.isAfter(time)) return false;
    if (endDate == null) return true;
    return !endDate.isBefore(time);
  }
}
