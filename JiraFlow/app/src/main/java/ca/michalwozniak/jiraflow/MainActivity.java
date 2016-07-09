package ca.michalwozniak.jiraflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.maksim88.passwordedittext.PasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.LoginService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import ca.michalwozniak.jiraflow.utility.PreferenceManager;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.input_username)
    TextInputEditText _username;
    @BindView(R.id.input_password)
    PasswordEditText _password;

    @OnClick(R.id.button)
    public void logIn() {
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


    private void connectJira(final String username, final String password) {

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
                        if (e.getMessage().contains("403")) {
                            Log.e("mainActivty", "Authentication fail : wrong password");
                            _password.setError("Wrong password");
                            Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
                        }
                        if(e.getMessage().contains("401"))
                        {
                            Log.e("mainActivty", "Authentication Failed");
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onNext(User user) {

                        PreferenceManager pm = PreferenceManager.getInstance(MainActivity.this);
                        pm.saveUsername(username);
                        pm.savePassword(password);

                        Intent startHome = new Intent(MainActivity.this, DashboardActivity.class);
                        startHome.putExtra("name", user.getName());
                        startHome.putExtra("email", user.getEmailAddress());
                        startActivity(startHome);
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
