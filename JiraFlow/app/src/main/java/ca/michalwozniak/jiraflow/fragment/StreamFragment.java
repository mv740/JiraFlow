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
import ca.michalwozniak.jiraflow.adapter.CardViewMessageAdapter;
import ca.michalwozniak.jiraflow.model.Feed.ActivityFeed;
import ca.michalwozniak.jiraflow.model.Feed.Entry;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.PreferenceManager;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StreamFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Activity myActivity;
    private List<Entry> messages;
    private CardViewMessageAdapter cardView;
    private Unbinder unbinder;
    private PreferenceManager preferenceManager;

    public StreamFragment() {
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
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferenceManager = PreferenceManager.getInstance(myActivity);

        LinearLayoutManager llm = new LinearLayoutManager(super.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        messages = new ArrayList<>();
        cardView = new CardViewMessageAdapter(messages);
        rv.setAdapter(cardView);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getActivityStream();
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
        getActivityStream();
    }

    private void getActivityStream() {

        JiraSoftwareService jiraService = ServiceGenerator.createServiceXML(JiraSoftwareService.class, preferenceManager.getUsername(), preferenceManager.getPassword());

        jiraService.getActivityFeed()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ActivityFeed>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("errorSubscriber", e.getMessage());
                    }

                    @Override
                    public void onNext(ActivityFeed feed) {

                        generateMessageFeedCards(feed.getEntry());
                    }
                });

    }

    private void generateMessageFeedCards(final List<Entry> activityFeed) {
        for (final Entry entry : activityFeed) {


            OkHttpClient httpClient = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder().url(entry.getAuthor().getLink().get(0).getHref()).build();

            okhttp3.Call call1 = httpClient.newCall(request);
            call1.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(final okhttp3.Call call, okhttp3.Response response) throws IOException {

                    entry.setImageType(ResourceManager.getImageType(response.headers().get("Content-type")));
                    response.body().close();
                }
            });
        }

        updateCardList(activityFeed);
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

    public void updateCardList(List<Entry> activityFeed) {
        //remove deleted projects
        for (Entry entry : messages) {
            boolean stillExist = false;
            for (Entry entryFeed : activityFeed) {

                if (Objects.equals(entryFeed.getPublished(), entry.getPublished())) {
                    stillExist = true;
                }
            }
            if (!stillExist) {
                messages.remove(entry);
            }
        }

        //add only new project
        for (Entry newProject : activityFeed) {
            boolean duplicate = false;
            for (Entry currentProject : messages) {
                if (Objects.equals(currentProject.getPublished(), newProject.getPublished())) {
                    duplicate = true;
                }
            }
            if (!duplicate) {
                messages.add(newProject);
            }
        }

        for (Entry test : messages) {
            Log.d("test1", test.getAuthor().getName());
        }
    }
}
