package com.example.lalal.Po;

public class User {

	private int userid;
	private String username;
	private String userpwd;
	private String userimg;
	
	public User() {
		super();
	}

	public User(int userid, String username, String userpwd, String userimg) {
		super();
		this.userid = userid;
		this.username = username;
		this.userpwd = userpwd;
		this.userimg = userimg;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}
	
	
}
