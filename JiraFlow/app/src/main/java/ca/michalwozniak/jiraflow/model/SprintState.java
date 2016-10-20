package ca.michalwozniak.jiraflow.model;

/**
 * Created by Michal Wozniak on 9/5/2016.
 */
public class SprintState {

    private int id;
    private String self;
    private String state;
    private String name;
    private String startDate;
    private String endDate;
    private String completeDate;
    private int originBoardId;

    public int getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public int getOriginBoardId() {
        return originBoardId;
    }
}
