package ca.michalwozniak.jiraflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import ca.michalwozniak.jiraflow.model.CreateIssueMetaField;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateIssueActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SessionManager sessionManager;

    CreateIssueMetaField  issueMetaFieldData;

    @OnFocusChange(R.id.input_project)
    public void actionOneditText(View v,boolean hasFocus)
    {
        if(hasFocus)
            Log.e("hey","project here");
    }
    @OnFocusChange(R.id.input_issueType)
    public void actionOneditText1(View v,boolean hasFocus)
    {
        if(hasFocus)
            Log.e("hey","issue type");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        ButterKnife.bind(this);
        sessionManager = SessionManager.getInstance(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Create Issue");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        getIssueMetaFieldData();
    }

    private void getIssueMetaFieldData() {

        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword());

        jiraService.getCreateIssueMeta(null,null,null,null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateIssueMetaField>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("errorSubscriber", e.getMessage());
                    }

                    @Override
                    public void onNext(CreateIssueMetaField issueMetaField) {

                        issueMetaFieldData = issueMetaField;

                        Log.e("CreateIssueMetaField", "WORK");
                       // generateIssueCards(userIssues.getIssues());
                    }
                });

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        finish();
    }
}
