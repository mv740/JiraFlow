package ca.michalwozniak.jiraflow.service;

import ca.michalwozniak.jiraflow.model.User;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by michal on 4/10/2016.
 */
public interface LoginService {

    @GET("/rest/api/2/myself")
    Observable<User> basicLogin();
}
