package com.linkin.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the persistent_logins database table.
 * 
 */
@Entity
@Table(name="persistent_logins")
public class PersistentLogin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String series;

	@Column(name="last_used")
	private Timestamp lastUsed;

	private String token;

	private String username;

	public PersistentLogin() {
	}

	public String getSeries() {
		return this.series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Timestamp getLastUsed() {
		return this.lastUsed;
	}

	public void setLastUsed(Timestamp lastUsed) {
		this.lastUsed = lastUsed;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}