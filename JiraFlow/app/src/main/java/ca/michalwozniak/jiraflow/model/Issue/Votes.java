package ca.michalwozniak.jiraflow.model.Issue;

/**
 * Created by Michal Wozniak on 7/10/2016.
 */
public class Votes {
    private String self;
    private int votes;
    private boolean hasVoted;

    public String getSelf() {
        return self;
    }

    public int getVotes() {
        return votes;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }
}
