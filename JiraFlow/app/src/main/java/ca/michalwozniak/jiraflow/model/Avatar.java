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

    public void setExtraSmall(String extraSmall) {
        this.extraSmall = extraSmall;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void setBig(String big) {
        this.big = big;
    }
}
