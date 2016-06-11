package ca.michalwozniak.jiraflow.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.List;

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
    private MaterialListView mListView;
    private List<Card> cards;
    private ImageType imageType;
    private boolean firstCard;
    private CircleProgressBar circleProgressBar;

    public ImageIcon(String destinationName, Context context, Project project, List<Card> cards, ImageType imageType, boolean firstCard, CircleProgressBar circleProgressBar) {
        this.destinationName = destinationName;
        this.context = context;
        this.project = project;
        this.cards = cards;
        this.imageType = imageType;
        this.firstCard = firstCard;
        this.circleProgressBar = circleProgressBar;

    }


    public void draw() {
        Drawable drawable = null;
        if (imageType == ImageType.SVG) {
            drawable = ResourceManager.getDrawableFromSVG(destinationName, context);
            project.setAvatar(drawable);
        }else if(imageType == ImageType.PNG)
        {
            //// TODO: 4/24/2016
        }


//        final Drawable finalDrawable = drawable;
//
//        Activity activity = (Activity) context;
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                project.setAvatar(finalDrawable);
//                mListView.getAdapter().add(card);
//                cards.add(card);
//
//                if (firstCard) {
//                    circleProgressBar.setVisibility(View.GONE);
//                    firstCard = false;
//                }
//            }
//        });
    }
}
