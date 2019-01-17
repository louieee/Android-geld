package com.wordpress.louieefitness.geld.Models;

public class User {
    private String username, first_name, last_name, level, wallet_address, email;

    public User(){

    }

    public User(String username, String first_name, String last_name, String level, String wallet_address, String email) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.level = level;
        this.wallet_address = wallet_address;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getLevel() {
        return level;
    }

    public String getWallet_address() {
        return wallet_address;
    }

    public String getEmail() {
        return email;
    }
}
