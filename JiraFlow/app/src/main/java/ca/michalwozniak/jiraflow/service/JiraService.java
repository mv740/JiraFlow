package ca.michalwozniak.jiraflow.service;

import java.util.List;

import ca.michalwozniak.jiraflow.model.Board;
import ca.michalwozniak.jiraflow.model.BoardList;
import ca.michalwozniak.jiraflow.model.Project;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by michal on 4/16/2016.
 *
 * interacting with JIRA agile REST API : https://docs.atlassian.com/jira-software/REST/cloud/
 */
public interface JiraService {

    //board query
    @GET("/rest/agile/1.0/board/{boardId}")
    Call<Board> getBoard();

    @GET("/rest/agile/1.0/board")
    Call<BoardList> getAllBoards();

    @POST("/rest/agile/1.0/board")
    Call<Board> createBoard(@Body Board board);

    @DELETE("/rest/agile/1.0/board/{boardId}")
    Call<Board> deleteBoard(@Body Board board);

    //project query
    @GET("/rest/api/2/project")
    Call<List<Project>> getAllProjects();

    @GET("/rest/api/2/project/{projectIdOrKey}")
    Call<Project> getProject();


}
