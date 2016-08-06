package ca.michalwozniak.jiraflow.model;

import ca.michalwozniak.jiraflow.model.other.ColumnConfig;
import ca.michalwozniak.jiraflow.model.other.Estimation;
import ca.michalwozniak.jiraflow.model.other.Filter;
import ca.michalwozniak.jiraflow.model.other.Ranking;

/**
 * Created by Michal Wozniak on 8/6/2016.
 */
public class BoardConfiguration {

    private int id;
    private String name;
    private String self;
    private Filter filter;
    private ColumnConfig columnConfig;
    private Estimation estimation;
    private Ranking ranking;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSelf() {
        return self;
    }

    public Filter getFilter() {
        return filter;
    }

    public ColumnConfig getColumnConfig() {
        return columnConfig;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public Ranking getRanking() {
        return ranking;
    }
}
