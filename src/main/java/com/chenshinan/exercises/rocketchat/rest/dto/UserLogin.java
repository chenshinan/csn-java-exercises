package com.chenshinan.exercises.rocketchat.rest.dto;

/**
 * @author shinan.chen
 * @since 2019/7/31
 */
public class UserLogin {
    private String user;
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
