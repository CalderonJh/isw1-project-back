package fpc.app.service.auth.impl;

import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import fpc.app.repository.auth.RoleRepository;
import fpc.app.repository.auth.UserRepository;
import fpc.app.service.auth.SuperuserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperuserServiceImpl implements SuperuserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Override
  public void giveRole(Long userId, Long roleId) {
    User user = userRepository.findById(userId).orElseThrow();
    Role role = roleRepository.findById(roleId).orElseThrow();
    if (!user.getRoles().contains(role)) {
      user.getRoles().add(role);
      userRepository.save(user);
    }
  }

  @Override
  public void revokeRole(Long userId, Long roleId) {
    User user = userRepository.findById(userId).orElseThrow();
    Role role = roleRepository.findById(roleId).orElseThrow();
    if (user.getRoles().contains(role)) {
      user.getRoles().remove(role);
      userRepository.save(user);
    }
  }
}
