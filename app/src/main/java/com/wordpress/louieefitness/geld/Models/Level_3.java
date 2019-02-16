package com.wordpress.louieefitness.geld.Models;

public class Level_3 {
    private String username;
    private static Level_3 level3_user;
    private int no_received = 0;
    final public static String ref = "Level_3";
    final public static String name = "Intermediate";
    private Boolean Reached_limit = getLimit();
    final public static String color = "#fcca01";

    public Level_3() {

    }

    public Level_3(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void setNo_received(int no_received) {
        this.no_received = no_received;
    }


    public String getUsername() {
        return username;
    }

    public int getNo_received() {
        return no_received;
    }

    private Boolean getLimit() {
        Boolean result = false;
        if (no_received < 8) {
            result = false;
        } else if (no_received == 8) {
            result = true;
        }
        return result;
    }

    public Boolean getReached_limit() {
        return Reached_limit;
    }

    private void setReached_limit() {
        Reached_limit = true;
    }

}
