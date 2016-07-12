package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Element;

/**
 * Created by Michal Wozniak on 7/11/2016.
 */
public class Target {
    @Element
    private String id;
    @Element
    private String title;
    @Element
    private String summary;
    @Element
    private Link link;
    @Element(name = "object-type")
    private String objectType;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Link getLink() {
        return link;
    }

    public String getObjectType() {
        return objectType;
    }
}
