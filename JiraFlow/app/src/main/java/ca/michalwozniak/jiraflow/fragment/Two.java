package ca.michalwozniak.jiraflow.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.Board;
import ca.michalwozniak.jiraflow.model.BoardList;
import ca.michalwozniak.jiraflow.model.SprintData;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;


public class Two extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Activity myActivity;
    private Unbinder unbinder;
    private SessionManager sessionManager;
    private CardViewActiveSprintAdapter sprintAdapter;
    private List<SprintData> boardSprint;

    public Two() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_board_selection, container, false);
        unbinder = ButterKnife.bind(this, view);
        myActivity = super.getActivity();
        sessionManager = SessionManager.getInstance(myActivity);
        boardSprint = new ArrayList<>();

        FragmentManager fragmentManager = getFragmentManager();
        sprintAdapter = new CardViewActiveSprintAdapter(boardSprint,fragmentManager);

        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setAdapter(sprintAdapter);

        //  rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.postDelayed(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    getBoardList();
                },500 //prevent lag
        );


        // Inflate the layout for this fragment
        return view;
    }


    private void getBoardList() {

        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword(), sessionManager.getServerUrl());

        jiraService.getAllBoards(null, null, null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Log.e("getAllBoards", error.getMessage()))
                .subscribe(this::getActiveSprint);
    }

    private void getActiveSprint(BoardList boardList) {
        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword(), sessionManager.getServerUrl());

        for (Board b : boardList.getValues()) {

            Log.d(TAG, "getActiveSprint: ");
            jiraService.getSprintsForBoard(b.getId(), null, null)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(error -> Log.e("getSprintsForBoard", error.getMessage()))
                    .subscribe(sprint -> {
                        {
                            for (SprintData sd : sprint.getValues()) {
                                if (sd.getState().equalsIgnoreCase("active")) {
                                    boardSprint.add(sd);
                                }
                            }
                        }
                    });
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            sprintAdapter.notifyDataSetChanged();
            // stopping swipe refresh
            if (swipeRefreshLayout != null) {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getBoardList();
    }
}