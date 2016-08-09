package ca.michalwozniak.jiraflow.MVP.Login;

import android.content.Context;

import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.utility.PreferenceManager;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {


    private LoginView loginView;
    private LoginInteractor loginInteractor;


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }

    public boolean validateInput(String username, String password) {

        if ((username.isEmpty() || password.isEmpty())) {
            loginView.inputEmpty();
            return false;
        } else if (username.isEmpty()) {
            loginView.usernameEmpty();
            return false;
        } else if (password.isEmpty()) {
            loginView.passwordEmpty();
            return false;
        } else return true;
    }

    @Override
    public void validateCredentials(String username, String password) {
        if (loginView != null) {
            loginView.showProgressIndicator();
        }

        loginInteractor.login(username, password, this);
    }

    @Override
    public void detachView() {
        loginView = null;
    }

    @Override
    public void onAuthenticationError() {
        if (loginView != null) {
            loginView.loginFailed("Authentication Error");
        }
    }

    @Override
    public void onPasswordError() {
        if (loginView != null) {
            loginView.loginFailed("Password Errors");
        }
    }

    @Override
    public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToDashboard();
        }
    }

    @Override
    public void saveUser(String username, String password, User user) {

        PreferenceManager pm = PreferenceManager.getInstance((Context) this.loginView);
        pm.saveUsername(username);
        pm.savePassword(password);
        pm.saveEmail(user.getEmailAddress());
        pm.saveProfileIconUrl(user.getAvatarUrls().getBig());
    }

}
