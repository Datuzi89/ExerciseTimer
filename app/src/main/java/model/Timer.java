package model;

import java.io.Serializable;

/**
 * Created by Xuezhu on 12/21/16.
 */

public class Timer implements Serializable{
    private int timerId;
    private int seconds;
    private int rounds;
    private int rest;
    private int set;

    public Timer(int timerId, int seconds, int rounds, int rest, int set) {
        this.timerId = timerId;
        this.seconds = seconds;
        this.rounds = rounds;
        this.rest = rest;
        this.set = set;
    }

    public Timer() {
    }

    public int getTimerId() {
        return timerId;
    }

    public void setTimerId(int timerId) {
        this.timerId = timerId;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
