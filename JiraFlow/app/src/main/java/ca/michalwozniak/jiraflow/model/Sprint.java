package ca.michalwozniak.jiraflow.model;

import java.util.List;

import ca.michalwozniak.jiraflow.model.Issue.Issue;

/**
 * Created by Michal Wozniak on 8/6/2016.
 */
public class Sprint {

    private String expand;
    private int startAt;
    private int maxResults;
    private int total;
    private List<Issue> issues;
    private boolean isLast;
    private List<SprintData> values;

    public String getExpand() {
        return expand;
    }

    public int getStartAt() {
        return startAt;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getTotal() {
        return total;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public boolean isLast() {
        return isLast;
    }

    public List<SprintData> getValues() {
        return values;
    }
}
