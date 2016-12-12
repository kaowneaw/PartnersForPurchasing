package su.ict.business59.partnersforpurchasing.utills;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kaowneaw on 12/4/2016.
 */

public class UserPreference {

    public static final String PREFERENCE_KEY = "pref_shop";
    public static final String USER_ID_KEY = "user_id";
    public static final String USERNAME_KEY = "username";
    public static final String IMG_KEY = "img";
    public static final String EMAIL_KEY = "email";
    public static final String ROLE_KEY = "role";

    private SharedPreferences sh_pref;
    private SharedPreferences.Editor sh_edit;

    public UserPreference(Context context) {

        sh_pref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        sh_edit = sh_pref.edit();
    }

    public UserPreference(Context context, String userid, String username, String email, String img, String role) {
        this(context);
        registerUserPreference(userid, username, email, img, role);
    }

    public void registerUserPreference(String userid, String username, String email, String img, String role) {
        sh_edit.putString(USER_ID_KEY, userid);
        sh_edit.putString(USERNAME_KEY, username);
        sh_edit.putString(EMAIL_KEY, email);
        sh_edit.putString(IMG_KEY, img);
        sh_edit.putString(ROLE_KEY, role);
    }

    public boolean commit() {
        return sh_edit.commit();
    }

    public boolean clearPreference() {
        sh_edit.clear();
        return sh_edit.commit();
    }

    public String getUserID() {
        return sh_pref.getString(USER_ID_KEY, "NULL");
    }

    public String getUsername() {
        return sh_pref.getString(USERNAME_KEY, "NULL");
    }

    public String getEmail() {
        return sh_pref.getString(EMAIL_KEY, "NULL");
    }

    public String getImg() {
        return sh_pref.getString(IMG_KEY, "NULL");
    }

    public String getRoleKey() {
        return sh_pref.getString(ROLE_KEY, "NULL");
    }
}
