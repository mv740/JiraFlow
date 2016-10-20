package ca.michalwozniak.jiraflow.features.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ca.michalwozniak.jiraflow.features.dashboard.DashboardActivity;
import ca.michalwozniak.jiraflow.features.login.LoginActivity;
import ca.michalwozniak.jiraflow.utility.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
        if(sessionManager.doesProfileExist())
        {
            intent = new Intent(this, DashboardActivity.class);
        }else{
            intent = new Intent(this,LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
