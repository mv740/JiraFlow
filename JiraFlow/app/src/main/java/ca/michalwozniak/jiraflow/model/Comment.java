package ca.michalwozniak.jiraflow.model;

import ca.michalwozniak.jiraflow.model.other.Visibility;

/**
 * Created by Michal Wozniak on 9/20/2016.
 */

public class Comment {

    private String body;
    private Visibility visibility;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
