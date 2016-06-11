package ca.michalwozniak.jiraflow.model;

import android.graphics.drawable.Drawable;

/**
 * Created by michal on 4/17/2016.
 */
public class Project {

    private String expand;
    private String self;
    private String id;
    private String key;
    private String name;
    private Avatar avatarUrls;
    private String projectTypeKey;
    private Category projectCategory;
    private Drawable avatar;

    public String getExpand() {
        return expand;
    }

    public String getSelf() {
        return self;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Avatar getAvatarUrls() {
        return avatarUrls;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public Category getProjectCategory() {
        return projectCategory;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }
}
