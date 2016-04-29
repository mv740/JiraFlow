package ca.michalwozniak.jiraflow.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by michal on 4/29/2016.
 */
public class PreferenceManager {

    public static final String USER_PREFERENCE = "userPreference";
    public static final String NOT_FOUND = "notFound";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    private PreferenceManager instance = null;
    private Context context;

    private PreferenceManager(Context context)
    {
        this.context = context;
    }

    public PreferenceManager getInstance(Context context)
    {
        if(instance !=null)
        {
            instance = new PreferenceManager(context);
        }
        return instance;
    }


    public void saveUsername(String username)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if(sharedPref!=null)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(USERNAME, username);
            editor.commit();
        }
    }

    public String getUsername()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE,Context.MODE_PRIVATE);
        if(sharedPref!=null)
        {
            return sharedPref.getString(USERNAME, NOT_FOUND);

        }
        return NOT_FOUND;
    }

    public void savePassword(String password)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE,Context.MODE_PRIVATE);
        if(sharedPref !=null)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PASSWORD, password);

            editor.commit();
        }
    }

    public String getPassword()
    {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE,Context.MODE_PRIVATE);
        if(sharedPref!=null)
        {
            return sharedPref.getString(PASSWORD, NOT_FOUND);
        }
        return NOT_FOUND;
    }

    public void deleteUser()
    {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USERNAME).commit();
        editor.remove(PASSWORD).commit();
    }





}
