package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by micha on 6/11/2016.
 */
@Element(name = "feed")
public class ActivityFeed {
    @Element
    private String id;
    @Element(required = false)
    private String link;
    @Element
    private String title;
    @Element(name = "timezone-offset")
    private String timezone;
    @Element
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
