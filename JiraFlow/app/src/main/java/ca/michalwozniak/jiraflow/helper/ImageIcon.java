package ca.michalwozniak.jiraflow.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.List;

import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.utility.ResourceManager;

/**
 * Created by michal on 4/30/2016.
 *
 * Used to pass loading process to downloadManager as a callback function
 */
public class ImageIcon {
    private String destinationName;
    private Context context;
    private Card.Builder cardB;
    private MaterialListView mListView;
    private List<Card> cards;
    private ImageType imageType;
    private boolean firstCard;
    private CircleProgressBar circleProgressBar;

    public ImageIcon(String destinationName, Context context, Card.Builder cardBuilder, MaterialListView mListView, List<Card> cards, ImageType imageType, boolean firstCard, CircleProgressBar circleProgressBar) {
        this.destinationName = destinationName;
        this.context = context;
        this.cardB = cardBuilder;
        this.mListView = mListView;
        this.cards = cards;
        this.imageType = imageType;
        this.firstCard = firstCard;
        this.circleProgressBar = circleProgressBar;

    }


    public void draw() {
        Drawable drawable = null;
        if (imageType == ImageType.SVG) {
            drawable = ResourceManager.getDrawableFromSVG(destinationName, context);
        }else if(imageType == ImageType.PNG)
        {
            //// TODO: 4/24/2016
        }


        final Drawable finalDrawable = drawable;

        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Card card = cardB.build().getProvider().setDrawable(finalDrawable).endConfig().build();
                mListView.getAdapter().add(card);
                cards.add(card);

                if (firstCard) {
                    circleProgressBar.setVisibility(View.GONE);
                    firstCard = false;
                }
            }
        });
    }
}
