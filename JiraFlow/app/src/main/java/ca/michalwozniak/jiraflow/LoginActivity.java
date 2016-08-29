package ca.michalwozniak.jiraflow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.michalwozniak.jiraflow.MVP.Login.LoginPresenterImpl;
import ca.michalwozniak.jiraflow.MVP.Login.LoginView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func3;
import top.wefor.circularanim.CircularAnim;

/**
 * Created by Michal Wozniak on 8/8/2016.
 */

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.input_username)
    TextInputEditText mUsername;
    @BindView(R.id.input_layout_username)
    TextInputLayout mUsernameLayout;
    @BindView(R.id.input_password)
    TextInputEditText mPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout mPasswordLayout;
    @BindView(R.id.loginProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.checkBoxRememberMe)
    CheckBox rememberMe;
    @BindView(R.id.input_serverUrl)
    TextInputEditText serverUrl;
    @BindView(R.id.spinnerHttpType)
    Spinner spinnerHttpType;
    @BindView(R.id.loginLayout)
    RelativeLayout snackbarMessagePosition;


    private View loginView;

    LoginPresenterImpl presenter;

    @OnClick(R.id.loginButton)
    public void logIn(View view) {
        final String user = mUsername.getText().toString();
        final String pass = mPassword.getText().toString();
        final String protocol = spinnerHttpType.getSelectedItem().toString();
        final String url = serverUrl.getText().toString();
        final boolean isChecked = rememberMe.isChecked();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();


        final String jiraUrl = protocol.concat(url);
        hideKeyboard(view);

        loginView = view;
        loginButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        CircularAnim.hide(loginButton).go();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Patterns.WEB_URL.matcher(url).matches() || Patterns.IP_ADDRESS.matcher(url).matches()) {
                    presenter.validateCredentials(user, pass, jiraUrl, isChecked);
                } else {
                    invalidUrl();

                }

            }
        }, 1000);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //testing
        mUsername.setText("mv740");
        mPassword.setText("Q1w2e3r4");
        serverUrl.setText("173.176.41.65:8000");


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.http_protocols));
        spinnerHttpType.setAdapter(spinnerArrayAdapter);

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

        Observable<Boolean> serverUrlObservable = RxTextView.textChanges(serverUrl)
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
                serverUrlObservable,
                new Func3<Boolean, Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean usernameValid, Boolean passwordValid, Boolean serverUrlValid) {
                        return (usernameValid && passwordValid && serverUrlValid);
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
    public void showProgressIndicator() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.loginButton.setVisibility(View.GONE);
    }

    @Override
    public void navigateToDashboard() {

        CircularAnim.fullActivity(LoginActivity.this, this.loginView)
                .colorOrImageRes(R.color.cardview_light_background)
                .duration(500)
                .go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        loginButton.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public void timeout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showErrorSnackBar("Failed To connect to server!");
            }
        }, 2000);
    }

    @Override
    public void connectionError(String errorMsg) {
        showErrorSnackBar(errorMsg);
    }

    @Override
    public void loginFailed(final String errorMessage) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showErrorSnackBar(errorMessage);
                resetLoginButton();
            }
        }, 2000);

    }

    private void resetLoginButton() {
        loginButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        CircularAnim.show(loginButton).go();
    }

    /**
     * Snackbar error notification
     */
    private void invalidUrl() {
        showErrorSnackBar("Invalid Url");
    }


    public void showErrorSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(snackbarMessagePosition, message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(ContextCompat.getColor(LoginActivity.this,R.color.md_red_900));
        TextView textView = ButterKnife.findById(view, android.support.design.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.show();
        resetLoginButton();
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
