package fpc.app.service.app.impl;

import static java.util.Objects.requireNonNull;

import fpc.app.constant.UserRole;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import fpc.app.repository.app.ClubAdminRepository;
import fpc.app.repository.auth.RoleRepository;
import fpc.app.repository.auth.UserRepository;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.SuperuserService;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperuserServiceImpl implements SuperuserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ClubAdminRepository clubAdminRepository;
  private final ClubService clubService;

  @Override
  public void giveRole(Long userId, Long roleId, Long clubId) {
    User user = getUser(userId);
    Role role = getRole(roleId);
    if (UserRole.CLUB_ADMIN.isSame(role)) {
      if (clubId == null) throw new ValidationException("Club id is required for club admin role");
      Club club = requireNonNull(clubService.getClub(clubId), "Club not found");
      giveClubAdminRole(user, club);
      return;
    }
    if (user.getRoles().add(role)) userRepository.save(user);
    else throw new ValidationException("User already has this role");
  }

  private void giveClubAdminRole(User user, Club club) {
    Optional<ClubAdmin> optional = clubAdminRepository.findByUserId(user.getId());
    if (optional.isEmpty()) {
      clubAdminRepository.save(
          ClubAdmin.builder().user(user).club(club).startDate(new Date()).endDate(null).build());
      user.getRoles().add(getRole(UserRole.CLUB_ADMIN));
      userRepository.save(user);
      return;
    }
    if (!optional.get().getClub().getId().equals(club.getId()))
      throw new ValidationException(
          "User already has a club admin role for another club, please revoke it first");
  }

  private void revokeClubAdminRole(Long userId) {
    User user = getUser(userId);
    ClubAdmin clubAdmin =
        clubAdminRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new ValidationException("User is not a club admin"));
    clubAdmin.setEndDate(new Date());
    clubAdminRepository.save(clubAdmin);
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
