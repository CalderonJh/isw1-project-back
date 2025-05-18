package fpc.app.dto.util;

import java.time.LocalDateTime;

public record DateRange(LocalDateTime start, LocalDateTime end) {
  public void validate() {
    if (!end.isAfter(start))
      throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
  }
}
