package com.techelevator.tenmo.models;

public class AccountUser {
	private long user_id;
	private String username;
	private String password;

	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "UserID: " + getUser_id() + " Username: " + getUsername();
	}
	

}