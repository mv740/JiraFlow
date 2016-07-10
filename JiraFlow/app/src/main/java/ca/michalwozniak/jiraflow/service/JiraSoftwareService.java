package ca.michalwozniak.jiraflow.service;

import java.util.List;

import ca.michalwozniak.jiraflow.model.Feed.ActivityFeed;
import ca.michalwozniak.jiraflow.model.Issue.ProjectIssues;
import ca.michalwozniak.jiraflow.model.Project;
import retrofit2.http.GET;
import retrofit2.http.Query;
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

    //@GET("/rest/api/2/search?jql={project}")
    @GET("/rest/api/2/search?jql")
    Observable<ProjectIssues> getProjectIssues(@Query("project") String project);

    @GET("/activity")
    Observable<ActivityFeed> getActivityFeed();

}
