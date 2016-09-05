package ca.michalwozniak.jiraflow.model;

/**
 * Created by michal on 4/10/2016.
 */
public class User {

    private String self;
    private String name;
    private String key;
    private String emailAddress;
    private Avatar avatarUrls;
    private String displayName;
    private boolean active;
    private String timeZone;
    private String locale;
    private Groups groups;
    private Roles applicationRoles;
    private String expand;

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

    public String getLocale() {
        return locale;
    }

    public Groups getGroups() {
        return groups;
    }

    public Roles getApplicationRoles() {
        return applicationRoles;
    }

    public String getExpand() {
        return expand;
    }

    public void setName(String name) {
        this.name = name;
    }
}
