package fpc.app.service.auth;

import fpc.app.dto.response.UserRoleDTO;

public interface UserRoleService {
  void giveRole(Long userId, Long roleId, Long clubId);

  void revokeRole(Long userId, Long roleId);

	UserRoleDTO getUserRoles(Long userId);
}
