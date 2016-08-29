package ca.michalwozniak.jiraflow.model;

/**
 * Created by Michal Wozniak on 8/28/2016.
 *
 * used for successful post
 */
public class EmptyResponse {

    private String id;
    private String key;
    private String self;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getSelf() {
        return self;
    }
}
