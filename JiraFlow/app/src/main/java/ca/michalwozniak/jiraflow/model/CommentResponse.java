package ca.michalwozniak.jiraflow.model;

import ca.michalwozniak.jiraflow.model.Feed.Author;
import ca.michalwozniak.jiraflow.model.other.Visibility;

/**
 * Created by Michal Wozniak on 9/20/2016.
 */

public class CommentResponse {

    private String self;
    private String id;
    private Author author;
    private String body;
    private Author updateAuthor;
    private String created;
    private String updated;
    private Visibility visibility;

    public String getSelf() {
        return self;
    }

    public String getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public Author getUpdateAuthor() {
        return updateAuthor;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public Visibility getVisibility() {
        return visibility;
    }
}
