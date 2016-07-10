package ca.michalwozniak.jiraflow.model.Issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.model.User;

/**
 * Created by Michal Wozniak on 7/9/2016.
 */
@JsonIgnoreProperties({"customfield_10000","customfield_10001","customfield_10002","customfield_10006"})
public class Field {

    private issueType issuetype;
    private List<Component> components;
    private String timespent;
    private String timeoriginalestimate;
    private String description;
    private Project project;
    private List<FixVersion> fixVersions;
    private int aggregatetimespent;
    private String resolution;
    private String customfield_10006;
    private int aggregatetimeestimate;
    private String resolutiondate;
    private int workratio;
    private String summary;
    private String lastViewed;
    private Watch watches;
    private User creator;
    private List<Subtask> subtasks;
    private String created;
    private User reporter;
    private String customfield_10000;
    private Progress aggregateprogress;
    private Priority priority;
    private String customfield_10001;
    private String customfield_10002;
    private List<Label> labels;
    private String environment;
    private int timeestimate;
    private int aggregatetimeoriginalestimate;
    private List<Version> versions;
    private String duedate;
    private Progress progress;
    private List<IssueLink> issuelinks;
    private Votes votes;
    private String assignee;
    private String updated;
    private Status status;

    public issueType getIssuetype() {
        return issuetype;
    }

    public List<Component> getComponents() {
        return components;
    }

    public String getTimespent() {
        return timespent;
    }

    public String getTimeoriginalestimate() {
        return timeoriginalestimate;
    }

    public String getDescription() {
        return description;
    }

    public Project getProject() {
        return project;
    }

    public List<FixVersion> getFixVersions() {
        return fixVersions;
    }

    public int getAggregatetimespent() {
        return aggregatetimespent;
    }

    public String getResolution() {
        return resolution;
    }


    public int getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    public String getResolutiondate() {
        return resolutiondate;
    }

    public int getWorkratio() {
        return workratio;
    }

    public String getSummary() {
        return summary;
    }

    public String getLastViewed() {
        return lastViewed;
    }

    public Watch getWatches() {
        return watches;
    }

    public User getCreator() {
        return creator;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public String getCreated() {
        return created;
    }

    public User getReporter() {
        return reporter;
    }

    public Progress getAggregateprogress() {
        return aggregateprogress;
    }

    public Priority getPriority() {
        return priority;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public String getEnvironment() {
        return environment;
    }

    public int getTimeestimate() {
        return timeestimate;
    }

    public int getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public String getDuedate() {
        return duedate;
    }

    public Progress getProgress() {
        return progress;
    }

    public Votes getVotes() {
        return votes;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getUpdated() {
        return updated;
    }

    public Status getStatus() {
        return status;
    }

    public List<IssueLink> getIssuelinks() {
        return issuelinks;
    }
}
