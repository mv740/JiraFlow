package ca.michalwozniak.jiraflow;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.adapter.CardViewAdapter;
import ca.michalwozniak.jiraflow.helper.ImageIcon;
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

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private Drawer drawerResult = null;
    private Activity myActivity;
    private List<Project> projects;
    private CardViewAdapter cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.myActivity = getParent();


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Projects");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //loading
        //circleProgressBar.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.atlassianNavy));

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background_account_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withProfileImagesClickable(false)
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        //Now create your drawer and pass the AccountHeader.Result
        drawerResult = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("test1"),
                        new SecondaryDrawerItem().withName("test2"),
                        new DividerDrawerItem(),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(R.drawable.ic_settings_grey600_48dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(HomeActivity.this, ((Nameable) drawerItem).getName().getText(HomeActivity.this), Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                })
                //additional Drawer setup as shown above
                .build();


        drawerResult.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
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

                                        getAllboards();
                                    }
                                }
        );
    }


    private void getAllboards() {

        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, "mv740", "Wozm__06");

        final DownloadResourceManager downloadResourceManager = new DownloadResourceManager(HomeActivity.this, "mv740", "Wozn__06");
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

                        generateProjectCards(projects, null, downloadResourceManager);
                    }
                });

    }

    private void generateProjectCards(final List<Project> projects, List<Card> refreshedCardList, final DownloadResourceManager downloadResourceManager) {
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

                    //// TODO: 4/22/2016 do each condition for each imageType
                    if (ResourceManager.getImageType(response.headers().get("Content-Type")) == ImageType.SVG) {
                        String destinationName = project.getKey() + ".svg";
                        String name = project.getAvatarUrls().getSmall();
                        ImageIcon imageIcon = new ImageIcon(destinationName, HomeActivity.this, project, ImageType.SVG);
                        downloadResourceManager.add(name, destinationName, imageIcon);


                    } else {
                        String url = myActivity.getFilesDir() + "/" + "museum_ex_1.png";
                    }
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
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
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

    @Override
    public void onRefresh() {
        getAllboards();
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

//        //add only new project
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

