package com.shmtu.fabric_server.Pojo;

public class User {
    private String uId;
    private String userName;
    private String pwd;

    public User() {
    }

    public User(String uId, String userName, String pwd) {
        this.uId = uId;
        this.userName = userName;
        this.pwd = pwd;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}