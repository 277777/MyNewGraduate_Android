package com.example.lalal.Po;

public class Friend {

    private int friendid;
    private int userid;
    private int user_friend_id;
    private int status;

    public Friend() {
    }

    public Friend(int friendid, int userid, int user_friend_id, int status) {
        this.friendid = friendid;
        this.userid = userid;
        this.user_friend_id = user_friend_id;
        this.status = status;
    }

    public int getFriendid() {
        return friendid;
    }

    public void setFriendid(int friendid) {
        this.friendid = friendid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUser_friend_id() {
        return user_friend_id;
    }

    public void setUser_friend_id(int user_friend_id) {
        this.user_friend_id = user_friend_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
