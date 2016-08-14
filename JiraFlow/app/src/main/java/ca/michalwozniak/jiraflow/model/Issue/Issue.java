package ca.michalwozniak.jiraflow.model.Issue;

/**
 * Created by michal on 7/9/2016.
 */
public class Issue {

    private String expand;
    private String id;
    private String self;
    private String key;
    private Field fields;

    public String getExpand() {
        return expand;
    }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getKey() {
        return key;
    }

    public Field getFields() {
        return fields;
    }
}
