package gr.ntua.ece.softeng18b.data.model;

import java.util.Objects;

public class Volunt {
	
	private final String username;
	private final String password;
	private final String name;
	private final long contact;
	private final String city;

	public Volunt(String username, String password, String name, long contact, String city) {
		this.username = username;
		this.password = password;
		this.name     = name;
		this.contact  = contact;
		this.city     = city;
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

	public long getContact() {
		return contact;
	}

	public String getCity() {
		return city;
	}

}
