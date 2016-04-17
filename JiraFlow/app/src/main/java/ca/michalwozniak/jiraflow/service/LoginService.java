package ca.michalwozniak.jiraflow.service;

import ca.michalwozniak.jiraflow.model.User;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by michal on 4/10/2016.
 */
public interface LoginService {

    @GET("/rest/api/2/myself")
    Call<User> basicLogin();
}
