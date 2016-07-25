package ca.michalwozniak.jiraflow;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.michalwozniak.jiraflow.adapter.ViewPagerAdapter;
import ca.michalwozniak.jiraflow.fragment.ProjectIssuesFragment;
import ca.michalwozniak.jiraflow.fragment.Two;

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

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
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
        adapter.addFragment(new Two(), "TWO");
        viewPager.setAdapter(adapter);
    }

}
