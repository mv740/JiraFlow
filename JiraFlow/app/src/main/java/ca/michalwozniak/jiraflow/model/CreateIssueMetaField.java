package ca.michalwozniak.jiraflow.model;

import java.util.List;

/**
 * Created by Michal Wozniak on 8/22/2016.
 */
public class CreateIssueMetaField {

    private String expand;
    private List<Project> projects;

    public String getExpand() {
        return expand;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
