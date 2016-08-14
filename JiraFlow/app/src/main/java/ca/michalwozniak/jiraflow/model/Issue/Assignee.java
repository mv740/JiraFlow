package ca.michalwozniak.jiraflow.model.Issue;

import ca.michalwozniak.jiraflow.model.Avatar;

/**
 * Created by Michal Wozniak on 7/20/2016.
 */
public class Assignee {

    private String self;
    private String name;
    private String key;
    private String emailAddress;
    private Avatar avatarUrls;
    private String displayName;
    private boolean active;
    private String timeZone;

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Avatar getAvatarUrls() {
        return avatarUrls;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return active;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
