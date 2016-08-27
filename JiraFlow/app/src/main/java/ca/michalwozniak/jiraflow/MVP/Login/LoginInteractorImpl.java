package ca.michalwozniak.jiraflow.MVP.Login;

import java.net.SocketTimeoutException;

import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Michal Wozniak on 8/8/2016.
 */
public class LoginInteractorImpl implements LoginInteractor {
    @Override
    public void login(final String username, final String password, final String url, final boolean rememberMe, final OnLoginFinishedListener listener) {

        final JiraSoftwareService jiraSoftwareService = ServiceGenerator.createService(JiraSoftwareService.class, username, password, url);
        jiraSoftwareService.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e.getMessage().contains("403")) {
                            listener.onPasswordError();
                        }else if(e.getMessage().contains("401"))
                        {
                            listener.onAuthenticationError();
                        }

                        if(e instanceof SocketTimeoutException)
                        {
                            listener.onTimeout();
                        }



                    }

                    @Override
                    public void onNext(User user) {

                        listener.saveUser(username,password,user,url);
                        if(rememberMe)
                        {
                            listener.rememberProfile();
                        }
                    }
                });

    }
}
