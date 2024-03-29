package ca.michalwozniak.jiraflow.features.dashboard.stream;

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
import ca.michalwozniak.jiraflow.model.Feed.Entry;
import ca.michalwozniak.jiraflow.utility.AnimationUtil;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import rx.Subscription;


public class StreamFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;

    private List<Entry> messagesHistory;
    private List<Entry> messages;
    private CardViewMessageAdapter cardView;
    private Unbinder unbinder;
    private NetworkManager networkManager;
    private Subscription subscription;

    public StreamFragment() {
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
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        unbinder = ButterKnife.bind(this, view);

        networkManager = NetworkManager.getInstance(getContext());

        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        messages = new ArrayList<>();
        cardView = new CardViewMessageAdapter(messages, getContext());
        rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);

        getActivityStream();

        return view;
    }

    private void getActivityStream() {

        swipeRefreshLayout.post(() -> {
                    swipeRefreshLayout.setRefreshing(true);
                    subscription = networkManager
                            .getActivityStream()
                            .subscribe(this::updateCardList);
                }
        );
    }

    /**
     * Update card view list, add unique entry on refresh and delete removed entries
     *
     * @param activityFeed Jira activity feed
     */
    public void updateCardList(List<Entry> activityFeed) {

        List<Entry> newEntrytoAddList = new ArrayList<>();
        List<Entry> entriesToRemove = new ArrayList<>();

        //check if it is first run or refreshing list


        if (messagesHistory != null) {
            //remove deleted projects
            for (Entry entry : messagesHistory) {
                boolean stillExist = false;
                for (Entry entryFeed : activityFeed) {

                    if (Objects.equals(entryFeed.getUpdated(), entry.getUpdated())) {
                        stillExist = true;
                    }
                }
                if (!stillExist) {
                    entriesToRemove.add(entry);
                }
            }

            //add only new project
            for (Entry newEntry : activityFeed) {
                boolean duplicate = false;
                for (Entry currentEntry : messagesHistory) {
                    if (Objects.equals(currentEntry.getUpdated(), newEntry.getUpdated())) {
                        duplicate = true;
                    }
                }
                if (!duplicate) {
                    newEntrytoAddList.add(newEntry);
                }
            }


            //delete entries
            for (Entry toDelete : entriesToRemove) {
                cardView.remove(toDelete);
            }
            //add new entries
            for (Entry item : newEntrytoAddList) {
                cardView.add(0, item);
                new Handler().postDelayed(() -> {
                    cardView.notifyInserted(0);
                    rv.scrollToPosition(0);
                }, 500);

            }


        }
        boolean toNotify = false;
        if (cardView.getItemCount() == 0) {

            toNotify = true;
            messages.addAll(activityFeed);
        }

        //save cards history for later comparing if old entry were deleted or new were added
        messagesHistory = new ArrayList<>(messages);
        Handler handler = new Handler();
        boolean finalToNotify = toNotify;
        handler.postDelayed(() -> {
            if (finalToNotify) {
                cardView.notifyDataSetChanged();
            }
            AnimationUtil.stopRefreshAnimation(swipeRefreshLayout);
        }, 1000);
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
        getActivityStream();
    }
}
