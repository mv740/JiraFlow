package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by micha on 6/11/2016.
 */
@Element(name = "author")
public class Author {
    @Element
    private String name;
    @Element
    private String email;
    @Element
    private String uri;
    @ElementList(inline = true)
    private List<Link> link;
    @Element
    private String username;
    @Element(name = "object-type")
    private String objectType;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUri() {
        return uri;
    }

    public List<Link> getLink() {
        return link;
    }

    public String getUsername() {
        return username;
    }

    public String getObjectType() {
        return objectType;
    }
}
