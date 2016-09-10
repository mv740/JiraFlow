package ca.michalwozniak.jiraflow.features.dashboard.myIssues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.features.createIssue.CreateIssueActivity;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import top.wefor.circularanim.CircularAnim;


public class AssignedIssuesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private List<Issue> issues;
    private CardViewIssueAdapter cardView;
    private Unbinder unbinder;
    private Map<String, Boolean> menuChecked;

    private NetworkManager networkManager;

    public AssignedIssuesFragment() {
        // Required empty public constructor
    }


    @OnClick(R.id.fabNewIssue)
    public void newIssueClick(View view) {

        final Activity activity = this.getActivity();

        CircularAnim.fullActivity(activity, view)
                .colorOrImageRes(R.color.atlassianNavy)
                .go(() -> startActivity(new Intent(activity, CreateIssueActivity.class)));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assigned_issues, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.networkManager =  NetworkManager.getInstance(this.getContext());


        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        //initialized checked menu items
        menuChecked = new HashMap<>();
        menuChecked.put("todo", Boolean.TRUE);
        menuChecked.put("inProgress", Boolean.TRUE);
        menuChecked.put("done", Boolean.FALSE);


        issues = new ArrayList<>();
        cardView = new CardViewIssueAdapter(issues);
        rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    getUserIssuesCard();
                }
        );

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getUserIssuesCard();
    }

    private void getUserIssuesCard() {
        networkManager.getUserIssues().subscribe(this::updateCardList);
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

        startFilter();

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            cardView.notifyDataSetChanged();
            // stopping swipe refresh
            if (swipeRefreshLayout != null) {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter_issue, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
        menu.add(0,R.id.filter_issue_status_done,0,R.string.done);
        menu.add(0,R.id.filter_issue_status_todo,0,R.string.todo);
        menu.add(0,R.id.filter_issue_status_inProgress,0,R.string.in_progress);

        menu.findItem(R.id.filter_issue_status_done).setVisible(true).setCheckable(true).setChecked(menuChecked.get("done"));
        menu.findItem(R.id.filter_issue_status_todo).setVisible(true).setCheckable(true).setChecked(menuChecked.get("todo"));
        menu.findItem(R.id.filter_issue_status_inProgress).setVisible(true).setCheckable(true).setChecked(menuChecked.get("inProgress"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Boolean isChecked;
        switch (item.getItemId()) {
            case R.id.filter_issue_status_done:

                isChecked = menuChecked.get("done");
                item.setChecked(!isChecked);
                menuChecked.put("done", !isChecked);
                break;
            case R.id.filter_issue_status_todo:
                isChecked = menuChecked.get("todo");
                item.setChecked(!isChecked);
                menuChecked.put("todo", !isChecked);
                break;
            case R.id.filter_issue_status_inProgress:
                isChecked = menuChecked.get("inProgress");
                item.setChecked(!isChecked);
                menuChecked.put("inProgress", !isChecked);
                break;
        }

        startFilter();

        return super.onOptionsItemSelected(item);
    }

    private void startFilter() {
        String filterConstraint = "";
        if (menuChecked.get("todo")) {
            filterConstraint = filterConstraint.concat(" to do ");
        }
        if (menuChecked.get("inProgress")) {
            filterConstraint = filterConstraint.concat(" in progress ");
        }
        if (menuChecked.get("done")) {
            filterConstraint = filterConstraint.concat(" done ");
        }

        cardView.getFilter().filter(filterConstraint);
    }
}
