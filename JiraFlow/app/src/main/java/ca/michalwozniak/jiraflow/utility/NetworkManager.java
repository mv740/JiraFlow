package ca.michalwozniak.jiraflow.utility;

import android.content.Context;
import android.util.Log;

import java.util.List;

import ca.michalwozniak.jiraflow.helper.JQLHelper;
import ca.michalwozniak.jiraflow.model.BoardConfiguration;
import ca.michalwozniak.jiraflow.model.BoardList;
import ca.michalwozniak.jiraflow.model.EmptyResponse;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.model.Issue.userIssues;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.model.Sprint;
import ca.michalwozniak.jiraflow.model.transition.TransitionModel;
import ca.michalwozniak.jiraflow.model.transition.TransitionPossible;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Michal Wozniak on 9/8/2016.
 */

public class NetworkManager {

    private final JiraSoftwareService jiraService;
    private final SessionManager sessionManager;
    private static NetworkManager instance = null;

    private NetworkManager(Context context) {

        this.sessionManager = SessionManager.getInstance(context);
        this.jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword(), sessionManager.getServerUrl());
    }

    public static NetworkManager getInstance(Context context) {

        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }


    public Observable<List<Issue>> getUserIssues() {
        JQLHelper jqlHelper = new JQLHelper(JQLHelper.Query.ASSIGNEE, sessionManager.getUsername());
        return jiraService.getUserIssues(jqlHelper.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(userIssues::getIssues)
                .doOnError(error -> Log.e("getUserIssues", error.getMessage()));
    }

    public Observable<BoardList> getAllBoards()
    {
        return jiraService.getAllBoards(null, null, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getAllBoards", error.getMessage()));
    }

    public Observable<List<Project>> getAllProjects()
    {
       return jiraService.getAllProjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getAllProjects", error.getMessage()));
    }


    public Observable<Sprint> getSprintsForBoard(int boardId)
    {
        return jiraService.getSprintsForBoard(boardId, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getSprintsForBoard", error.getMessage()));
    }

    public Observable<TransitionPossible> getTransitions(String issueKey)
    {
        return jiraService.getTransitions(issueKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getPossibleTransition", error.getMessage()));
    }

    public Observable<EmptyResponse> doTransition(String currentDraggedIssueKey, TransitionModel tm)
    {
        return jiraService.doTransition(currentDraggedIssueKey, tm)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getPossibleTransition", error.getMessage()));
    }

    public Observable<BoardConfiguration> getBoardConfiguration(int boardId)
    {
        return jiraService.getBoardConfiguration(boardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getBoardConfiguration", error.getMessage()));
    }

    public Observable<Sprint> getIssuesForSprint(int sprintID)
    {
        return jiraService.getIssuesForSprint(sprintID,null,null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //todo implementation used in Login
    public void getUser()
    {
        //
    }

}
