package com.thundermoose.bio.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthPages {
	@SuppressWarnings("serial")
	public static final Map<String, Boolean> ADMIN_PAGES = Collections.unmodifiableMap(new HashMap<String, Boolean>() {
		{
			put("admin", true);
		}
	});
	@SuppressWarnings("serial")
	public static final Map<String, Boolean> USER_PAGES = Collections.unmodifiableMap(new HashMap<String, Boolean>() {
		{
			put("profile", true);
		}
	});
}
