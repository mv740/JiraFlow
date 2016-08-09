package ca.michalwozniak.jiraflow.MVP.Login;

import ca.michalwozniak.jiraflow.model.User;

/**
 * Created by Michal Wozniak on 8/8/2016.
 */
public interface LoginInteractor {

    interface OnLoginFinishedListener{

        void onAuthenticationError();

        void onPasswordError();

        void onSuccess();

        void saveUser(String username, String password, User user);
    }

    void login(String username,String password, OnLoginFinishedListener listener);
}
