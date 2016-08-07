package ca.michalwozniak.jiraflow.service;

import java.util.List;

import ca.michalwozniak.jiraflow.model.BoardConfiguration;
import ca.michalwozniak.jiraflow.model.BoardList;
import ca.michalwozniak.jiraflow.model.Feed.ActivityFeed;
import ca.michalwozniak.jiraflow.model.Issue.ProjectIssues;
import ca.michalwozniak.jiraflow.model.Issue.issueType;
import ca.michalwozniak.jiraflow.model.Issue.userIssues;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.model.Sprint;
import ca.michalwozniak.jiraflow.model.User;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by michal on 4/16/2016.
 *
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


//
//        @POST("/api/2/issue/{issueIdOrKey}/comment")
//        void addComment(@Path("issueIdOrKey") String str, @Body AddCommentModel addCommentModel, Callback<Comment> callback);
//
//        @PUT("/api/2/issue/{issueIdOrKey}/assignee")
//        void assignUser(@Path("issueIdOrKey") String str, @Body AssignUserModel assignUserModel, Callback<EmptyResponseModel> callback);
//
//        @POST("/api/2/issue")
//        void createIssue(@Body CreateIssueModel createIssueModel, Callback<CreateIssueResponse> callback);
//
//        @POST("/api/2/issue/{issueIdOrKey}/transitions")
//        void doTransition(@Path("issueIdOrKey") String str, @Body DoTransitionModel doTransitionModel, Callback<EmptyResponseModel> callback);
//
//        @GET("/agile/1.0/board")
//        void getAllBoards(@Query("maxResults") int i, @Query("startAt") int i2, @Query("projectKeyOrId") String str, @Query("type") String str2, Callback<Boards> callback);
//
//        @GET("/api/2/search")
//        void getAllIssues(@Query("jql") String str, @Query("expand") String str2, @Query("fields") String str3, @Query("maxResults") int i, @Query("startAt") int i2, Callback<Issues> callback);
//
//        @GET("/api/2/status")
//        void getAllStatuses(Callback<ArrayList<Status>> callback);
//
//        @GET("/api/2/user/assignable/search")
//        void getAssignableUsers(@Query("username") String str, @Query("issueKey") String str2, @Query("project") String str3, @Query("maxResults") int i, @Query("startAt") int i2, Callback<ArrayList<User>> callback);
//
//        @GET("/agile/1.0/board/{boardId}/configuration")
//        void getBoardConfiguration(@Path("boardId") int i, Callback<BoardConfiguration> callback);
//
//        @GET("/api/2/issue/createmeta")
//        void getCreateIssueMetaData(@Query("expand") String str, @Query("projectIds") String str2, @Query("issuetypeIds") String str3, Callback<CreateIssueMetaWithField> callback);
//
//        @GET("/api/2/filter/{filterId}")
//        void getFilter(@Path("filterId") int i, Callback<Filter> callback);
//
//        @GET("/api/2/issue/{issueKeyOrId}")
//        void getIssue(@Path("issueKeyOrId") String str, @Query("expand") String str2, Callback<Issue> callback);
//
//        @GET("/api/2/issueLinkType")
//        void getIssueLinkTypes(Callback<IssueLinkTypes> callback);
//
//        @GET("/agile/1.0/board/{boardId}/sprint")
//        void getSprintsForBoard(@Path("boardId") int i, @Query("state") String str, @Query("maxResults") int i2, Callback<Sprints> callback);
//
//        @GET("/api/2/myself")
//        void getUser(Callback<com.leanwalk.jirainmotion.services.commands.jiraapi.responses.user.User> callback);
//
//        @POST("/agile/1.0/backlog/issue")
//        void moveIssuesToBacklog(@Body IssuesToMoveModel issuesToMoveModel, Callback<EmptyResponseModel> callback);
//
//        @POST("/agile/1.0/sprint/{sprintId}/issue")
//        void moveIssuesToSprint(@Path("sprintId") int i, @Body IssuesToMoveModel issuesToMoveModel, Callback<EmptyResponseModel> callback);
//
//        @PUT("/agile/1.0/issue/rank")
//        void rankIssues(@Body RankIssuesModel rankIssuesModel, Callback<EmptyResponseModel> callback);
//
//        @GET("/api/2/user/search")
//        void searchUsers(@Query("username") String str, @Query("maxResults") int i, @Query("startAt") int i2, Callback<ArrayList<User>> callback);


}
