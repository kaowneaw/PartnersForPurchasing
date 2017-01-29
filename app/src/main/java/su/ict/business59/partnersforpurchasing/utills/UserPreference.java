package su.ict.business59.partnersforpurchasing.utills;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import su.ict.business59.partnersforpurchasing.models.Shop;

public class UserPreference {

    public static final String PREFERENCE_KEY = "pref_shop";
    public static final String STR_OBJ = "user";

    private SharedPreferences sh_pref;
    private SharedPreferences.Editor sh_edit;

    public UserPreference(Context context) {
        sh_pref = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        sh_edit = sh_pref.edit();
    }

    public UserPreference(Context context, Shop userObj) {
        this(context);
        setUserObject(userObj);
    }

//    public void registerUserPreference(String strObj) {
//        sh_edit.putString(STR_OBJ, strObj);
//    }

    public boolean commit() {
        return sh_edit.commit();
    }

    public boolean clearPreference() {
        sh_edit.clear();
        return sh_edit.commit();
    }

    public Shop getUserObject() {
        Gson gson = new Gson();
        String json = sh_pref.getString(STR_OBJ, "");
        return gson.fromJson(json, Shop.class);
    }

    public void setUserObject(Shop userObj) {
        Gson gson = new Gson();
        String json = gson.toJson(userObj);
        sh_edit.putString(STR_OBJ, json);
        commit();
    }

}
