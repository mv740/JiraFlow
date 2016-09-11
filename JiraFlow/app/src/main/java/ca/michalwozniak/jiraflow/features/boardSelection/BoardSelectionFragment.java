package ca.michalwozniak.jiraflow.features.boardSelection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ca.michalwozniak.jiraflow.model.SprintData;
import ca.michalwozniak.jiraflow.utility.AnimationUtil;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import ca.michalwozniak.jiraflow.utility.SessionManager;


public class BoardSelectionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Unbinder unbinder;
    private SessionManager sessionManager;
    private CardViewActiveSprintAdapter sprintAdapter;
    private List<SprintData> boardSprint;
    private NetworkManager networkManager;

    public BoardSelectionFragment() {
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

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select board");
        }

        sessionManager = SessionManager.getInstance(getContext());
        networkManager = NetworkManager.getInstance(getContext());
        boardSprint = new ArrayList<>();

        FragmentManager fragmentManager = getFragmentManager();
        sprintAdapter = new CardViewActiveSprintAdapter(boardSprint, fragmentManager, sessionManager);

        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setAdapter(sprintAdapter);

        //  rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        getBoardCardList();


        // Inflate the layout for this fragment
        return view;
    }

    private void getBoardCardList() {

        swipeRefreshLayout.post(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    networkManager.getActiveSprintBoard().subscribe(this::updateCardList);
                }
        );

    }


    /**
     * Prevent duplicate and remove deleted sprint from view
     *
     * @param sprintDatas
     */
    public void updateCardList(List<SprintData> sprintDatas) {
        //remove deleted projects
        for (SprintData oldSprintData : boardSprint) {
            boolean stillExist = false;
            for (SprintData currentProject : sprintDatas) {

                if (Objects.equals(currentProject.getName(), oldSprintData.getName())) {
                    stillExist = true;
                }
            }
            if (!stillExist) {
                boardSprint.remove(oldSprintData);
            }
        }
        //add only new sprintData
        for (SprintData newSprintData : sprintDatas) {
            boolean duplicate = false;
            for (SprintData currentProject : boardSprint) {
                if (Objects.equals(currentProject.getName(), newSprintData.getName())) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                boardSprint.add(newSprintData);
            }
        }
        sprintAdapter.notifyDataSetChanged();
        AnimationUtil.stopRefreshAnimation(swipeRefreshLayout);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        getBoardCardList();
    }

}