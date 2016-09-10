package ca.michalwozniak.jiraflow.features.login;

import android.content.Context;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public interface LoginView  {

    void loginFailed(String errorMessage);
    void showProgressIndicator();
    void navigateToDashboard();
    void timeout();
    void connectionError(String errorMsg);

    Context getViewContext();
}
