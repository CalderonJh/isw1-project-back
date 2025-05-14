package fpc.app.dto.response;

import lombok.*;

@Builder
public record ClubResponseDTO(Long id, String name, String shortName, String imageId) {}
