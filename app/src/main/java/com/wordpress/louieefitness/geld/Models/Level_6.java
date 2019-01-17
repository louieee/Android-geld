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
}
