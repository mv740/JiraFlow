package ca.michalwozniak.jiraflow.utility;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import ca.michalwozniak.jiraflow.helper.JQLHelper;
import ca.michalwozniak.jiraflow.model.BoardConfiguration;
import ca.michalwozniak.jiraflow.model.BoardList;
import ca.michalwozniak.jiraflow.model.EmptyResponse;
import ca.michalwozniak.jiraflow.model.Feed.ActivityFeed;
import ca.michalwozniak.jiraflow.model.Feed.Entry;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.model.Issue.userIssues;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.model.Sprint;
import ca.michalwozniak.jiraflow.model.SprintData;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.model.transition.Transition;
import ca.michalwozniak.jiraflow.model.transition.TransitionModel;
import ca.michalwozniak.jiraflow.model.transition.TransitionPossible;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Michal Wozniak on 9/8/2016.
 */

public class NetworkManager {

    private final JiraSoftwareService jiraService;
    private final JiraSoftwareService jiraServiceXML;
    private final SessionManager sessionManager;
    private static NetworkManager instance = null;

    private NetworkManager(Context context) {

        this.sessionManager = SessionManager.getInstance(context);
        this.jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword(), sessionManager.getServerUrl());
        this.jiraServiceXML = ServiceGenerator.createServiceXML(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword(), sessionManager.getServerUrl());
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

    public Observable<BoardList> getAllBoards() {
        return jiraService.getAllBoards(null, null, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getAllBoards", error.getMessage()));
    }

    public Observable<List<Project>> getAllProjects() {
        return jiraService.getAllProjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getAllProjects", error.getMessage()));
    }


    public Observable<Sprint> getSprintsForBoard(int boardId) {
        return jiraService.getSprintsForBoard(boardId, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getSprintsForBoard", error.getMessage()));
    }

    public Observable<List<SprintData>> getActiveSprintBoard() {
        return getAllBoards()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMapIterable(BoardList::getValues)
                .flatMap(board -> this.getSprintsForBoard(board.getId()))
                .flatMapIterable(Sprint::getValues)
                .filter(sprintData -> sprintData.getState().equalsIgnoreCase("active"))
                .toList();
    }


    public Observable<Transition> getTransitions(String issueKey, String name) {
        return jiraService.getTransitions(issueKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(TransitionPossible::getTransitions)
                .filter(transition -> transition.getName().equalsIgnoreCase(name))
                .doOnError(error -> Log.e("getTransitions", error.getMessage()));
    }

    public Observable<EmptyResponse> doTransition(String currentDraggedIssueKey, TransitionModel tm) {
        return jiraService.doTransition(currentDraggedIssueKey, tm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("doTransition", error.getMessage()));
    }

    public Observable<BoardConfiguration> getBoardConfiguration(int boardId) {
        return jiraService.getBoardConfiguration(boardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getBoardConfiguration", error.getMessage()));
    }

    public Observable<Sprint> getIssuesForSprint(int sprintID) {
        return jiraService.getIssuesForSprint(sprintID, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<User> logIn(final String username, final String password, final String url) {
        final JiraSoftwareService jiraSoftwareService = ServiceGenerator.createService(JiraSoftwareService.class, username, password, url);

        return jiraSoftwareService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<Entry>> getActivityStream() {

        return jiraServiceXML.getActivityFeed()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getActivityFeed", error.getMessage()))
                .flatMapIterable(ActivityFeed::getEntry)
                .map(entry -> {

                    OkHttpClient httpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(entry.getAuthor().getLink().get(0).getHref()).build();

                    Call newCall = httpClient.newCall(request);
                    newCall.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(final Call call, Response response) throws IOException {

                            entry.setImageType(ResourceManager.getImageType(response.headers().get("Content-type")));
                            response.body().close();
                        }
                    });

                    return entry;
                })
                .toList();

    }

}
