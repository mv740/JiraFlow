package ca.michalwozniak.jiraflow.features.dashboard.projects.issues;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Issue.Issue;
import ca.michalwozniak.jiraflow.utility.NetworkManager;

public class ProjectActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private List<Issue> projectIssues;
    private CardViewProjectIssueAdapter cardView;
    private String projectTitle;
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            projectTitle = getIntent().getStringExtra("title");
            getSupportActionBar().setTitle(projectTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
        networkManager = NetworkManager.getInstance(getApplicationContext());

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        projectIssues = new ArrayList<>();
        cardView = new CardViewProjectIssueAdapter(projectIssues);
        rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    getProjectsIssues();
                }
        );

    }

    @Override
    public void onRefresh() {
        getProjectsIssues();
    }

    private void getProjectsIssues() {

        networkManager.getProjectIssues(projectTitle)
                .subscribe(projectIssues -> generateIssueCards(projectIssues.getIssues()));

    }

    private void generateIssueCards(final List<Issue> issues) {

        updateCardList(issues);
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
