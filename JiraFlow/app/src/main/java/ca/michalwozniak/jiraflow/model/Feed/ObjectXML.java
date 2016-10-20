package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Element;

/**
 * Created by Michal Wozniak on 6/11/2016.
 */
public class ObjectXML {
    @Element
    private String id;
    @Element(required = false)
    private String title;
    @Element(required = false)
    private String summary;
    @Element
    private Link link;


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

}
