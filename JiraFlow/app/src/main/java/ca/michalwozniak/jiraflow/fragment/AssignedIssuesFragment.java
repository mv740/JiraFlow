package ca.michalwozniak.jiraflow.fragment;

import android.app.Activity;
import android.content.Intent;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.CreateIssueActivity;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.adapter.CardViewIssueAdapter;
import ca.michalwozniak.jiraflow.helper.JQLHelper;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.model.Issue.userIssues;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.wefor.circularanim.CircularAnim;


public class AssignedIssuesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Activity myActivity;
    private List<Issue> issues;
    private CardViewIssueAdapter cardView;
    private Unbinder unbinder;
    private SessionManager sessionManager;

    public AssignedIssuesFragment() {
        // Required empty public constructor
    }


    @OnClick(R.id.fabNewIssue)
    public void newIssueClick(View view)
    {

        final Activity activity = this.getActivity();

        CircularAnim.fullActivity(activity, view)
                .colorOrImageRes(R.color.atlassianNavy)
                .go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        startActivity(new Intent(activity, CreateIssueActivity.class));
                    }
                });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assigned_issues, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.sessionManager = SessionManager.getInstance(myActivity);


        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        issues = new ArrayList<>();
        cardView = new CardViewIssueAdapter(issues);
        rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getProjects();
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
        getProjects();
    }

    private void getProjects() {

        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword());



        JQLHelper jqlHelper = new JQLHelper(JQLHelper.Query.ASSIGNEE,sessionManager.getUsername());
        jiraService.getUserIssues(jqlHelper.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<userIssues>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("errorSubscriber", e.getMessage());
                    }

                    @Override
                    public void onNext(userIssues userIssues) {

                        Log.v("userIssuesTotal", String.valueOf(userIssues.getTotal()));
                        generateIssueCards(userIssues.getIssues());
                    }
                });

    }

    private void generateIssueCards(final List<Issue> issueList) {

        updateCardList(issueList);
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
        for (Issue oldProject : issues) {
            boolean stillExist = false;
            for (Issue currentProject : issueList) {

                if (Objects.equals(currentProject.getId(), oldProject.getId())) {
                    stillExist = true;
                }
            }
            if (!stillExist) {
                issues.remove(oldProject);
            }
        }
        //add only new project
        for (Issue newProject : issueList) {
            boolean duplicate = false;
            for (Issue currentProject : issues) {
                if (Objects.equals(currentProject.getId(), newProject.getId())) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                issues.add(newProject);
            }
        }
    }
}
