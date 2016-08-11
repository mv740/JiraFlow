package ca.michalwozniak.jiraflow.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.adapter.CardViewProjectIssueAdapter;
import ca.michalwozniak.jiraflow.helper.JQLHelper;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.model.Issue.ProjectIssues;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ProjectIssuesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Activity myActivity;
    private List<Issue> projectIssues;
    private CardViewProjectIssueAdapter cardView;
    private Unbinder unbinder;
    private SessionManager sessionManager;
    private String projectID;

    public ProjectIssuesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects_issues, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.sessionManager = SessionManager.getInstance(myActivity);

        projectID = getArguments().getString("project");

        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        projectIssues = new ArrayList<>();
        cardView = new CardViewProjectIssueAdapter(projectIssues);
        rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getProjectsIssues();
                                    }
                                }
        );

        myActivity = super.getActivity();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getProjectsIssues();
    }

    private void getProjectsIssues() {

        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword());

        JQLHelper jqlHelper = new JQLHelper(JQLHelper.Query.PROJECT.toString(), projectID);
        jiraService.getProjectIssues(jqlHelper.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProjectIssues>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("errorSubscriber", e.getMessage());

                    }

                    @Override
                    public void onNext(ProjectIssues projectIssues) {

                        generateIssueCards(projectIssues.getIssues());
                    }
                });

    }

    private void generateIssueCards(final List<Issue> issues) {
//        for (final Issue issue : issues) {
//
//
//            Log.e("link",project.getAvatarUrls().getBig());
//            //http://173.176.41.65:8000/secure/projectavatar?size=small&pid=10001
//            OkHttpClient httpClient = new OkHttpClient();
//
//            String link = project.getAvatarUrls().getBig();
//            if(ResourceManager.isSVG(link))
//            {
//                link = ResourceManager.fixImageUrl(project.getAvatarUrls().getBig());
//
//            }
//
//            okhttp3.Request request = new okhttp3.Request.Builder().url(link).build();
//
//            okhttp3.Call call1 = httpClient.newCall(request);
//            call1.enqueue(new okhttp3.Callback() {
//                @Override
//                public void onFailure(okhttp3.Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(final okhttp3.Call call, okhttp3.Response response) throws IOException {
//
//                    issue.setImageType(ResourceManager.getImageType(response.headers().get("Content-type")));
//                    response.body().close();
//                }
//            });
//        }

        updateCardList(issues);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                cardView.notifyDataSetChanged();
                // stopping swipe refresh
                if (swipeRefreshLayout != null) {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 1000);
    }

    public void updateCardList(List<Issue> issueList) {
        //remove deleted projects
        for (Issue old : projectIssues) {
            boolean stillExist = false;
            for (Issue current : issueList) {

                if (Objects.equals(current.getId(), old.getId())) {
                    stillExist = true;
                }
            }
            if (!stillExist) {
                projectIssues.remove(old);
            }
        }
        //add only new project
        for (Issue newIssue : issueList) {
            boolean duplicate = false;
            for (Issue currentProject : projectIssues) {
                if (Objects.equals(currentProject.getId(), newIssue.getId())) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                projectIssues.add(newIssue);
            }
        }
    }
}
