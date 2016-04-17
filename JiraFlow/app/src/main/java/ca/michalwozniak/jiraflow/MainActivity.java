package ca.michalwozniak.jiraflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import ca.michalwozniak.jiraflow.model.User;
import ca.michalwozniak.jiraflow.service.LoginService;
import ca.michalwozniak.jiraflow.service.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectJira();
    }

    private void connectJira() {

        LoginService loginService = ServiceGenerator.createService(LoginService.class, "mv740", "Wozm__06");
        Call<User> call = loginService.basicLogin();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
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
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("failure", String.valueOf(t.toString()));
            }
        });
    }
}
