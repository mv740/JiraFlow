package ca.michalwozniak.jiraflow.features.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.features.board.BoardFragment;
import ca.michalwozniak.jiraflow.features.dashboard.myIssues.AssignedIssuesFragment;
import ca.michalwozniak.jiraflow.features.dashboard.projects.ProjectFragment;
import ca.michalwozniak.jiraflow.features.dashboard.stream.StreamFragment;
import ca.michalwozniak.jiraflow.features.login.LoginActivity;
import ca.michalwozniak.jiraflow.fragment.Two;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import ca.michalwozniak.jiraflow.utility.SessionManager;

public class DashboardActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    private SessionManager sm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Dashboard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        sm = SessionManager.getInstance(this);


        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(final ImageView imageView, final Uri uri, final Drawable placeholder) {
                //super.set(imageView, uri, placeholder);

                final GlideUrl glideUrl = new GlideUrl(sm.getProfileIconUrl(), new LazyHeaders.Builder()
                        .addHeader("Authorization", ResourceManager.getEncoredCredentialString(DashboardActivity.this))
                        .addHeader("Accept", "application/json")
                        .build());

                GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = ResourceManager.getGenericRequestBuilderForSVG(imageView.getContext());

                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .load(uri)
                        .listener(new RequestListener<Uri, PictureDrawable>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<PictureDrawable> target, boolean isFirstResource) {

                                Glide.with(imageView.getContext())
                                        .load(glideUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .dontAnimate()
                                        .dontTransform()
                                        .into(imageView);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(PictureDrawable resource, Uri model, Target<PictureDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .dontAnimate()
                        .dontTransform()
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
                //super.cancel(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return super.placeholder(ctx);
            }

        });

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background_account_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(sm.getUsername()).withEmail(sm.getEmail()).withIcon(Uri.parse(sm.getProfileIconUrl()))
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
        Drawer drawerResult = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Home").withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            if (viewFlipper.getDisplayedChild() != 0) {
                                if (tabLayout.getVisibility() == View.GONE) {
                                    tabLayout.setVisibility(View.VISIBLE);
                                    toolbar.setSubtitle(null);
                                }
                                // toolbar.getMenu().setGroupEnabled(0,false);
                                toolbar.getMenu().setGroupVisible(0, false);
                                viewFlipper.setDisplayedChild(0);
                            }
                            return false;
                        }),
                        new SecondaryDrawerItem().withName("Board").withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            if (viewFlipper.getDisplayedChild() != 1) {
                                if (tabLayout.getVisibility() == View.VISIBLE) {

                                    tabLayout.setVisibility(View.GONE);
                                }
                                viewFlipper.setDisplayedChild(1);

                                if(sm.hasSelectedBoard())
                                {
                                    showFragment(BoardFragment.newInstance());
                                }else
                                {
                                    showFragment(new Two());
                                }

                            }
                            return false;
                        }),
                        new DividerDrawerItem(),
                        new ProfileSettingDrawerItem().withName("Logout").withIcon(R.drawable.ic_settings_grey600_48dp).withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            //// TODO: 8/10/2016 for now testing logout will
                            sm.deleteUser();
                            Intent intent = new Intent(DashboardActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            return false;
                        })
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem instanceof Nameable) {
                        Toast.makeText(DashboardActivity.this, ((Nameable) drawerItem).getName().getText(DashboardActivity.this), Toast.LENGTH_SHORT).show();
                    }

                    return false;
                })
                //additional Drawer setup as shown above
                .build();


        drawerResult.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        //viewPager.setVisibility(View.VISIBLE);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StreamFragment(), "Stream");
        adapter.addFragment(new AssignedIssuesFragment(), "My Issues");
        adapter.addFragment(new ProjectFragment(), "Projects");
        viewPager.setAdapter(adapter);
    }

    //used to add icon to title  http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
