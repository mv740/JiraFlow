package ca.michalwozniak.jiraflow.model.Feed;

import android.graphics.drawable.Drawable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import ca.michalwozniak.jiraflow.model.ImageType;

/**
 * Created by Michal Wozniak on 6/11/2016.
 */
@Root(strict = false)
public class Entry {
    @Element
    private String id;
    @Element
    private String title;
    @Element(required = false)
    private String content;
    @Element
    private Author author;
    @Element
    private String published;
    @Element
    private String updated;
//    @Element(required=false)
//    private CategoryXML category;
    @ElementList(inline = true)
    private List<Link> link;
    @Element(name = "in-reply-to", required = false)
    private String inReplyTo;
    @Element
    private Generator generator;
    @Element
    private String application;
    @ElementList(inline = true, entry = "verb")
    private List<String> verb;
    @Element(required = false)
    private Target target;
    @Element(name = "object")
    private ObjectXML object;
    @Element(name = "timezone-offset")
    private String timezone;
    private ImageType imageType;

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

//    public CategoryXML getCategory() {
//        return category;
//    }

    public List<Link> getLink() {
        return link;
    }

    public Generator getGenerator() {
        return generator;
    }

    public String getApplication() {
        return application;
    }

    public List<String> getVerb() {
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

    public String getContent() {
        return content;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public String getInReplyTo() {
        return inReplyTo;
    }

    public Target getTarget() {
        return target;
    }
}
