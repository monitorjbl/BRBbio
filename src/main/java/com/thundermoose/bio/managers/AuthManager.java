package com.thundermoose.bio.managers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.thundermoose.bio.auth.AuthInfo;
import com.thundermoose.bio.auth.AuthPages;
import com.thundermoose.bio.auth.User;
import com.thundermoose.bio.dao.AuthDao;
import com.thundermoose.bio.exceptions.UserNotFoundException;
import com.thundermoose.bio.util.Utils;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {

	private static final Logger logger = Logger.getLogger(AuthManager.class);

	@SuppressWarnings("serial")
	private static final List<String> admins = new ArrayList<String>() {
		{
			add("thundermoose");
		}
	};

	@Autowired
	private AuthDao dao;

	public User getUser(String username) {
		User u = dao.getUser(username);
		u.setAuthorities(getAuthorities(u.getUsername()));
		return u;
	}

	public void createUser(User u) {
		u.setPassword(Utils.md5(u.getPassword()));
		u.setActive(true);
		logger.debug("creating user "+u.getUsername());
		dao.createUser(u);

		// log user in
		Authentication auth = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public void updatePassword(String oldPassword, String newPassword) {
		User user = dao.getUser(Utils.getCurrentUsername());
		oldPassword = Utils.md5(oldPassword);
		newPassword = Utils.md5(newPassword);
		if (user.getPassword().equals(oldPassword)) {
			logger.debug("Updating password for " + user.getUsername());
			user.setPassword(newPassword);
			dao.updateUser(user);
		} else {
			throw new RuntimeException("Password incorrect");
		}
	}

	public AuthInfo getCurrentUserInfo() {
		String username = Utils.getCurrentUsername();
		if ("anonymousUser".equals(username)) {
			throw new UserNotFoundException("User not found");
		}

		User user = getUser(username);
		AuthInfo authInfo = new AuthInfo(user);
		logger.debug("Getting granted authorities");
		for (GrantedAuthority ga : user.getAuthorities()) {
			if ("ROLE_USER".equals(ga.getAuthority())) {
				authInfo.getViewable().putAll(AuthPages.USER_PAGES);
			} else if ("ROLE_ADMIN".equals(ga.getAuthority())) {
				authInfo.getViewable().putAll(AuthPages.ADMIN_PAGES);
			}
		}

		return authInfo;
	}

	private List<GrantedAuthority> getAuthorities(String username) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);

		logger.debug("Grant ROLE_USER to " + username);
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (admins.contains(username)) {
			logger.debug("Grant ROLE_ADMIN to " + username);
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		return authList;
	}
}
