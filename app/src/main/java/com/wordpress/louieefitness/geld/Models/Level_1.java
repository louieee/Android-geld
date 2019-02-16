package com.wordpress.louieefitness.geld.Models;

public class Level_1 {
    private String username;
    private static Level_1 level1_user;
    private int no_received = 0;
    final public static String ref = "Level_1";
    final public static String name = "Beginner";
    private Boolean Reached_limit = getLimit();
    final public static String color = "#ff0404";

    public Level_1() {

    }

    public Level_1(String username, int no_received) {
        this.username = username;
        this.no_received = no_received;
    }

    public Boolean getReached_limit() {
        return Reached_limit;
    }

    private void setReached_limit() {
        Reached_limit = true;
    }

    public String getUsername() {
        return username;
    }

    public int getNo_received() {
        return no_received;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo_received(int no_received) {
        this.no_received = no_received;
    }


    private Boolean getLimit() {
        Boolean result = false;
        if (no_received < 2) {
            result = false;
        } else if (no_received == 2) {
            result = true;
        }
        return result;
    }



}
