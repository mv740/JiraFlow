package ca.michalwozniak.jiraflow.features.createIssue;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import ca.michalwozniak.jiraflow.R;
import ca.michalwozniak.jiraflow.model.CreateIssueMetaField;
import ca.michalwozniak.jiraflow.model.CreateIssueModel;
import ca.michalwozniak.jiraflow.model.Issue.Field;
import ca.michalwozniak.jiraflow.model.Issue.issueType;
import ca.michalwozniak.jiraflow.model.Project;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.utility.LogManager;
import ca.michalwozniak.jiraflow.utility.NetworkManager;
import ca.michalwozniak.jiraflow.utility.SessionManager;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

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
    @BindView(R.id.issueDescription)
    TextInputEditText description;

    private List<User> reporterSearchResult;
    private int projectIndexSelected;
    private int issueTypeSelected;
    private SessionManager sessionManager;
    private CreateIssueMetaField issueMetaFieldData;
    private NetworkManager networkManager;
    private CompositeSubscription subscriptions = new CompositeSubscription();

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

        networkManager = NetworkManager.getInstance(getApplicationContext());
        createNewIssueButton.setVisibility(View.INVISIBLE);
        reporterSearchResult = new ArrayList<>();
        issueType.setEnabled(false);
        setupCreateIssueButtonObserver();
        getIssueMetaFieldData();

    }

    private void getIssueMetaFieldData() {

        subscriptions.add(networkManager.getCreateIssueMeta()
                .subscribe(issueMetaField -> {

                    this.issueMetaFieldData = issueMetaField;

                    for (final Project project : this.issueMetaFieldData.getProjects()) {

                        networkManager.getProjectIconType(project);
                    }
                }));

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

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
                summary.setEnabled(true);
                description.setEnabled(true);
                reporter.setEnabled(true);
                reporter.setText(sessionManager.getUsername()); // set default to yourself

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

    @OnFocusChange(R.id.issueReporter)
    public void onIssueReporterFocusChange(View v, boolean hasFocus) {

        if (hasFocus) {
            final UserSimpleListAdapter simpleListAdapter = new UserSimpleListAdapter((index, user) -> reporter.setText(user.getKey()), reporterSearchResult);


            MaterialDialog searchDialog = new MaterialDialog.Builder(this)
                    .customView(R.layout.search_view, false)
                    .cancelListener(dialogInterface -> project.clearFocus())
                    .build();


            ImageButton button = (ImageButton) searchDialog.findViewById(R.id.search_view_searchAction);
            RecyclerView recyclerView = (RecyclerView) searchDialog.findViewById(R.id.search_view_result);
            simpleListAdapter.setDialog(searchDialog);
            recyclerView.setAdapter(simpleListAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(searchDialog.getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setHasFixedSize(true);
            simpleListAdapter.clear();

            button.setOnClickListener(v1 -> {

                EditText searching = ButterKnife.findById(searchDialog, R.id.search_view_text);
                String username = searching.getText().toString();
                String projectKey = issueMetaFieldData.getProjects().get(projectIndexSelected).getKey();

                subscriptions.add(networkManager.findAssignableUsers(username, projectKey)
                        .subscribe(userList -> {
                            simpleListAdapter.clear();
                            for (User user : userList) {
                                simpleListAdapter.add(user);
                            }
                        }));
            });

            searchDialog.show();

        }
    }

    /**
     * Generate a issue base on selected options - Post Request
     */
    private void createIssue() {

        List<Project> projects = issueMetaFieldData.getProjects();
        CreateIssueModel issueModel = new CreateIssueModel();

        Field field = new Field();
        if (!summary.getText().toString().isEmpty()) {
            field.setSummary(summary.getText().toString());
        }

        field.setDescription("Creating of an issue using project keys and issue type names using the REST API");
        Project project = new Project();
        project.setKey(projects.get(projectIndexSelected).getKey());
        ca.michalwozniak.jiraflow.model.Issue.issueType issueType = new issueType();
        issueType.setName(projects.get(projectIndexSelected).getIssuetypes().get(issueTypeSelected).getName());
        field.setProject(project);
        field.setIssuetype(issueType);

        field.setDescription(description.getText().toString());

        //reporter
        User userReporter = new User();
        userReporter.setName(reporter.getText().toString());
        field.setReporter(userReporter);

        issueModel.setFields(field);

        LogManager.displayJSON("createIssue", issueModel);

        subscriptions.add(networkManager.createIssue(issueModel).subscribe(emptyResponse -> {
        }));
    }

    private void setupCreateIssueButtonObserver() {
        Observable<Boolean> summaryObservable = RxTextView.textChanges(summary)
                .map(charSequence -> (charSequence.length() != 0))
                .distinctUntilChanged();

        Observable<Boolean> reporterObservable = RxTextView.textChanges(reporter)
                .map(charSequence -> (charSequence.length() != 0))
                .distinctUntilChanged();

        subscriptions.add(Observable.combineLatest(
                summaryObservable,
                reporterObservable,
                (summaryValid, reporterValid) -> (summaryValid && reporterValid))
                .distinctUntilChanged()
                .subscribe(isValid -> {
                    createNewIssueButton.setVisibility(isValid ? View.VISIBLE : View.INVISIBLE);
                }));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
