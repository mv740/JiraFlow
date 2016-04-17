package ca.michalwozniak.jiraflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by michal on 4/16/2016.
 */
public class Avatar {
    @JsonProperty("16x16")
    private String extraSmall;
    @JsonProperty("24x24")
    private String small;
    @JsonProperty("32x32")
    private String medium;
    @JsonProperty("48x48")
    private String big;


    public String getExtraSmall() {
        return extraSmall;
    }

    public String getSmall() {
        return small;
    }

    public String getMedium() {
        return medium;
    }

    public String getBig() {
        return big;
    }
}
