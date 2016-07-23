package ca.michalwozniak.jiraflow.MVP.Login;

import android.util.Log;

import ca.michalwozniak.jiraflow.MVP.IPresenter;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.LoginService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.PreferenceManager;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Michal Wozniak on 7/23/2016.
 */
public class LoginPresenter implements IPresenter<ILoginView> {

    private ILoginView loginView;
    private Subscription subscription;

    public LoginPresenter() {
    }


    @Override
    public void attachView(ILoginView view) {
        this.loginView = view;
    }

    @Override
    public void detachView() {
        this.loginView = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void authenticateProcess(final String usernameEntered, final String passwordEntered)
    {
        final String username = usernameEntered.trim();
        final String password = passwordEntered.trim();
        if (username.isEmpty() || password.isEmpty()) return;

        loginView.showProgressIndicator();
        if (subscription != null) subscription.unsubscribe();



        final LoginService loginService = ServiceGenerator.createService(LoginService.class, username, password);
        loginService.basicLogin()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        loginView.navigateToDasboard();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("mainActivty", e.getMessage());
                        if (e.getMessage().contains("403")) {
                            Log.e("mainActivty", "Authentication fail : wrong password");
                            //_password.setError("Wrong password");
                            loginView.loginFailed("Wrong password");
                            //Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
                        }
                        if(e.getMessage().contains("401"))
                        {
                            Log.e("mainActivty", "Authentication Failed");
                            loginView.loginFailed("Authentication Failed");
                            //Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onNext(User user) {

                        saveUser(username,password,user);
                    }
                });

    }

    public void saveUser(String username, String password, User user)
    {
        PreferenceManager pm = PreferenceManager.getInstance(loginView.getContext());
        pm.saveUsername(username);
        pm.savePassword(password);
        pm.saveEmail(user.getEmailAddress());
        pm.saveProfileIconUrl(user.getAvatarUrls().getBig());
    }
}
