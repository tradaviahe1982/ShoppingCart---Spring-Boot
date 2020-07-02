package com.linkin.model;

import java.util.List;

public class SearchUserDTO extends SearchDTO {

	private static final long serialVersionUID = 1L;
	private Boolean enabled;
	private List<Integer> roleList;

	public SearchUserDTO() {
		super();
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Integer> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Integer> roleList) {
		this.roleList = roleList;
	}

}
