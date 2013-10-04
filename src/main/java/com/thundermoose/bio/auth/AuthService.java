package com.thundermoose.bio.auth;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.thundermoose.bio.managers.AuthManager;

public class AuthService implements UserDetailsService {

	private static final Logger logger = Logger.getLogger(AuthService.class);

	@Autowired
	private AuthManager manager;

	@Override
	public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
		logger.debug("Getting details for " + user);
		try {
			return manager.getUser(user);
		} catch (Exception e) {
			logger.warn(user+" not found");
			throw new UsernameNotFoundException("Could not find user " + user);
		}
	}

}
