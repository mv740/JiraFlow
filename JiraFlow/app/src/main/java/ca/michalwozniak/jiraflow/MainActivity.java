package ca.michalwozniak.jiraflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.LoginService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.input_username) EditText _username;
    @BindView(R.id.input_password) EditText _password;

    @OnClick(R.id.button)
    void logIn() {
        String user = _username.getText().toString();
        String pass = _password.getText().toString();

        if (isValid())
            connectJira(user, pass);

        Toast.makeText(this, "logIn", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //for testing
        connectJira("test", "Q1w2e3r4");
        //testFeed();

    }


    private void connectJira(String username, String password) {

        LoginService loginService = ServiceGenerator.createService(LoginService.class, username, password);
        loginService.basicLogin()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        //do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("mainActivty", e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        if (user != null) {

                            //Intent startHome = new Intent(MainActivity.this, HomeActivity.class);
                           Intent startHome = new Intent(MainActivity.this, DashboardActivity.class);
                            startHome.putExtra("name", user.getName());
                            startHome.putExtra("email", user.getEmailAddress());
                            startActivity(startHome);

                        } else {
                            _password.setError("Wrong password");
                            Log.e("failure", "failedLog");
                        }
                    }
                });

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

}
