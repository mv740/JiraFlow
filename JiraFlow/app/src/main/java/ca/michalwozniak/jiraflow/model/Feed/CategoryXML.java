package ca.michalwozniak.jiraflow.model.Feed;

import org.simpleframework.xml.Attribute;

/**
 * Created by Michal Wozniak on 7/11/2016.
 */
public class CategoryXML {
    @Attribute(name = "term")
    private String term;

    public String getTerm() {
        return term;
    }
}
