package ca.michalwozniak.jiraflow.features.dashboard.projects.issues;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.features.dashboard.projects.ViewPagerAdapter;
import ca.michalwozniak.jiraflow.features.boardSelection.BoardSelectionFragment;

public class ProjectActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private String projectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        ButterKnife.bind(this);
        if(toolbar!=null)
        {
            setSupportActionBar(toolbar);
            projectTitle = getIntent().getStringExtra("title");
            getSupportActionBar().setTitle(projectTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
        
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ProjectIssuesFragment projectIssuesFragment = new ProjectIssuesFragment();
        Bundle bundle=new Bundle();
        bundle.putString("project", projectTitle);
        projectIssuesFragment.setArguments(bundle);
        adapter.addFragment(projectIssuesFragment, "ONE");
        adapter.addFragment(new BoardSelectionFragment(), "TWO");
        viewPager.setAdapter(adapter);
    }

}
