package ca.michalwozniak.jiraflow.model.Issue;

/**
 * Created by Michal Wozniak on 7/10/2016.
 */
public class issueType {

    private String self;
    private String id;
    private String description;
    private String iconUrl;
    private String name;
    private boolean subtask;
    //@JsonProperty(required = false)
    private int avatarId;

    public String getSelf() {
        return self;
    }

    public String getId() {
        return id;
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

    public boolean isSubtask() {
        return subtask;
    }

    public int getAvatarId() {
        return avatarId;
    }
}
