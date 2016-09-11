package ca.michalwozniak.jiraflow.features.login;

import android.content.Context;

import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import ca.michalwozniak.jiraflow.utility.SessionManager;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {


    private LoginView loginView;
    private LoginInteractor loginInteractor;


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        NetworkManager networkManager = NetworkManager.getInstance(loginView.getViewContext());
        this.loginInteractor = new LoginInteractorImpl(networkManager);
    }

    @Override
    public void validateCredentials(String username, String password, String url, boolean rememberMe) {
        if (loginView != null) {
            loginView.showProgressIndicator();
        }

        loginInteractor.login(username, password,url ,rememberMe, this);
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
    public void saveUser(String username, User user, String url) {

        SessionManager pm = SessionManager.getInstance((Context) this.loginView);
        pm.saveUsername(user.getName());
        pm.savePassword(password);
        pm.saveEmail(user.getEmailAddress());
        pm.saveProfileIconUrl(user.getAvatarUrls().getBig());
        pm.saveServerUrl(url);
    }

    @Override
    public void rememberProfile() {
        SessionManager pm = SessionManager.getInstance((Context) this.loginView);
        pm.rememberProfile();
    }

    @Override
    public void onTimeout() {
        if(loginView !=null)
            loginView.timeout();
    }

    @Override
    public void connectionError(String errorMsg) {
        if(loginView != null)
            loginView.connectionError(errorMsg);
    }

}
