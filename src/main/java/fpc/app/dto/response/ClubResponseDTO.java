package fpc.app.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import fpc.app.dto.util.Reference;
import lombok.*;

@Builder
public record ClubResponseDTO(
    Long id,
    String name,
    String shortName,
    String imageId,
    @JsonInclude(NON_NULL) Reference admin) {}
