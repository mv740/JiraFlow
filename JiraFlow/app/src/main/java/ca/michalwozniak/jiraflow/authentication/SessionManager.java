package ca.michalwozniak.jiraflow.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import ca.michalwozniak.jiraflow.model.User;

/**
 * Created by michal on 4/16/2016.
 */
public class SessionManager {

    private Context context;
    private User session;

    public SessionManager(Context context)
    {
        this.context = context;
    }

    public User getSession()
    {
        if(session == null)
        {
            load();
        }
        return session;
    }

    public void load()
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String json = sharedpreferences.getString("session","");
        ObjectMapper mapper = new ObjectMapper();
        try {
            session = mapper.readValue(json,User.class);
        } catch (IOException e) {
            Log.e("JsonHelper", Log.getStackTraceString(e));
        }
    }

    public void save(User user)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedpreferences.edit();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(user);
            editor.putString("session",json);
            editor.apply();
        } catch (JsonProcessingException e) {
            Log.e("JsonHelper", Log.getStackTraceString(e));
        }
    }


}
