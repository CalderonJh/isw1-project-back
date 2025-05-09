package fpc.app.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record StadiumDTO(
    @NotBlank @Size(min = 3, max = 250) String name,
    @Size(min = 1, max = 25) List<StandDTO> stands) {}
