package com.wordpress.louieefitness.geld.Models;

public class Wallet {
    private String guid;
    private String address;
    private String email;
    private static Wallet the_wallet;
    public final static String Ref = " Wallet";
    private String Main_Address;
    private String password;
    private Double Balance = 0.0;

    public Wallet() {

    }

    public Wallet(String guid, String address, String email, String main_Address, String password, Double balance) {
        this.guid = guid;
        this.address = address;
        this.email = email;
        Main_Address = main_Address;
        this.password = password;
        Balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMain_Address() {
        return Main_Address;
    }

    public void setMain_Address(String main_Address) {
        Main_Address = main_Address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        Balance = balance;
    }

}

