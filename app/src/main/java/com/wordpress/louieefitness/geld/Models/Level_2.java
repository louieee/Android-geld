package com.wordpress.louieefitness.geld.Models;

public class Level_2 {
    final public static String ref = "Level_2";
    final public static String name = "Amateur";
    final public static String color = "#7103f7";
    private static Level_2 level2_user;
    private String username;
    private int no_received = 0;
    private Boolean Reached_limit = getLimit();

    public Level_2() {

    }

    public Level_2(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNo_received() {
        return no_received;
    }

    public void setNo_received(int no_received) {
        this.no_received = no_received;
    }

    private Boolean getLimit() {
        Boolean result = false;
        if (no_received < 4) {
            result = false;
        } else if (no_received == 4) {
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
