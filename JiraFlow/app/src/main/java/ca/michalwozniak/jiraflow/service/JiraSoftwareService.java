package ca.michalwozniak.jiraflow.service;

import java.util.List;

import ca.michalwozniak.jiraflow.model.BoardConfiguration;
import ca.michalwozniak.jiraflow.model.BoardList;
import ca.michalwozniak.jiraflow.model.CreateIssueMetaField;
import ca.michalwozniak.jiraflow.model.CreateIssueModel;
import ca.michalwozniak.jiraflow.model.EmptyResponse;
import ca.michalwozniak.jiraflow.model.Feed.ActivityFeed;
import ca.michalwozniak.jiraflow.model.Issue.ProjectIssues;
import ca.michalwozniak.jiraflow.model.Issue.issueType;
import ca.michalwozniak.jiraflow.model.Issue.userIssues;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.model.Sprint;
import ca.michalwozniak.jiraflow.model.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by michal on 4/16/2016.
 * <p/>
 * interacting with JIRA agile REST API : https://docs.atlassian.com/jira-software/REST/cloud/
 */
public interface JiraSoftwareService {

    ///Get
    @GET("/rest/api/2/myself")
    Observable<User> getUser();

    //https://docs.atlassian.com/jira/REST/latest/#api/2/user
    @GET("/rest/api/2/user")
    Observable<User> getUser(@Query("username") String username, @Query("key") String key);

    //project query
    @GET("/rest/api/2/project")
    Observable<List<Project>> getAllProjects();

    @GET("/rest/api/2/project/{projectIdOrKey}")
    Observable<Project> getProject();

    //@GET("/rest/api/2/search?jql={project}")
    @GET("/rest/api/2/search")
    Observable<ProjectIssues> getProjectIssues(@Query("jql") String project);

    //@GET("/rest/api/2/search?jql")
    //Observable<userIssues> getUserIssues(@Query("assignee") String username);

    @GET("/rest/api/2/search")
    Observable<userIssues> getUserIssues(@Query("jql") String username);

    //xml response
    @GET("/activity")
    Observable<ActivityFeed> getActivityFeed();

    @GET("rest/agile/1.0/board")
    Observable<BoardList> getAllBoards(@Query("maxResults") int max, @Query("startAt") int start, @Query("projectKeyOrId") String keyId, @Query("type") String type);

    @GET("rest/agile/1.0/board/{boardId}/configuration")
    Observable<BoardConfiguration> getBoardConfiguration(@Path("boardId") int i);

    //// TODO: 8/6/2016 remove or keep ?
    @GET("/rest/api/2/issuetype/{issueKeyOrId}")
    Observable<issueType> getIssueType(@Path("issueKeyOrId") String id);
    //http://173.176.41.65:8000/rest/api/2/issuetype/10004

    @GET("rest/agile/1.0/board/{boardId}/sprint")
    Observable<Sprint> getSprintsForBoard(@Path("boardId") int id, @Query("state") String state, @Query("maxResults") Integer max);

    @GET("rest/agile/1.0/sprint/{sprintId}/issue")
    Observable<Sprint> getIssuesForSprint(@Path("sprintId") int id, @Query("state") String state, @Query("maxResults") Integer max);

    @GET("/rest/api/2/search")
    Observable<Sprint> getIssuesForActiveSprint(@Query("jql") String project);

    @GET("/rest/api/2/issue/createmeta")
    Observable<CreateIssueMetaField> getCreateIssueMeta(@Query("projectIds") String projectIds, @Query("projectKeys") String projectKeys, @Query("issuetypeIds") String issuetypeIds, @Query("issuetypeNames") String issuetypeNames);

    //https://docs.atlassian.com/jira/REST/cloud/#api/2/issue-createIssue
    @POST("rest/api/2/issue")
    Observable<EmptyResponse> createIssue(@Body CreateIssueModel createIssueModel);

}
