package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Attribute;

/**
 * Created by Michal Wozniak on 7/11/2016.
 */
public class Generator {
    @Attribute(name = "uri")
    private String uri;

    public String getUri() {
        return uri;
    }
}
