package ca.michalwozniak.jiraflow.MVP.Login;

import ca.michalwozniak.jiraflow.MVP.View;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public interface LoginView extends View {

    void loginFailed(String errorMessage);
    void showProgressIndicator();
    void navigateToDashboard();

}
