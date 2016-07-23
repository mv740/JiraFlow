package ca.michalwozniak.jiraflow.MVP.Login;

import ca.michalwozniak.jiraflow.MVP.IView;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public interface ILoginView extends IView{

    void loginFailed(String errorMessage);
    void showProgressIndicator();
    void navigateToDasboard();

}
