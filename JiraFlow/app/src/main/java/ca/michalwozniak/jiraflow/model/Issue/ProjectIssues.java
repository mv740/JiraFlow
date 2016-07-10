package ca.michalwozniak.jiraflow.model.Issue;

import java.util.List;

/**
 * Created by Michal Wozniak on 7/9/2016.
 */
public class ProjectIssues {

    private String expand;
    private int startAt;
    private int maxResults;
    private int total;
    private List<Issue> issues;

    public String getExpand() {
        return expand;
    }

    public int getStartAt() {
        return startAt;
    }

    public int getTotal() {
        return total;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public List<Issue> getIssues() {
        return issues;
    }
}
