package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Michal Wozniak on 6/11/2016.
 */
@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom")
})
@Root(name = "feed", strict = false)
public class ActivityFeed {

    @Element
    private String id;
    @Element(required = false)
    private String link;
    @Element
    private String title;
    @Element(name = "timezone-offset")
    private String timezone;

    private String updated;
    @ElementList(inline=true)
    private List<Entry> entry;


    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getUpdated() {
        return updated;
    }

    public List<Entry> getEntry() {
        return entry;
    }
}
