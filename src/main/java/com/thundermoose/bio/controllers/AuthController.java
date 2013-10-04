package com.thundermoose.bio.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thundermoose.bio.auth.AuthInfo;
import com.thundermoose.bio.auth.User;
import com.thundermoose.bio.managers.AuthManager;

@Controller
@RequestMapping("/auth")
public class AuthController {
	private static final Logger logger = Logger.getLogger(AuthController.class);

	@Autowired
	private AuthManager manager;

	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public @ResponseBody
	void createUser(User user) {
		manager.createUser(user);
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody
	void updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
		manager.updatePassword(oldPassword, newPassword);
	}

	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public @ResponseBody
	AuthInfo getUserInfo() {
		return manager.getCurrentUserInfo();
	}

	@RequestMapping(value = "/adminUi", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView adminUi(User user) {
		return new ModelAndView("admin");
	}

	@RequestMapping(value = "/profileUi", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView profileUi(User user) {
		return new ModelAndView("profile");
	}
	
	@RequestMapping(value = "/createUi", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView createUi(User user) {
		return new ModelAndView("createAccount");
	}

	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public ModelAndView getDeniedPage() {
		logger.debug("Received request to show denied page");
		return new ModelAndView("denied");
	}
}
