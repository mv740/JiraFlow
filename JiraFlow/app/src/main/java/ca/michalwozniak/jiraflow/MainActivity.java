package ca.michalwozniak.jiraflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

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
    }

    private void connectJira(String username, final String password) {

        LoginService loginService = ServiceGenerator.createService(LoginService.class, username, password);
        Call<User> call = loginService.basicLogin();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful())
                {
                    Log.e("success", String.valueOf(response.raw()));
                    // Create the AccountHeader
                    AccountHeader headerResult = new AccountHeaderBuilder()
                            .withActivity(MainActivity.this)
                            .addProfiles(
                                    new ProfileDrawerItem()
                                            .withName("Mike Penz")
                                            .withEmail("mikepenz@gmail.com")
                            ).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                                @Override
                                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                    return false;
                                }
                            })
                            .build();

                    //Now create your drawer and pass the AccountHeader.Result
                    new DrawerBuilder()
                            .withActivity(MainActivity.this)
                            .withAccountHeader(headerResult)
                            //additional Drawer setup as shown above

                            .build();
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
