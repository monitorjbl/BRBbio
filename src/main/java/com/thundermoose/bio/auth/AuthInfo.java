package com.thundermoose.bio.auth;

import java.util.HashMap;
import java.util.Map;

public class AuthInfo {

	private User user;
	private Map<String, Boolean> viewable = new HashMap<String, Boolean>();

	public AuthInfo(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Boolean> getViewable() {
		return viewable;
	}

	public void setViewable(Map<String, Boolean> viewable) {
		this.viewable = viewable;
	}

}
