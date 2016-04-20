package ca.michalwozniak.jiraflow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.model.ImageType;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.service.JiraService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.DownloadResourceManager;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private Drawer drawerResult = null;
    private List<Project> projectList = null;
    private Activity myActivity;
    private boolean firstCard = true;
    @Bind(R.id.spin_kit) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.myActivity = getParent();

        //loading
        progressBar.setIndeterminateDrawable(new FadingCircle());

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

        getAllboards();


    }

    private void getAllboards() {

        JiraService jiraService = ServiceGenerator.createService(JiraService.class, "mv740", "Wozm__06");
        Call<List<Project>> call = jiraService.getAllProjects();
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if (response.isSuccessful()) {
                    projectList = response.body();
                    Log.e("card", "success");

                    final MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);
                    final List<Card> cards = new ArrayList<>();

                    for (Project project : response.body()) {

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


                        okhttp3.Call call1 = httpClient.newCall(request);
                        call1.enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                                Log.e("wtf", response.headers().get("Content-Type"));
                                Log.e("wtf", String.valueOf(ResourceManager.getImageType(response.headers().get("Content-Type"))));
                                if (ResourceManager.getImageType(response.headers().get("Content-Type")) == ImageType.SVG) {
                                    DownloadResourceManager downloadResourceManager = new DownloadResourceManager(HomeActivity.this, "mv740", "Wozn__06");
                                    downloadResourceManager.add("https://ewok390.atlassian.net/secure/projectavatar?size=xsmall&pid=10001&avatarId=10011", "museum_ex_1.svg");

                                    final Drawable drawable = ResourceManager.getDrawableFromSVG("museum_ex_1.svg",getApplicationContext());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            cardB.build().getProvider().setDrawable(drawable);
                                            cards.add(cardB.build());
                                            mListView.getAdapter().add(cardB.build());
                                            if(firstCard)
                                            {
                                                progressBar.setVisibility(View.GONE);
                                                firstCard =false;
                                            }
                                        }
                                    });
                                } else {
                                    String url = myActivity.getFilesDir() + "/" + "museum_ex_1.png";
                                    cardB.build().getProvider().setDrawable(url);
                                }
                            }
                        });


                        //Drawable drawable = ResourceManager.getDrawableFromSVG(getApplicationContext());

//
//                        Card card = new Card.Builder(HomeActivity.this)
//                                .setTag("project")
//                                .withProvider(new CardProvider())
//                                .setLayout(R.layout.material_project_card)
//                                .setTitle(project.getName())
//                                .setTitleColor(Color.BLACK)
//                                .setSubtitle(project.getProjectTypeKey())
//                                .setSubtitleColor(Color.DKGRAY)
//                                .setDrawable(drawable)
//                                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
//                                    @Override
//                                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
//                                        requestCreator.fit();
//                                    }
//                                })
//                                .endConfig()
//                                .build();
//
//                        cards.add(card);
                        // mListView.getAdapter().add(cardB.build());
                    }

                    //mListView.getAdapter().addAll(cards);
                    mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(Card card, int position) {
                            Log.d("CARD_TYPE", card.getTag().toString());

                        }

                        @Override
                        public void onItemLongClick(Card card, int position) {
                            Log.d("LONG_CLICK", card.getTag().toString());
                        }
                    });
                } else {
                    Log.e("card", "fail");
                }

            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.e("card", "failure");
                Log.e("card", t.toString());
            }
        });
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
