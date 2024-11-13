package com.youngtfy.server.model;

// id, pw, name, email

public class User {
    private String id;
    private String username;
    private int password;
    private String email;

    public User(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password.hashCode(); // 해시처리하여 정수로 변경
        this.email = email;
    }

    public User(String id, String username, int password, String email) {
        this.id = id;
        this.username = username;
        this.password = password; // 해시처리하여 정수로 변경
        this.email = email;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getPassword() { return password; }
    public void setPassword(String password) { this.password = password.hashCode(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}