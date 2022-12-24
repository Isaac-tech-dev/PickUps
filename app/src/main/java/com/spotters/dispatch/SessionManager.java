package com.spotters.dispatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private  static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public  static final String FIRSTNAME = "FIRSTNAME";
    public  static final String LASTNAME = "LASTNAME";
    public  static final String PHONE = "PHONE";
    public  static final String EMAIL = "EMAIL";
    public  static final String ID = "ID";
    public static final String SIGN_UP_DATE = "SIGN_UP_DATE";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor =sharedPreferences.edit();
    }

    public void createSession(String firstname, String lastname, String phone, String email, String id, String sign_up_date){
        editor.putBoolean(LOGIN, true);
        editor.putString(FIRSTNAME, firstname);
        editor.putString(LASTNAME, lastname);
        editor.putString(PHONE, phone);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.putString(SIGN_UP_DATE, sign_up_date);

        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLogin()){
            Intent i = new Intent(context, Login.class);
            context.startActivity(i);
            ((Dashboard) context).finish();
        }
    }

    public HashMap<String, String> getUserInfo(){
        HashMap<String, String> user = new HashMap<>();
        user.put(FIRSTNAME, sharedPreferences.getString(FIRSTNAME, null));
        user.put(LASTNAME, sharedPreferences.getString(LASTNAME, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(SIGN_UP_DATE, sharedPreferences.getString(SIGN_UP_DATE, null));

        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        context.startActivity(i);
        ((Dashboard) context).finish();
    }
}
