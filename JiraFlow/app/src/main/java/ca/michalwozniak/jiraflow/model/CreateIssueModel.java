package ca.michalwozniak.jiraflow.model;

import ca.michalwozniak.jiraflow.model.Issue.Field;

/**
 * Created by Michal Wozniak on 8/28/2016.
 */


public class CreateIssueModel {

    private Field fields;

    public Field getFields() {
        return fields;
    }

    public void setFields(Field fields) {
        this.fields = fields;
    }
}
