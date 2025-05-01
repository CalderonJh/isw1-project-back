package fpc.app.util;

import fpc.app.dto.util.Suggestion;
import fpc.app.exception.DataNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

  public static boolean equalsText(@NonNull String text1, @NonNull String text2) {
    return removeExtraSpaces(text1).equalsIgnoreCase(removeExtraSpaces(text2));
  }

  public static <E> E requiredEntity(E entity) {
    if (entity == null) {
      throw new DataNotFoundException("Entity not found");
    }
    return entity;
  }

  public static <E> E requiredEntity(E entity, String message) {
    if (entity == null) {
      throw new DataNotFoundException(message);
    }
    return entity;
  }
}
