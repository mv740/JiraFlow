package ca.michalwozniak.jiraflow.features.boardSelection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import ca.michalwozniak.jiraflow.model.SprintState;
import ca.michalwozniak.jiraflow.utility.AnimationUtil;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import rx.Subscription;


public class BoardSelectionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Unbinder unbinder;
    private SessionManager sessionManager;
    private CardViewActiveSprintAdapter sprintAdapter;
    private List<SprintState> boardSprint;
    private NetworkManager networkManager;
    private Subscription subscription;

    public BoardSelectionFragment() {
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
                    subscription = networkManager.getActiveSprintBoard().subscribe(this::updateCardList);
                }
        );

    }


    /**
     * Prevent duplicate and remove deleted sprint from view
     *
     * @param sprintStates
     */
    public void updateCardList(List<SprintState> sprintStates) {
        //remove deleted projects
        for (SprintState oldSprintState : boardSprint) {
            boolean stillExist = false;
            for (SprintState currentProject : sprintStates) {

                if (Objects.equals(currentProject.getName(), oldSprintState.getName())) {
                    stillExist = true;
                }
            }
            if (!stillExist) {
                boardSprint.remove(oldSprintState);
            }
        }
        //add only new sprintData
        for (SprintState newSprintState : sprintStates) {
            boolean duplicate = false;
            for (SprintState currentProject : boardSprint) {
                if (Objects.equals(currentProject.getName(), newSprintState.getName())) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                boardSprint.add(newSprintState);
            }
        }
        sprintAdapter.notifyDataSetChanged();
        AnimationUtil.stopRefreshAnimation(swipeRefreshLayout);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (subscription != null) subscription.unsubscribe();

    }

    @Override
    public void onRefresh() {
        getBoardCardList();
    }

}