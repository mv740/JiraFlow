package ca.michalwozniak.jiraflow.model;

/**
 * Created by michal on 4/17/2016.
 */
public class Board {

    //https://docs.atlassian.com/jira-software/REST/cloud/#agile/1.0/board-getBoard
    private int id;
    private String self;
    private String name;
    private String type;

    public int getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
