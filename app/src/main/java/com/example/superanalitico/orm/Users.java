package com.example.superanalitico.orm;

public class Users {

    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String ANALYTIC = "analytic";

    private String lastNames;
    private String firstNames;
    private String user_type;
    private String email;

    public Users(String lastNames, String firstNames, String user_type, String email) {
        this.lastNames = lastNames;
        this.firstNames = firstNames;
        this.user_type = user_type;
        this.email = email;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
