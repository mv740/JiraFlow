package ca.michalwozniak.jiraflow.MVP.Login;

import android.util.Log;

import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.Error.RetrofitException;
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

                        Log.e("error",e.getMessage());

////                        if (e.getMessage().contains("403")) {
////                            //forbidden request, need necessary permissions
////                            listener.onPasswordError();
////                        }else if(e.getMessage().contains("401"))
////                        {
////                            //not authorized
////                            listener.onAuthenticationError();
////                        }else if (e.getMessage().contains("404"))
////                        {
////                            Log.e("error","HTTP 404 Not Found");
////                        }
////
////                        if(e instanceof SocketTimeoutException)
////                        {
////                            listener.onTimeout();
////                        }
////
////                        if(e.getMessage().contains("No address associated with hostname"))
////                        {
////                            //UnknownHostException
////                            Log.e("error", "hostname does not exist");
////                            listener.connectionError("Your hostname does not exist");
////                        }
////
////                        if(e instanceof SSLPeerUnverifiedException)
////                        {
////                            Log.e("error", "Your hostname is not verified");
////                            listener.connectionError("Your hostname is not verified");
////                        }
//
//                        RetrofitException error = (RetrofitException) e;
//                        Log.e("kindError", error.getKind().name());
//                        Log.e("typeError", error.getType().name());
//                        Log.e("error", e.getMessage());
//                        listener.onTimeout();

                    }

                    @Override
                    public void onNext(User user) {

                        listener.saveUser(username, password, user, url);
                        if (rememberMe) {
                            listener.rememberProfile();
                        }
                    }
                });


    }
}
