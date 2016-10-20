package ca.michalwozniak.jiraflow.utility;


import android.support.test.espresso.IdlingResource;

/**
 * Created by Michal Wozniak on 9/1/2016.
 *
 * //https://github.com/bookdash/bookdash-android-app/blob/master/app/src/androidTestMock/java/org.bookdash.android/presentation/splash/util/ElapsedTimeIdlingResource.java
 */

public class ElapsedTimeIdlingResource implements IdlingResource {
    private final long startTime;
    private final long waitingTime;
    private ResourceCallback resourceCallback;

    public ElapsedTimeIdlingResource(long waitingTime) {
        this.startTime = System.currentTimeMillis();
        this.waitingTime = waitingTime;
    }

    @Override
    public String getName() {
        return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
    }

    @Override
    public boolean isIdleNow() {
        long elapsed = System.currentTimeMillis() - startTime;
        boolean idle = (elapsed >= waitingTime);
        if (idle) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(
            ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
