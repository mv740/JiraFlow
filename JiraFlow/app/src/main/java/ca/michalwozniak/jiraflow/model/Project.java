package ca.michalwozniak.jiraflow.model;

import android.graphics.drawable.Drawable;

import java.util.List;

import ca.michalwozniak.jiraflow.model.Issue.issueType;

/**
 * Created by Michal Wozniak on 4/17/2016.
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

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Avatar getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(Avatar avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public Category getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(Category projectCategory) {
        this.projectCategory = projectCategory;
    }

    public List<issueType> getIssuetypes() {
        return issuetypes;
    }

    public void setIssuetypes(List<issueType> issuetypes) {
        this.issuetypes = issuetypes;
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
}
