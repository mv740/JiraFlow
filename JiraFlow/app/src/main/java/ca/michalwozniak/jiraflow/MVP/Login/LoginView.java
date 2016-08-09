package ca.michalwozniak.jiraflow.MVP.Login;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public interface LoginView  {

    void loginFailed(String errorMessage);
    void showProgressIndicator();
    void navigateToDashboard();

    void inputEmpty();

    void usernameEmpty();

    void passwordEmpty();
}
