package com.studapps.shrikant.novamaterial;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shrikant on 7/1/2015.
 */
public class AdminLocalStore {

    public static final String SP_NAME = "AdminUserDetails";
    SharedPreferences sharedPreferences;

    AdminLocalStore(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, 0);
    }

    private void saveUser() {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
    }
}
