package ca.michalwozniak.jiraflow.helper;

/**
 * Created by Michal Wozniak on 8/7/2016.
 */
public class DragCardData {

    private String key;
    private String summary;
    private String IconType;

    public DragCardData(String key, String summary, String iconType) {
        this.key = key;
        this.summary = summary;
        IconType = iconType;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return summary;
    }

    public String getIconType() {
        return IconType;
    }
}
