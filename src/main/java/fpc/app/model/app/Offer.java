package fpc.app.model.app;

import static fpc.app.util.Tools.getColTime;

import fpc.app.exception.ValidationException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Offer {
  @NotNull
  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @NotNull
  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "paused")
  private boolean isPaused;

  @Nullable
  @Column(name = "image_id")
  private String imageId;

  public void validateForSale() {
    if (isPaused) throw new ValidationException("La oferta está pausada");

    if (!isActive()) throw new ValidationException("La oferta no está vigente");
  }

  public boolean isActive() {
    var now = getColTime();
    if (startDate.isAfter(now)) return false;
    if (endDate == null) return true;
    return !endDate.isBefore(now);
  }

  public void validateDateRange() {
    LocalDateTime now = getColTime();
    if (startDate.isAfter(endDate))
      throw new ValidationException("La fecha de inicio no puede ser posterior a la fecha de fin");
    if (startDate.isBefore(now))
      throw new ValidationException("La fecha de inicio debe ser futura");
  }

  public void validateEndDate() {
    if (endDate.isBefore(getColTime()))
      throw new ValidationException("La fecha de fin no puede ser anterior a la fecha actual");
  }
}
