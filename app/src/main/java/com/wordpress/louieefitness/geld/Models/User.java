package com.wordpress.louieefitness.geld.Models;

public class User {
    private String username, first_name, last_name, wallet_address, email;
    private String question, answer, password, referer;
    private String recent_payout, accumulated_payout, level = "0";
    public  static final String ref = "User";

    public User(){
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setWallet_address(String wallet_address) {
        this.wallet_address = wallet_address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRecent_payout() {
        return recent_payout;
    }

    public void setRecent_payout(String recent_payout) {
        this.recent_payout = recent_payout;
    }

    public String getAccumulated_payout() {
        return accumulated_payout;
    }

    public void setAccumulated_payout(String accumulated_payout) {
        this.accumulated_payout = accumulated_payout;
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
