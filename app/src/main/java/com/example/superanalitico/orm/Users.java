package com.example.superanalitico.orm;

public class Users {

    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String ANALYTIC = "analytic";

    public String last_names;
    public String first_names;
    public String user_type;
    public String email;

    public Users() {
        this.last_names = "";
        this.first_names = "";
        this.user_type = "user";
        this.email = "";
    }

    public Users(String lastNames, String firstNames, String user_type, String email) {
        this.last_names = lastNames;
        this.first_names = firstNames;
        this.user_type = user_type;
        this.email = email;
    }

    public String getLastNames() {
        return last_names;
    }

    public void setLastNames(String lastNames) {
        this.last_names = lastNames;
    }

    public String getFirstNames() {
        return first_names;
    }

    public void setFirstNames(String firstNames) {
        this.first_names = firstNames;
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
