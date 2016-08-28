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

        void saveUser(String username, String password, User user, String url);

        void rememberProfile();

        void onTimeout();

        void connectionError(String s);
    }

    void login(String username, String password, String url, boolean isChecked, OnLoginFinishedListener listener);
}
