package com.ranezgmail.shubham16.livestatusupdate.model;

public class Status {

    private String userStatus;
    private String userId;

    public Status() {
    }

    public Status(String userStatus, String userId) {
        this.userStatus = userStatus;
        this.userId = userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
