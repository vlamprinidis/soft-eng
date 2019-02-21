package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;

public class User {
	
	private final String username;
	private final String password;
	private final String name;
	private final String email;
	private final boolean admin;
	private final String token;

	public User(String username, String password, String name, String email,boolean admin, String token) {
		this.username = username;
		this.password = password;
		this.name     = name;
		this.email     = email;
		this.admin    = admin;
		this.token    = token;
	}


	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public boolean isAdmin(){
		return admin;
	}

	public String getToken(){
		return token;
	}

}
