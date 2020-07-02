package com.linkin.utils;

public enum RoleEnum {
	ADMIN(1, "ADMIN"), STAFF(2, "STAFF");
	private int id;
	private String roleName;

	RoleEnum(int id, String roleName) {
		this.id = id;
		this.roleName = roleName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
