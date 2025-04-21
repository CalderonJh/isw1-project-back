package fpc.app.service.auth;

public interface SuperuserService {
  void giveRole(Long userId, Long roleId);

  void revokeRole(Long userId, Long roleId);
}
