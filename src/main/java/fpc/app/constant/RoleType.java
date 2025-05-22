package fpc.app.constant;

import fpc.app.model.auth.Role;

public enum RoleType {
  USER,
  CLUB_ADMIN,
  SUPERUSER;

	public  boolean isSame(Role role) {
    return role != null && (role.getName().equalsIgnoreCase(this.name()));
	}
}
