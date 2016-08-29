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
    private Resolution resolution;
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
    private Assignee assignee;
    private String updated;
    private Status status;

    public issueType getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(issueType issuetype) {
        this.issuetype = issuetype;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public String getTimespent() {
        return timespent;
    }

    public void setTimespent(String timespent) {
        this.timespent = timespent;
    }

    public String getTimeoriginalestimate() {
        return timeoriginalestimate;
    }

    public void setTimeoriginalestimate(String timeoriginalestimate) {
        this.timeoriginalestimate = timeoriginalestimate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<FixVersion> getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(List<FixVersion> fixVersions) {
        this.fixVersions = fixVersions;
    }

    public int getAggregatetimespent() {
        return aggregatetimespent;
    }

    public void setAggregatetimespent(int aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public String getCustomfield_10006() {
        return customfield_10006;
    }

    public void setCustomfield_10006(String customfield_10006) {
        this.customfield_10006 = customfield_10006;
    }

    public int getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    public void setAggregatetimeestimate(int aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    public String getResolutiondate() {
        return resolutiondate;
    }

    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }

    public int getWorkratio() {
        return workratio;
    }

    public void setWorkratio(int workratio) {
        this.workratio = workratio;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(String lastViewed) {
        this.lastViewed = lastViewed;
    }

    public Watch getWatches() {
        return watches;
    }

    public void setWatches(Watch watches) {
        this.watches = watches;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public String getCustomfield_10000() {
        return customfield_10000;
    }

    public void setCustomfield_10000(String customfield_10000) {
        this.customfield_10000 = customfield_10000;
    }

    public Progress getAggregateprogress() {
        return aggregateprogress;
    }

    public void setAggregateprogress(Progress aggregateprogress) {
        this.aggregateprogress = aggregateprogress;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCustomfield_10001() {
        return customfield_10001;
    }

    public void setCustomfield_10001(String customfield_10001) {
        this.customfield_10001 = customfield_10001;
    }

    public String getCustomfield_10002() {
        return customfield_10002;
    }

    public void setCustomfield_10002(String customfield_10002) {
        this.customfield_10002 = customfield_10002;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public int getTimeestimate() {
        return timeestimate;
    }

    public void setTimeestimate(int timeestimate) {
        this.timeestimate = timeestimate;
    }

    public int getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    public void setAggregatetimeoriginalestimate(int aggregatetimeoriginalestimate) {
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public List<IssueLink> getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(List<IssueLink> issuelinks) {
        this.issuelinks = issuelinks;
    }

    public Votes getVotes() {
        return votes;
    }

    public void setVotes(Votes votes) {
        this.votes = votes;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
