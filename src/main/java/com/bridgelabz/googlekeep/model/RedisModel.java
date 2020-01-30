package com.bridgelabz.googlekeep.model;

import java.io.Serializable;

public class RedisModel implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String token;
    private String email;
    private String lastLogin;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}