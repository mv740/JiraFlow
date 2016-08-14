package ca.michalwozniak.jiraflow.model.other;

import java.util.List;

/**
 * Created by Michal Wozniak on 8/6/2016.
 */
public class ColumnConfig {

    private List<Column> columns;
    private String constraintType;

    public List<Column> getColumns() {
        return columns;
    }

    public String getConstraintType() {
        return constraintType;
    }
}
