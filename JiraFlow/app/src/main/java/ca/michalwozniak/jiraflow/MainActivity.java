package ca.michalwozniak.jiraflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.LoginService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.input_username) EditText _username;
    @Bind(R.id.input_password) EditText _password;

    @OnClick(R.id.button) void logIn(){
        String user = _username.getText().toString();
        String pass = _password.getText().toString();

        if (isValid())
            connectJira(user,pass);

        Toast.makeText(this,"logIn", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        connectJira("mv740","Wozm__06");
    }

    private void connectJira(String username, final String password) {

        LoginService loginService = ServiceGenerator.createService(LoginService.class, username, password);
        Call<User> call = loginService.basicLogin();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful())
                {

                    Intent startHome = new Intent(MainActivity.this,HomeActivity.class);
                    startHome.putExtra("name",response.body().getName());
                    startHome.putExtra("email",response.body().getEmailAddress());
                    startActivity(startHome);

                }else
                {
                    _password.setError("Wrong password");
                    Log.e("failure", "failedLog");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("failure", String.valueOf(t.toString()));
            }
        });
    }

    private boolean isValid()
    {
        boolean valid = true;

        if(_password.getText().toString().isEmpty())
        {
            _password.setError("password is required!");
            valid = false;
        }
        if(_username.getText().toString().isEmpty())
        {
            _username.setError("username is required!");
            valid = false;
        }

        return valid;
    }

}
