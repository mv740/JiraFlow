package ca.michalwozniak.jiraflow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.service.JiraService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private Drawer drawerResult = null;
    private List<Project> projectList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                if(response.isSuccessful())
                {
                    projectList = response.body();
                    Log.e("card","success");

                    MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);
                    List<Card> cards = new ArrayList<>();

                    for (Project project : response.body())
                    {
                        Log.e("hello",project.getAvatarUrls().getExtraSmall());
                        Card card = new Card.Builder(HomeActivity.this)
                                .setTag("project")
                                .withProvider(new CardProvider())
                                .setLayout(R.layout.material_project_card)
                                .setTitle(project.getName())
                                .setTitleColor(Color.BLACK)
                                .setSubtitle(project.getProjectTypeKey())
                                .setSubtitleColor(Color.DKGRAY)
                                .setDrawable(project.getAvatarUrls().getExtraSmall())
                                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                                    @Override
                                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                                        requestCreator.fit();
                                    }
                                })
                                .endConfig()
                                .build();

                        cards.add(card);
                    }
                    mListView.getAdapter().addAll(cards);
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
                }else
                {
                    Log.e("card","fail");
                }

            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.e("card","failure");
                Log.e("card",t.toString());
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
