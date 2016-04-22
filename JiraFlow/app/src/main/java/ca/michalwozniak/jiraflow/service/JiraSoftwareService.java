package ca.michalwozniak.jiraflow.service;

import java.util.List;

import ca.michalwozniak.jiraflow.model.Project;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by michal on 4/16/2016.
 *
 * interacting with JIRA agile REST API : https://docs.atlassian.com/jira-software/REST/cloud/
 */
public interface JiraSoftwareService {


    //project query
    @GET("/rest/api/2/project")
    Observable<List<Project>> getAllProjects();

    @GET("/rest/api/2/project/{projectIdOrKey}")
    Observable<Project> getProject();

}
