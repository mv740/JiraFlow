package ca.michalwozniak.jiraflow.model.Issue;

/**
 * Created by Michal Wozniak on 7/10/2016.
 */
public class Status {
    private String self;
    private String description;
    private String iconUrl;
    private String name;
    private String id;
    private StatusCategory statusCategory;

    public String getSelf() {
        return self;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public StatusCategory getStatusCategory() {
        return statusCategory;
    }
}
