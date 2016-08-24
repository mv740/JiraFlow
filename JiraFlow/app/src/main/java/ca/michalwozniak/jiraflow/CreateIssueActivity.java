package ca.michalwozniak.jiraflow;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import ca.michalwozniak.jiraflow.adapter.MaterialSimpleListAdapter;
import ca.michalwozniak.jiraflow.model.CreateIssueMetaField;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.service.JiraSoftwareService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.ResourceManager;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateIssueActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.input_issueType)
    TextInputEditText issueType;
    @BindView(R.id.input_project)
    TextInputEditText project;

    private int projectIndexSelected;

    private SessionManager sessionManager;

    CreateIssueMetaField  issueMetaFieldData;

    @OnFocusChange(R.id.input_project)
    public void actionOneditText(View v,boolean hasFocus)
    {
        if(hasFocus)
        {

            final MaterialSimpleListAdapter simpleListAdapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
                @Override
                public void onMaterialListItemSelected(int index, Project item) {
                    project.setText(item.getName());
                    projectIndexSelected = index;
                    issueType.setEnabled(true);

                }
            },this.issueMetaFieldData.getProjects());

            new MaterialDialog.Builder(this)
                    .title("title")
                    .adapter(simpleListAdapter,null)
                    .show();
        }
    }
    @OnFocusChange(R.id.input_issueType)
    public void actionOneditText1(View v,boolean hasFocus)
    {
        if(hasFocus) {
            Log.d("hey","issueType");
        }


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
        issueType.setEnabled(false);
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

                        getProjectIconType(issueMetaField);
                    }
                });

    }

    private void getProjectIconType(CreateIssueMetaField issueMetaField ) {

        this.issueMetaFieldData = issueMetaField;


        for (final Project project : this.issueMetaFieldData.getProjects()) {


            OkHttpClient httpClient = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder().url(project.getAvatarUrls().getExtraSmall()).build();

            okhttp3.Call call1 = httpClient.newCall(request);
            call1.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {

                }

                @Override
                public void onResponse(final okhttp3.Call call, okhttp3.Response response) throws IOException {

                    project.setImageType(ResourceManager.getImageType(response.headers().get("Content-type")));
                    response.body().close();
                }
            });
        }
        Log.e("done","done");

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        finish();
    }
}
