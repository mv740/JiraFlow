package ca.michalwozniak.jiraflow.service;

import ca.michalwozniak.jiraflow.model.Board;
import ca.michalwozniak.jiraflow.model.BoardList;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by michal on 4/22/2016.
 */
public interface JiraAgileService {

    //board query
    @GET("/rest/agile/1.0/board/{boardId}")
    Observable<Board> getBoard();

    @GET("/rest/agile/1.0/board")
    Observable<BoardList> getAllBoards();

    @POST("/rest/agile/1.0/board")
    Observable<Board> createBoard(@Body Board board);

    @DELETE("/rest/agile/1.0/board/{boardId}")
    Observable<Board> deleteBoard(@Body Board board);
}
