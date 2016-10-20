package ca.michalwozniak.jiraflow.utility;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by Michal Wozniak on 9/10/2016.
 */

public class AnimationUtil {


    public static void stopRefreshAnimation(SwipeRefreshLayout swipeRefreshLayout) {

        // stopping swipe refresh
        if (swipeRefreshLayout != null) {
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        }

    }
}
