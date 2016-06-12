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

import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.adapter.CardViewMessageAdapter;
import ca.michalwozniak.jiraflow.helper.ImageIcon;
import ca.michalwozniak.jiraflow.model.Feed.ActivityFeed;
import ca.michalwozniak.jiraflow.model.Feed.Entry;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.DownloadResourceManager;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StreamFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity myActivity;
    private List<Entry> messages;
    private CardViewMessageAdapter cardView;

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
        View view =  inflater.inflate(R.layout.fragment_stream, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
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
    public void onRefresh() {
        getActivityStream();
    }

    private void getActivityStream() {

        JiraSoftwareService jiraService = ServiceGenerator.createServiceXML(JiraSoftwareService.class, "mv740", "Wozm__06");

        final DownloadResourceManager downloadResourceManager = new DownloadResourceManager(super.getActivity(), "mv740", "Wozn__06");
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

                        generateMessageFeedCards(feed.getEntry(), downloadResourceManager);
                    }
                });

    }

    private void generateMessageFeedCards(final List<Entry> activityFeed, final DownloadResourceManager downloadResourceManager) {
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

                    //// TODO: 4/22/2016 do each condition for each imageType
                    if (ResourceManager.getImageType(response.headers().get("Content-Type")) == ImageType.SVG) {
                        String destinationName = entry.getAuthor().getName() + ".svg";
                        String name = entry.getAuthor().getLink().get(0).getHref();
                        ImageIcon imageIcon = new ImageIcon(destinationName, myActivity, entry, ImageType.SVG);
                        downloadResourceManager.add(name, destinationName, imageIcon);


                    } else {
                        String url = myActivity.getFilesDir() + "/" + "museum_ex_1.png";
                    }
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
                swipeRefreshLayout.setRefreshing(false);
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

        for(Entry test : messages)
        {
            Log.d("test",test.getAuthor().getName());
        }
    }
}
