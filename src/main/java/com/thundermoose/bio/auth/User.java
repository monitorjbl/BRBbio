package com.thundermoose.bio.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User implements UserDetails {
	private static final long serialVersionUID = -2230825739778656604L;

	private long id;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private boolean active;
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	public User() {

	}

	public User(long id, String username, String firstName, String lastName, String password, boolean active) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.active = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password + ", active=" + active + "]";
	}

	public User safeClone() {
		User u = new User();
		u.setUsername(username);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setAuthorities(new ArrayList<GrantedAuthority>(authorities));
		return u;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return active;
	}

	@Override
	public boolean isAccountNonLocked() {
		return active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return active;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

}
