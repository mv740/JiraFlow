package ca.michalwozniak.jiraflow.features.login;

import android.util.Log;

import ca.michalwozniak.jiraflow.service.Error.RetrofitException;
import ca.michalwozniak.jiraflow.utility.NetworkUtil;

/**
 * Created by Michal Wozniak on 8/8/2016.
 */
public class LoginInteractorImpl implements LoginInteractor {


    public LoginInteractorImpl() {
    }

    @Override
    public void login(final String username, final String password, final String url, final boolean rememberMe, final OnLoginFinishedListener listener) {

        NetworkUtil.logIn(username, password, url)
                .subscribe(user -> {
                            listener.saveUser(password, user, url);
                            if (rememberMe) {
                                listener.rememberProfile();
                            }
                            listener.onSuccess();
                        }, e -> errorActions(listener, e)
                );


    }


    private void errorActions(OnLoginFinishedListener listener, Throwable e) {
        RetrofitException error = (RetrofitException) e;

        switch (error.getType()) {
            case UNAUTHORIZED:
                listener.onAuthenticationError();
                break;
            case FORBIDDEN:
                listener.onPasswordError();
                break;
            case NO_ASSOCIATED_ADDRESS:
                listener.connectionError("No address associated with hostname");
                break;
            case UNKNOWN_HOST:
                listener.connectionError("Your hostname does not exist");
                break;
            case NOT_FOUND:
                listener.connectionError(RetrofitException.Type.NOT_FOUND.name());
                break;
            case HOSTNAME_NOT_VERIFIED:
            case UNKNOWN:
            case TIMEOUT:
                listener.onTimeout();
                break;
            default:
                listener.connectionError("Network Error, Please try again!");
                break;
        }

        Log.e("error", e.getMessage());
    }

}
