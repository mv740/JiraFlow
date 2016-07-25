package ca.michalwozniak.jiraflow.MVP.Login;

import android.util.Log;

import ca.michalwozniak.jiraflow.MVP.IPresenter;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
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
        loginView.showProgressIndicator();

        final String username = usernameEntered.trim();
        final String password = passwordEntered.trim();
        if (username.isEmpty() || password.isEmpty()) return;

        if (subscription != null) subscription.unsubscribe();


        final JiraSoftwareService jiraSoftwareService = ServiceGenerator.createService(JiraSoftwareService.class, username, password);
        jiraSoftwareService.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        loginView.navigateToDasboard();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e.getMessage().contains("403")) {
                            loginView.loginFailed("Wrong password");
                        }
                        if(e.getMessage().contains("401"))
                        {
                            loginView.loginFailed("Authentication Failed");
                        }

                    }

                    @Override
                    public void onNext(User user) {

                        saveUser(username,password,user);
                    }
                });

    }

    public boolean validateInput(String username, String password)
    {
        return !(username.isEmpty() || password.isEmpty());
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
