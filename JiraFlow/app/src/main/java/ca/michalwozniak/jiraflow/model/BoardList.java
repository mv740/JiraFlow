package ca.michalwozniak.jiraflow.model;

import java.util.ArrayList;

/**
 * Created by michal on 4/17/2016.
 */
public class BoardList {

    private int maxResult;
    private int startAt;
    private int total;
    private boolean isLast;
    private ArrayList<Board> values;

    public int getMaxResult() {
        return maxResult;
    }

    public int getStartAt() {
        return startAt;
    }

    public int getTotal() {
        return total;
    }

    public boolean isLast() {
        return isLast;
    }

    public ArrayList<Board> getValues() {
        return values;
    }
}
