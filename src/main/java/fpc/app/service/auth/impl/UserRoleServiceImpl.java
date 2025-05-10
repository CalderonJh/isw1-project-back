package fpc.app.service.auth.impl;


import static fpc.app.util.Tools.required;
import static java.util.Objects.isNull;

import fpc.app.constant.UserRole;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import fpc.app.repository.auth.RoleRepository;
import fpc.app.repository.auth.UserRepository;
import fpc.app.service.app.ClubAdminService;
import fpc.app.service.app.ClubService;
import fpc.app.service.auth.UserRoleService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ClubService clubService;
  private final ClubAdminService clubAdminService;

  @Override
  public void giveRole(Long userId, Long roleId, Long clubId) {
    User user = getUser(userId);
    Role role = getRole(roleId);
    if (UserRole.CLUB_ADMIN.isSame(role)) {
      if (clubId == null) throw new ValidationException("Club id is required for club admin role");
      Club club = clubService.getClub(clubId);
      giveClubAdminRole(user, club);
      return;
    }
    if (user.getRoles().add(role)) userRepository.save(user);
    else throw new ValidationException("User already has this role");
  }

  private void giveClubAdminRole(User user, Club club) {
    ClubAdmin admin = clubAdminService.getClubAdmin(user);
    if (isNull(admin)) {
      clubAdminService.save(
          ClubAdmin.builder().user(user).club(club).startDate(new Date()).endDate(null).build());
      user.getRoles().add(getRole(UserRole.CLUB_ADMIN));
      userRepository.save(user);
      return;
    }
    if (!admin.getClub().getId().equals(club.getId()))
      throw new ValidationException(
          "User already has a club admin role for another club, please revoke it first");
  }

  private void revokeClubAdminRole(Long userId) {
    User user = getUser(userId);
    ClubAdmin clubAdmin = clubAdminService.getClubAdmin(user);
    clubAdmin.setEndDate(new Date());
    clubAdminService.save(clubAdmin);
    user.getRoles().remove(getRole(UserRole.USER));
    userRepository.save(user);
  }

  @Override
  public void revokeRole(Long userId, Long roleId) {
    User user = getUser(userId);
    Role role = getRole(roleId);
    if (UserRole.CLUB_ADMIN.isSame(role)) {
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

  private Role getRole(UserRole role) {
    return roleRepository
        .findByNameIgnoreCase(role.toString())
        .orElseThrow(() -> new DataNotFoundException("Role not found with given id"));
  }

  private User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new DataNotFoundException("User not found with given id"));
  }
}
