package ca.michalwozniak.jiraflow.features.dashboard.projects;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.utility.AnimationUtil;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import rx.Subscription;


public class ProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private List<Project> projects;
    private CardViewProjectAdapter cardView;
    private Unbinder unbinder;
    private NetworkManager networkManager;
    private Subscription subscription;

    public ProjectFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        unbinder = ButterKnife.bind(this, view);

        networkManager = NetworkManager.getInstance(getContext());

        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        projects = new ArrayList<>();
        cardView = new CardViewProjectAdapter(projects);
        rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        getProjects();


        return view;
    }

    private void getProjects() {

        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            subscription = networkManager.getProjectWithUpdatedIconType().subscribe(this::updateCardList);
        });

    }

    public void updateCardList(List<Project> projectList) {
        //remove deleted projects
        for (Project oldProject : projects) {
            boolean stillExist = false;
            for (Project currentProject : projectList) {

                if (Objects.equals(currentProject.getName(), oldProject.getName())) {
                    stillExist = true;
                }
            }
            if (!stillExist) {
                projects.remove(oldProject);
            }
        }
        //add only new project
        for (Project newProject : projectList) {
            boolean duplicate = false;
            for (Project currentProject : projects) {
                if (Objects.equals(currentProject.getName(), newProject.getName())) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                projects.add(newProject);
            }
        }

        Handler post = new Handler();
        post.postDelayed(() -> {
            cardView.notifyDataSetChanged();
            AnimationUtil.stopRefreshAnimation(swipeRefreshLayout);
        }, 500);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public void onRefresh() {
        getProjects();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (subscription != null) subscription.unsubscribe();
    }
}
