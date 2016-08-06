package ca.michalwozniak.jiraflow;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.maksim88.passwordedittext.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.michalwozniak.jiraflow.MVP.Login.LoginPresenter;
import ca.michalwozniak.jiraflow.MVP.Login.LoginView;
import top.wefor.circularanim.CircularAnimUtil;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.input_username)
    TextInputEditText _username;
    @BindView(R.id.input_password)
    PasswordEditText _password;
    @BindView(R.id.loginProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.loginButton)
    Button loginButton;

    private View loginView;

    LoginPresenter presenter;

    @OnClick(R.id.loginButton)
    public void logIn(View view) {
        String user = _username.getText().toString();
        String pass = _password.getText().toString();

       hideKeyboard(view);

        loginView = view;
        if(presenter.validateInput(user,pass))
        {
            loginButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            CircularAnimUtil.hide(loginButton);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.login("mv740", "Q1w2e3r4");
                }
            },1000);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        //pass your presenter a reference to your view
        if(presenter == null)
        {
            presenter = new LoginPresenter();
        }
        presenter.attachView(this);

        //for testing
        //presenter.login("mv740", "Q1w2e3r4");

    }

    @Override
    public void loginFailed(final String errorMessage) {
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
               loginButton.setEnabled(true);
               progressBar.setVisibility(View.GONE);
               CircularAnimUtil.show(loginButton);
           }
       },2000);

    }

    @Override
    public void showProgressIndicator() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.loginButton.setVisibility(View.GONE);
    }

    @Override
    public void navigateToDashboard() {

        //Intent startHome = new Intent(this, DashboardActivity.class);
        //CircularAnimUtil.startActivity(LoginActivity.this, DashboardActivity.class, this.loginView, R.color.colorPrimary);
        CircularAnimUtil.startActivity(LoginActivity.this, DashboardActivity.class, this.loginView, R.color.colorPrimary);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               loginButton.setEnabled(true);
               progressBar.setVisibility(View.GONE);
               loginButton.setVisibility(View.VISIBLE);
           }
       },1000);
        //startActivity(startHome);

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

    public void hideKeyboard(View view)
    {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
