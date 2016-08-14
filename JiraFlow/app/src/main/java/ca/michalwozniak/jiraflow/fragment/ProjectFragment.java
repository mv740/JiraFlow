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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.adapter.CardViewAdapter;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Activity myActivity;
    private List<Project> projects;
    private CardViewAdapter cardView;
    private Unbinder unbinder;
    private SessionManager sessionManager;

    public ProjectFragment() {
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
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.sessionManager = SessionManager.getInstance(myActivity);


        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        projects = new ArrayList<>();
        cardView = new CardViewAdapter(projects);
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

        jiraService.getAllProjects()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Project>>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("errorSubscriber", e.getMessage());
                    }

                    @Override
                    public void onNext(List<Project> projects) {

                        generateProjectCards(projects);
                    }
                });

    }

    private void generateProjectCards(final List<Project> projects) {
        for (final Project project : projects) {


            OkHttpClient httpClient = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder().url(project.getAvatarUrls().getExtraSmall()).build();

            okhttp3.Call call1 = httpClient.newCall(request);
            call1.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(final okhttp3.Call call, okhttp3.Response response) throws IOException {

                    project.setImageType(ResourceManager.getImageType(response.headers().get("Content-type")));
                    response.body().close();
                }
            });
        }

        updateCardList(projects);
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
    }
}
