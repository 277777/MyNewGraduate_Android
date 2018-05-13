package com.example.lalal.Po;

import java.sql.Timestamp;

public class Message {

	private int msgid;
	private int userid;
	private String msgtext;
	private Timestamp msgtime;
	public Message(int msgid, int userid, String msgtext, Timestamp msgtime) {
		super();
		this.msgid = msgid;
		this.userid = userid;
		this.msgtext = msgtext;
		this.msgtime = msgtime;
	}
	public Message() {
		super();
	}
	public int getMsgid() {
		return msgid;
	}
	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getMsgtext() {
		return msgtext;
	}
	public void setMsgtext(String msgtext) {
		this.msgtext = msgtext;
	}
	public Timestamp getMsgtime() {
		return msgtime;
	}
	public void setMsgtime(Timestamp msgtime) {
		this.msgtime = msgtime;
	}
	
	
	
}
