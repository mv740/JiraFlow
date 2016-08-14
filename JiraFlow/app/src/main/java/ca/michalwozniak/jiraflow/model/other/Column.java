package ca.michalwozniak.jiraflow.model.other;

import java.util.List;

/**
 * Created by Michal Wozniak on 8/6/2016.
 */
public class Column {

    private String name;
    private List<Status> statuses;

    public String getName() {
        return name;
    }

    public List<Status> getStatuses() {
        return statuses;
    }
}
