package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Michal Wozniak on 6/11/2016.
 */
@Element(name = "link")
public class Link {
    @Attribute(name = "rel")
    private String rel;
    @Attribute
    private String href;
    @Attribute(required = false)
    private int height;
    @Attribute(required = false)
    private int width;
    @Attribute(required = false)
    private String title;


    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
