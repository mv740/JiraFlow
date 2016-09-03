package ca.michalwozniak.jiraflow.features.createIssue;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.CreateIssueMetaField;
import ca.michalwozniak.jiraflow.model.CreateIssueModel;
import ca.michalwozniak.jiraflow.model.EmptyResponse;
import ca.michalwozniak.jiraflow.model.Issue.Field;
import ca.michalwozniak.jiraflow.model.Issue.issueType;
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
    @BindView(R.id.fabCreateNewIssue)
    FloatingActionButton createNewIssueButton;
    @BindView(R.id.issueSummary)
    TextInputEditText summary;
    @BindView(R.id.issueReporter)
    TextInputEditText reporter;


    private int projectIndexSelected;
    private int issueTypeSelected;

    private SessionManager sessionManager;

    CreateIssueMetaField issueMetaFieldData;

    @OnFocusChange(R.id.input_project)
    public void onInputProjectFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            final NewIssueProjectSimpleListAdapter simpleListAdapter = new NewIssueProjectSimpleListAdapter((index, item) -> {
                project.setText(item.getName());
                projectIndexSelected = index;
                project.clearFocus();
                issueType.setEnabled(true);

            }, this.issueMetaFieldData.getProjects());

            new MaterialDialog.Builder(this)
                    .title("Select Project")
                    .adapter(simpleListAdapter, null)
                    .cancelListener(dialogInterface -> project.clearFocus())
                    .show();
        }
    }

    @OnFocusChange(R.id.input_issueType)
    public void onInputIssueTypeFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            final NewIssueTypeSimpleListAdapter simpleListAdapter = new NewIssueTypeSimpleListAdapter((index, item) -> {
                issueType.setText(item.getName());
                issueTypeSelected = index;
                issueType.clearFocus();

                //for now show here
                //Todo only show button when all required information for a minimal issue is available
                createNewIssueButton.show();
                summary.setEnabled(true);


            }, this.issueMetaFieldData.getProjects().get(projectIndexSelected).getIssuetypes());

            new MaterialDialog.Builder(this)
                    .title("Select issue type")
                    .adapter(simpleListAdapter, null)
                    .cancelListener(dialogInterface -> issueType.clearFocus())
                    .show();
        }

    }

    @OnClick(R.id.fabCreateNewIssue)
    public void newIssueClick() {
        createIssue();
        finish();
    }


    /**
     * Generate a issue base on selected options - Post Request
     */
    private void createIssue() {


        List<Project> projects = issueMetaFieldData.getProjects();

        CreateIssueModel issueModel = new CreateIssueModel();


        Field field = new Field();
        field.setSummary(summary.getText().toString());
        field.setDescription("Creating of an issue using project keys and issue type names using the REST API");
        Project project = new Project();
        project.setKey(projects.get(projectIndexSelected).getKey());
        issueType issueType = new issueType();
        issueType.setName(projects.get(projectIndexSelected).getIssuetypes().get(issueTypeSelected).getName());
        field.setProject(project);
        field.setIssuetype(issueType);

        issueModel.setFields(field);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        try {

            String json = mapper.writeValueAsString(issueModel);
            Log.e("JSON", json);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword(), sessionManager.getServerUrl());

        jiraService.createIssue(issueModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EmptyResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EmptyResponse response) {

                    }

                });
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

            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
        issueType.setEnabled(false);
        getIssueMetaFieldData();

    }

    private void getIssueMetaFieldData() {

        JiraSoftwareService jiraService = ServiceGenerator.createService(JiraSoftwareService.class, sessionManager.getUsername(), sessionManager.getPassword(), sessionManager.getServerUrl());

        jiraService.getCreateIssueMeta(null, null, null, null)
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

    private void getProjectIconType(CreateIssueMetaField issueMetaField) {

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
        Log.e("done", "done");

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }
}
