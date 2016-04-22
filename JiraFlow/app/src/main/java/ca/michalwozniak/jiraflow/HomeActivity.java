package ca.michalwozniak.jiraflow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.DownloadResourceManager;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private Drawer drawerResult = null;
    private List<Project> projectList = null;
    private Activity myActivity;
    private boolean firstCard = true;
    @Bind(R.id.refresh)
    MaterialRefreshLayout materialRefreshLayout;
    @Bind(R.id.progressBar)
    CircleProgressBar circleProgressBar;
    @Bind(R.id.material_listview)
    MaterialListView mListView;
    private List<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.myActivity = getParent();
        cards = new ArrayList<>();

        //loading
        circleProgressBar.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.atlassianNavy));

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_drawer_badge)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(name)
                                .withEmail(email)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //Now create your drawer and pass the AccountHeader.Result
        drawerResult = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult)
                //additional Drawer setup as shown above
                .build();


        drawerResult.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getAllboards();
            }
        });


        getAllboards();


    }


    private boolean alreadyExist(String title) {

        for (Card currentCard : cards) {
            if (Objects.equals(title, currentCard.getProvider().getTitle())) {
                return true;
            }
        }
        return false;
    }


    private void getAllboards() {

        final List<Card> refreshedCardList = new ArrayList<>();
        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, "mv740", "Wozm__06");
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
                        Log.e("errorSubscriber",e.getMessage());
                    }

                    @Override
                    public void onNext(List<Project> projects) {
                        if(!projects.isEmpty())
                        {
                            final DownloadResourceManager downloadResourceManager = new DownloadResourceManager(HomeActivity.this, "mv740", "Wozn__06");
                            for (final Project project : projects) {


                                OkHttpClient httpClient = new OkHttpClient();
                                okhttp3.Request request = new okhttp3.Request.Builder().url(project.getAvatarUrls().getExtraSmall()).build();


                                final Card.Builder cardB = new Card.Builder(HomeActivity.this)
                                        .setTag("project")
                                        .withProvider(new CardProvider())
                                        .setLayout(R.layout.material_project_card)
                                        .setTitle(project.getName())
                                        .setTitleColor(Color.BLACK)
                                        .setSubtitle(project.getProjectTypeKey())
                                        .setSubtitleColor(Color.DKGRAY).endConfig();

                                // cards.add(cardB.build());
                                refreshedCardList.add(cardB.build());

                                okhttp3.Call call1 = httpClient.newCall(request);
                                call1.enqueue(new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(okhttp3.Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(final okhttp3.Call call, okhttp3.Response response) throws IOException {

                                        //// TODO: 4/22/2016 do each condition for each imageType
                                        if (ResourceManager.getImageType(response.headers().get("Content-Type")) == ImageType.SVG) {


                                            if (!alreadyExist(cardB.build().getProvider().getTitle())) {
                                                String destinationName = project.getKey() + ".svg";
                                                String name = project.getAvatarUrls().getSmall();
                                                downloadResourceManager.add(name, destinationName);
                                                

                                                final Drawable drawable = ResourceManager.getDrawableFromSVG(destinationName, getApplicationContext());

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Card card = cardB.build().getProvider().setDrawable(drawable).endConfig().build();
                                                        mListView.getAdapter().add(card);
                                                        cards.add(card);

                                                        if (firstCard) {
                                                            circleProgressBar.setVisibility(View.GONE);
                                                            firstCard = false;
                                                        }
                                                    }
                                                });
                                            }

                                        } else {
                                            String url = myActivity.getFilesDir() + "/" + "museum_ex_1.png";
                                            cardB.build().getProvider().setDrawable(url);
                                        }

                                        response.body().close();
                                    }
                                });


                            }

                            mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(final Card card, final int position) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("CARD_TYPE", card.getProvider().getTitle() + " - position :" + position);

                                        }
                                    },250);
                                }

                                @Override
                                public void onItemLongClick(Card card, int position) {
                                    Log.d("LONG_CLICK", card.getTag().toString());
                                }
                            });
                            removeDeleteCards(refreshedCardList);
                        }
                    }
                });



    }

    private void removeDeleteCards(List<Card> refreshedCardList) {

        List<Card> tobeRemoved = new ArrayList<>();

        //// TODO: 4/20/2016 need to make this better, also cn only delete last project in the list
        for (Card card : cards) {

            boolean exist = false;
            for (Card card1 : refreshedCardList) {

                if (card.getProvider().getTitle().equals(card1.getProvider().getTitle())) {
                    exist = true;
                }
            }
            if (!exist) {
                int index = mListView.getAdapter().getItemCount();
                Log.e("itemCount", String.valueOf(index));

                int size = cards.size();
                for (int i = 0; i < size; i++) {
                    Log.e("index", String.valueOf(i));
                    Card currentCard = mListView.getAdapter().getCard(i);
                    if (currentCard != null) {
                        if (currentCard.getProvider().getTitle().equals(card.getProvider().getTitle())) {
                            currentCard.setDismissible(true);
                            mListView.getAdapter().remove(currentCard, false);
                            tobeRemoved.add(currentCard);
                        }
                    }
                }

            }
        }

        for (Card card : tobeRemoved) {
            cards.remove(card);
        }

        mListView.getAdapter().notifyDataSetChanged();
        materialRefreshLayout.finishRefresh();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = drawerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (drawerResult != null && drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public List<Project> getProjectList() {
        return projectList;
    }
}
