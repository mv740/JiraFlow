package ca.michalwozniak.jiraflow.model;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;

/**
 * Created by michal on 4/17/2016.
 */
public class BoardList {

    private int maxResults;
    private int startAt;
    private int total;
    private boolean isLast;
    private ArrayList<Board> values;

    public int getMaxResults() {
        return maxResults;
    }

    public int getStartAt() {
        return startAt;
    }

    public int getTotal() {
        return total;
    }

    @JsonSetter("isLast")
    public void setLast(String last) {

        isLast = Boolean.parseBoolean(last);
    }

    public boolean isLast() {
        return isLast;
    }

    public ArrayList<Board> getValues() {
        return values;
    }
}
