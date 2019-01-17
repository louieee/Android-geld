package com.wordpress.louieefitness.geld.Models;

public class Level_6 {
    private String username, no_received, color;

    public Level_6(){

    }

    public Level_6(String username, String no_received, String color) {
        this.username = username;
        this.no_received = no_received;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public String getNo_received() {
        return no_received;
    }

    public String getColor() {
        return color;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo_received(String no_received) {
        this.no_received = no_received;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
