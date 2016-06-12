package ca.michalwozniak.jiraflow.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import ca.michalwozniak.jiraflow.model.Feed.Entry;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.utility.ResourceManager;

/**
 * Created by michal on 4/30/2016.
 *
 * Used to pass loading process to downloadManager as a callback function
 */
public class ImageIcon {
    private String destinationName;
    private Context context;
    private Project project;
    private Entry entry;
    private ImageType imageType;

    public ImageIcon(String destinationName, Context context, Project project, ImageType imageType) {
        this.destinationName = destinationName;
        this.context = context;
        this.project = project;
        this.imageType = imageType;
    }

    public ImageIcon(String destinationName, Activity myActivity, Entry entry, ImageType imageType) {
        this.destinationName = destinationName;
        this.context = myActivity;
        this.entry = entry;
        this.imageType = imageType;
    }


    public void draw() {
        Drawable drawable;
        if (imageType == ImageType.SVG) {
            drawable = ResourceManager.getDrawableFromSVG(destinationName, context);
            entry.setAvatar(drawable);
        }else if(imageType == ImageType.PNG)
        {
            //// TODO: 4/24/2016
        }
    }
}
