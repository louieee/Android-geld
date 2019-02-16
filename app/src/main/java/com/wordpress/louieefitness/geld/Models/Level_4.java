package com.wordpress.louieefitness.geld.Models;

public class Level_4 {
    private String username;
    private static Level_4 level4_user;
    private int no_received = 0;
    final public static String ref = "Level_4";
    final public static String name = "Professional";
    private Boolean Reached_limit = getLimit();
    final public static String color = "#02ff15";

    public Level_4() {

    }

    public Level_4(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNo_received(int no_received) {
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
        if ((no_received) < 16) {
            result = false;
        } else if ((no_received) == 16) {
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
