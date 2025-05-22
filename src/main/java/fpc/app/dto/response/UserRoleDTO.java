package fpc.app.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import fpc.app.dto.util.Reference;
import java.util.List;
import lombok.Builder;

@Builder
public record UserRoleDTO(
    Long userId,
    String username,
    @JsonInclude(NON_EMPTY) List<Reference> extraRoles,
    @JsonInclude(NON_NULL) Reference managedClub) {}
