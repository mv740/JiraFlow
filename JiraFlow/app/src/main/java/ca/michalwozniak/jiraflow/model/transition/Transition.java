package ca.michalwozniak.jiraflow.model.transition;

import ca.michalwozniak.jiraflow.model.Issue.Status;

/**
 * Created by Michal Wozniak on 9/5/2016.
 */

public class Transition{

    private String id;
    private String name;
    private Status to;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getTo() {
        return to;
    }
}