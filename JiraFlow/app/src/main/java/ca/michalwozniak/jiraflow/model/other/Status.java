package ca.michalwozniak.jiraflow.model.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Michal Wozniak on 8/6/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

    private String id;
    private String self;
    private String issueType;

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }
}
