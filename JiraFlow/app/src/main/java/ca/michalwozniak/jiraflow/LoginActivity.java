package ca.michalwozniak.jiraflow;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.maksim88.passwordedittext.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.michalwozniak.jiraflow.MVP.Login.LoginPresenterImpl;
import ca.michalwozniak.jiraflow.MVP.Login.LoginView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;
import top.wefor.circularanim.CircularAnimUtil;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.input_username)
    TextInputEditText mUsername;
    @BindView(R.id.input_layout_username)
    TextInputLayout mUsernameLayout;
    @BindView(R.id.input_password)
    PasswordEditText mPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout mPasswordLayout;
    @BindView(R.id.loginProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.loginButton)
    Button loginButton;


    private View loginView;

    LoginPresenterImpl presenter;

    @OnClick(R.id.loginButton)
    public void logIn(View view) {
        final String user = mUsername.getText().toString();
        final String pass = mPassword.getText().toString();

        hideKeyboard(view);

        loginView = view;

        loginButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        CircularAnimUtil.hide(loginButton);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.validateCredentials("mv740", "Q1w2e3r4");
                //presenter.validateCredentials(user, pass);
            }
        }, 1000);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        //pass your presenter a reference to your view
        if (presenter == null) {
            presenter = new LoginPresenterImpl(this);
        }

        setupInputObserver();
    }

    private void setupInputObserver() {
        Observable<Boolean> usernameObservable = RxTextView.textChanges(mUsername)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return (charSequence.length() != 0);
                    }
                })
                .distinctUntilChanged();

        Observable<Boolean> passwordObservable = RxTextView.textChanges(mPassword)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return (charSequence.length() != 0);
                    }
                })
                .distinctUntilChanged();

        Observable.combineLatest(
                usernameObservable,
                passwordObservable,
                new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean usernameValid, Boolean passwordValid) {
                        return (usernameValid && passwordValid);
                    }
                })
                .distinctUntilChanged()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean isValid) {
                        loginButton.setEnabled(isValid);
                    }
                });
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
        }, 2000);

    }

    @Override
    public void showProgressIndicator() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.loginButton.setVisibility(View.GONE);
    }

    @Override
    public void navigateToDashboard() {

        CircularAnimUtil.startActivity(LoginActivity.this, DashboardActivity.class, this.loginView, R.color.colorPrimary);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginButton.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        }, 1000);

    }

    @Override
    public void inputEmpty() {
        mUsername.setError("username required");
        mPassword.setError("password required");
    }

    @Override
    public void usernameEmpty() {
        mUsername.requestFocus();
        mUsername.setError("username required");
    }

    @Override
    public void passwordEmpty() {
        mPassword.requestFocus();
        mPassword.setError("username required");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
