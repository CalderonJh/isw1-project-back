package fpc.app.service.auth;

public interface UserRoleService {
  void giveRole(Long userId, Long roleId, Long clubId);

  void revokeRole(Long userId, Long roleId);
}
