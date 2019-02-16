package com.wordpress.louieefitness.geld.Models;

public class User {
    private String username, email;
    private String question, answer, password, referer;
    private static User the_user;
    private Boolean paid = false;
    private Double Balance = 0.0;
    private String level = "Newbie";
    public static final String ref = "User";

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        this.Balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public String getLevel() {
        return level;
    }

    public String getEmail() {
        return email;
    }

}
