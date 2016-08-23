package ca.michalwozniak.jiraflow.model;

import android.graphics.drawable.Drawable;

import java.util.List;

import ca.michalwozniak.jiraflow.model.Issue.issueType;

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
   // @JsonProperty(required = false)
    private Category projectCategory;
   // @JsonProperty(required = false)
    private List<issueType> issuetypes;
    private Drawable avatar;
    private ImageType imageType;

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

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public void setAvatarBigUrls(String avatarUrls) {
        this.avatarUrls.setBig(avatarUrls);
    }

    public List<issueType> getIssuetypes() {
        return issuetypes;
    }
}
