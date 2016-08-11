package ca.michalwozniak.jiraflow.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by michal on 4/29/2016.
 */
public class SessionManager {

    private static final String USER_PREFERENCE = "userPreference";
    private static final String NOT_FOUND = "notFound";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PROFILE_ICON_URL = "profileIconUrl";
    private static SessionManager instance = null;
    private Context context;

    private SessionManager(Context context) {
        this.context = context;
    }


    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public boolean doesProfileExist() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        String profile = sharedPref.getString(USERNAME, "empty");
        return !profile.contains("empty");
    }


    public void saveUsername(String username) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(USERNAME, username);
            editor.commit();
        }
    }

    public String getUsername() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return sharedPref.getString(USERNAME, NOT_FOUND);

        }
        return NOT_FOUND;
    }

    public void savePassword(String password) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PASSWORD, password);

            editor.commit();
        }
    }

    public String getPassword() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return sharedPref.getString(PASSWORD, NOT_FOUND);
        }
        return NOT_FOUND;
    }

    public void saveEmail(String email) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(EMAIL, email);
            editor.commit();
        }
    }

    public String getEmail() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return sharedPref.getString(EMAIL, NOT_FOUND);

        }
        return NOT_FOUND;
    }


    public void saveProfileIconUrl(String url) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PROFILE_ICON_URL, url);
            editor.commit();
        }
    }

    public String getProfileIconUrl() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return sharedPref.getString(PROFILE_ICON_URL, NOT_FOUND);

        }
        return NOT_FOUND;
    }

    public void deleteUser() {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
    }


}
