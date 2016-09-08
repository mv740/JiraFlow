package ca.michalwozniak.jiraflow.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by michal on 4/29/2016.
 */
public class SessionManager {

    private static final String USER_PREFERENCE = "userPreference";
    private static final String NOT_FOUND = "notFound";
    private static final int NOT_FOUND_INT = -1;
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PROFILE_ICON_URL = "profileIconUrl";
    private static final String REMEMBER_ME = "rememberMe";
    private static final String SERVER_URL = "JiraServerURL";
    private static final String BOARD_ID = "BOARD_ID";
    private static final String SPRINT_ID = "SPRINT_ID";
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

    public void saveUsername(String username) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(USERNAME, username);
            editor.apply();
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

            editor.apply();
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
            editor.apply();
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
            editor.apply();
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
        editor.apply();
    }

    public boolean doesProfileExist() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(REMEMBER_ME, false);
    }

    public void rememberProfile() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(REMEMBER_ME, true);
            editor.apply();
        }
    }

    public void saveServerUrl(String url) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SERVER_URL, url);
            editor.apply();
        }
    }

    public String getServerUrl() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return sharedPref.getString(SERVER_URL, NOT_FOUND);

        }
        return NOT_FOUND;
    }

    public void saveFavoriteBoardId(int boardId) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(BOARD_ID, boardId);
            editor.apply();
        }
    }

    public int getFavoriteBoardId() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return sharedPref.getInt(BOARD_ID, NOT_FOUND_INT);

        }
        return NOT_FOUND_INT;
    }

    public void saveFavoriteSprintId(int boardId) {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(SPRINT_ID, boardId);
            editor.apply();
        }
    }

    public int getFavoriteSprintId() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return sharedPref.getInt(SPRINT_ID, NOT_FOUND_INT);

        }
        return NOT_FOUND_INT;
    }

    public boolean hasFavoriteBoard() {
        SharedPreferences sharedPref = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref != null) {
            return NOT_FOUND_INT != sharedPref.getInt(BOARD_ID, NOT_FOUND_INT);

        }
        return false;
    }
}
