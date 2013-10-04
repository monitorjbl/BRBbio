package com.thundermoose.bio.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.thundermoose.bio.managers.AuthManager;

public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private AuthManager manager;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
		resp.setStatus(200);
		new ObjectMapper().writeValue(resp.getWriter(), manager.getCurrentUserInfo());
	}
}
