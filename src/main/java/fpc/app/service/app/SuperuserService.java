package fpc.app.service.app;

public interface SuperuserService {
  void giveRole(Long userId, Long roleId, Long clubId);

  void revokeRole(Long userId, Long roleId);
}
