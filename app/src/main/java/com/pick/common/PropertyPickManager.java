package com.pick.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pick.PickApplication;

/**
 * Created by pyoinsoo on 2016-08-20.
 */
public class PropertyPickManager {
    private static PropertyPickManager instance;
    public static PropertyPickManager getPickInstance() {
        if (instance == null) {
            instance = new PropertyPickManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyPickManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(PickApplication.getItemContext());
        mEditor = mPrefs.edit();
    }

    //안드로이드에서 생성한 UUID
    public static final String UUID_KEY = "uuid_id";
    public void setUUID(String uuid) {
        mEditor.putString(UUID_KEY, uuid);
        mEditor.commit();
    }
    public String getUUID(){
        return mPrefs.getString(UUID_KEY, "");
    }

    //UUID를 가지고 서버에서 생성한 실제 user_id;
    public static final String KEY_USER_ID = "user_id";
    public void setUserID(String userID){
        mEditor.putString(KEY_USER_ID, userID);
        mEditor.commit();
    }
    public String getUserID(){
        return mPrefs.getString(KEY_USER_ID, "");
    }

    //현재 단말기 사용자가 개인 or 밴드인지를 식별
    public static final String KEY_BAND_OR_PERSONAL="band_or_person";
    public void setBandORPersonal(String bandPersonal){
        mEditor.putString(KEY_BAND_OR_PERSONAL, bandPersonal);
        mEditor.commit();
    }
    public String getKeyBandOrPersonal(){
        return mPrefs.getString(KEY_BAND_OR_PERSONAL, "");
    }

    public static final String KEY_OAUTH_EMAIL = "youtube_auth_email";
    //구글 Youtube OAuth인증을 위한 email저장
    public void setYoutubeEmail(String email){
        mEditor.putString(KEY_OAUTH_EMAIL, email);
        mEditor.commit();
    }
    public String getYoutubeEmail(){
        return mPrefs.getString(KEY_OAUTH_EMAIL,"");
    }
    public void deleteEmail(){
        mEditor.remove(KEY_OAUTH_EMAIL);
        mEditor.commit();
    }
}