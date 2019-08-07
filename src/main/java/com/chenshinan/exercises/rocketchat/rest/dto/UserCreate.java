package com.chenshinan.exercises.rocketchat.rest.dto;

/**
 * @author shinan.chen
 * @since 2019/8/2
 */
public class UserCreate {
    private String name;
    private String email;
    private String password;
    private String username;

    public UserCreate() {
    }

    public UserCreate(String name, String email, String password, String username) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
