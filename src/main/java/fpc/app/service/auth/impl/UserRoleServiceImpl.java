package fpc.app.service.auth.impl;

import fpc.app.constant.RoleType;
import fpc.app.dto.response.UserRoleDTO;
import fpc.app.dto.util.Reference;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.Club;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import fpc.app.repository.auth.RoleRepository;
import fpc.app.repository.auth.UserRepository;
import fpc.app.service.app.ClubService;
import fpc.app.service.auth.UserRoleService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ClubService clubService;

  @Override
  public void giveRole(Long userId, Long roleId, Long clubId) {
    User user = getUser(userId);
    Role role = getRole(roleId);
    if (RoleType.CLUB_ADMIN.isSame(role)) {
      if (clubId == null)
        throw new ValidationException("Debe indicar el club para asignar este rol");
      Club club = clubService.getClubById(clubId);
      giveClubAdminRole(user, club);
      return;
    }
    if (user.getRoles().add(role)) userRepository.save(user);
    else throw new ValidationException("User already has this role");
  }

  private void giveClubAdminRole(User user, Club club) {
    Role clubAdminRole = getRole(RoleType.CLUB_ADMIN);

    // verify if the user is already club admin
    try {
      Club managed = clubService.getClubByAdminId(user.getId());
      if (managed.getId().equals(club.getId())) return;
      throw new ValidationException("Este usuario ya es administrador de otro club");
    } catch (DataNotFoundException e) {
      /* ignore */
    }

    // verify if the club already has an admin
    User admin = club.getAdmin();
    if (admin != null) {
      if (admin.getId().equals(user.getId())) return;
      else throw new ValidationException("Este club ya tiene administrador asignado");
    }
    club.setAdmin(user);
    user.getRoles().add(clubAdminRole);
    userRepository.save(user);
  }

  private void revokeClubAdminRole(Long userId) {
    User user = getUser(userId);
    Club club = clubService.getClubByAdminId(userId);
    club.setAdmin(null);
    clubService.save(club);
    user.getRoles().remove(getRole(RoleType.CLUB_ADMIN));
    userRepository.save(user);
  }

  @Override
  public void revokeRole(Long userId, Long roleId) {
    User user = getUser(userId);
    Role role = getRole(roleId);
    if (RoleType.CLUB_ADMIN.isSame(role)) {
      revokeClubAdminRole(userId);
      return;
    }

    if (user.getRoles().remove(role)) userRepository.save(user);
    else throw new ValidationException("User does not have this role");
  }

  private Role getRole(Long roleId) {
    return roleRepository
        .findById(roleId)
        .orElseThrow(() -> new DataNotFoundException("Role not found with given id"));
  }

  private Role getRole(RoleType role) {
    return roleRepository
        .findByNameIgnoreCase(role.toString())
        .orElseThrow(() -> new DataNotFoundException("Role not found with given name"));
  }

  private User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new DataNotFoundException("User not found with given id"));
  }

  @Override
  public UserRoleDTO getUserRoles(Long userId) {
    User user = getUser(userId);
    Set<Role> roles = user.getRoles();
    Reference managedClub = null;
    try {
      Club club = clubService.getClubByAdminId(userId);
      managedClub = club.getReference();
    } catch (DataNotFoundException e) {
      // ignore
    }

    return UserRoleDTO.builder()
        .userId(userId)
        .username(user.getUsername())
        .extraRoles(roles.stream().map(Role::getReference).toList())
        .managedClub(managedClub)
        .build();
  }
}
