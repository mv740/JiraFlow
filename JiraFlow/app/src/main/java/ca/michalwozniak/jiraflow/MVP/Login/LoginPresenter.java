package ca.michalwozniak.jiraflow.MVP.Login;

/**
 * Created by Michal Wozniak on 8/8/2016.
 */
public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void detachView();
}
