package ca.michalwozniak.jiraflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.maksim88.passwordedittext.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.michalwozniak.jiraflow.MVP.Login.ILoginView;
import ca.michalwozniak.jiraflow.MVP.Login.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    @BindView(R.id.input_username)
    TextInputEditText _username;
    @BindView(R.id.input_password)
    PasswordEditText _password;
    @BindView(R.id.loginProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.button)
    Button button;

    LoginPresenter presenter;

    @OnClick(R.id.button)
    public void logIn() {
        String user = _username.getText().toString();
        String pass = _password.getText().toString();

        if (isValid())
        {
            presenter.authenticateProcess(user,pass);
        }

        Toast.makeText(this, "logIn", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE
        );
        //pass your presenter a reference to your view
        if(presenter == null)
        {
            presenter = new LoginPresenter();
        }
        presenter.attachView(this);

        //for testing
        presenter.authenticateProcess("mv740", "Q1w2e3r4");
        //testFeed();

    }



    private boolean isValid() {
        boolean valid = true;

        if (_password.getText().toString().isEmpty()) {
            _password.setError("password is required!");
            valid = false;
        }
        if (_username.getText().toString().isEmpty()) {
            _username.setError("username is required!");
            valid = false;
        }

        return valid;
    }

    @Override
    public void loginFailed(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);

    }

    @Override
    public void showProgressIndicator() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.button.setVisibility(View.GONE);
    }

    @Override
    public void navigateToDasboard() {

        progressBar.setVisibility(View.GONE);
        Intent startHome = new Intent(this, DashboardActivity.class);
        startActivity(startHome);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
