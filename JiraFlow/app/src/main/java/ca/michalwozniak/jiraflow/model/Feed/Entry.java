package ca.michalwozniak.jiraflow.model.Feed;

import android.graphics.drawable.Drawable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by micha on 6/11/2016.
 */
@Element(name = "entry")
public class Entry {
    @Element
    private String id;
    @Element
    private String title;
    @Element
    private Author author;
    @Element
    private String published;
    @Element
    private String updated;
    @Element(required = false)
    private String category;
    @ElementList(inline = true, required = false)
    private List<Link> link;
    @Element(required = false)
    private String generator;
    @Element(required = false)
    private String application;
    @Element(required = false)
    private String verb;
    @Element
    private ObjectXML object;
    @Element(name = "timezone-offset")
    private String timezone;

    private Drawable avatar;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public String getUpdated() {
        return updated;
    }

    public String getCategory() {
        return category;
    }

    public List<Link> getLink() {
        return link;
    }

    public String getGenerator() {
        return generator;
    }

    public String getApplication() {
        return application;
    }

    public String getVerb() {
        return verb;
    }

    public ObjectXML getObject() {
        return object;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }

    public Drawable getAvatar() {
        return avatar;
    }
}
