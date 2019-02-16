package com.wordpress.louieefitness.geld.Models;

public class Level_5 {
    final public static String ref = "Level_5";
    final public static String name = "Master";
    final public static String color = "#ff6f00";
    private static Level_5 level5_user;
    private String username;
    private int no_received = (0);
    private Boolean Reached_limit = getLimit();

    public Level_5() {

    }

    public Level_5(String username) {
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

    private void setNo_received(int no_received) {
        this.no_received = no_received;
    }

    private Boolean getLimit() {
        Boolean result = false;
        if ((no_received) < 32) {
            result = false;
        } else if ((no_received) == 32) {
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
