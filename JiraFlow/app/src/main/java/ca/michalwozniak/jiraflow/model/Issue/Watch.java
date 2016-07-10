package ca.michalwozniak.jiraflow.model.Issue;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Michal Wozniak on 7/10/2016.
 */
public class Watch {

    private String self;
    private int watchCount;
    @JsonProperty("isWatching")
    private Boolean isWatching;

    public String getSelf() {
        return self;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public Boolean isWatching() {
        return isWatching;
    }
}
