package ca.michalwozniak.jiraflow.model.Issue;

/**
 * Created by Michal Wozniak on 7/10/2016.
 */
public class StatusCategory {
    private String self;
    private int id;
    private String key;
    private String colorName;
    private String name;

    public String getSelf() {
        return self;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getColorName() {
        return colorName;
    }

    public String getName() {
        return name;
    }
}
