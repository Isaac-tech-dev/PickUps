package com.spotters.dispatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

public class SessionManage {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private  static final String PREF_NAME = "SHOW";
    private static final String LOGIN = "IS_LOGIN";
    public  static final String PASSWORD = "PASSWORD";

    public SessionManage(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor =sharedPreferences.edit();
    }

    public void createSession(String password, String lastname, String phone, String email, String id, String sign_up_date){
        editor.putBoolean(LOGIN, true);
        editor.putString(PASSWORD, password);

        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkPassword(){
        if(!this.isLogin()){
            Intent i = new Intent(context, Show_Password.class);
            context.startActivity(i);
            ((Dashboard) context).finish();
        }
    }

    public HashMap<String, String> getUserInfo(){
        HashMap<String, String> user = new HashMap<>();
        user.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));

        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        context.startActivity(i);
        ((Dashboard) context).finish();
    }

    public void createSession(String firstname, String lastname, String phone, String email, String id) {
    }
}
