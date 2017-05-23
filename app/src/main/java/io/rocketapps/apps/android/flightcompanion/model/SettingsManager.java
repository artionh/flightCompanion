package io.rocketapps.apps.android.flightcompanion.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsManager {


    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_TOKEN = "key_tokenn";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_SURNAME = "key_surname";
    private static final String KEY_IMAGE = "key_image";
    private static final String KEY_URL_FEED = "key_url_feed";
    private static final String KEY_URL_NOTIFICATION= "key_url_notification";
    private static final String KEY_URL_PROFILE= "key_url_profile";
    private static final String START_SERVICE_KEY = "start_service_key";
    private static final String KEY_URL_ACCOUNT = "key_url_account";
    private static final String KEY_URL_IMAGE_DIRECTORY = "key_url_imagedirecotry";
    private static final String KEY_URL_IMAGE_PATTERN = "key_url_patterndirecotry";
    private static final String KEY_URL_CREATE_QUOTE = "key_url_createquote";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_LOGIN_TYPE = "key_logintype";
    private static final String KEY_LAST_NOTIFICATION_MORNING = "key_last_morning_notif";
    SharedPreferences mManager;


    public SettingsManager(Context mcontext) {
        mManager = PreferenceManager.getDefaultSharedPreferences(mcontext);
    }

    public int getUserId()
    {
        return mManager.getInt(KEY_USER_ID, -1);
    }

    public void setUserId(int i)
    {
        mManager.edit().putInt(KEY_USER_ID, i).apply();
    }

    public String getToken() {
        return mManager.getString(KEY_TOKEN,"");
    }

    public void setToken(String s){
        mManager.edit().putString(KEY_TOKEN, s).apply();
    }

    public String getEmail() {
        return mManager.getString(KEY_EMAIL,"");
    }

    public void setEmail(String s){
        mManager.edit().putString(KEY_EMAIL, s).apply();
    }
    public String getName() {
        return mManager.getString(KEY_NAME,"").length() > 2 ? mManager.getString(KEY_NAME,"").substring(0, 1).toUpperCase() + mManager.getString(KEY_NAME,"").substring(1) :  mManager.getString(KEY_NAME,"");
    }

    public void setName(String s){
        mManager.edit().putString(KEY_NAME, s).apply();
    }

    public String getSurname() {
        return mManager.getString(KEY_SURNAME,"").length() > 2 ? mManager.getString(KEY_SURNAME,"").substring(0, 1).toUpperCase() + mManager.getString(KEY_SURNAME,"").substring(1) :  mManager.getString(KEY_SURNAME,"");
    }

    public void setSurname(String s){
        mManager.edit().putString(KEY_SURNAME, s).apply();
    }

    public String getImage() {
        return mManager.getString(KEY_IMAGE,"");
    }

    public void setImage(String s){
        mManager.edit().putString(KEY_IMAGE, s).apply();
    }

    public String getFeedUrl() {
        return mManager.getString(KEY_URL_FEED,"");
    }

    public void setFeedUrl(String s){
        mManager.edit().putString(KEY_URL_FEED, s).apply();
    }

    public String getNotificationUrl() {
        return mManager.getString(KEY_URL_NOTIFICATION,"");
    }

    public void setNotificationUrl(String s){
        mManager.edit().putString(KEY_URL_NOTIFICATION, s).apply();
    }

    public void setupUser(JSONObject o) throws JSONException {
        setToken(o.getJSONObject("data").getString("token"));
        setEmail(o.getJSONObject("data").getString("email"));
        setUsername(o.getJSONObject("data").getString("username"));
        setLoginType(o.getJSONObject("data").getString("login_type"));
        setUserId(o.getJSONObject("data").getInt("id"));
        setName(o.getJSONObject("data").getString("name"));
        setSurname(o.getJSONObject("data").getString("surname"));
        setImage(o.getJSONObject("data").getString("image256"));
        setFeedUrl(o.getJSONObject("data").getJSONObject("urls").getString("feed"));
        setNotificationUrl(o.getJSONObject("data").getJSONObject("urls").getString("notification"));
        setAccountUrl(o.getJSONObject("data").getJSONObject("urls").getString("account"));
        setImageDirectoryUrl(o.getJSONObject("data").getJSONObject("urls").getString("imagedirectory"));
        setCreateQuoteUrl(o.getJSONObject("data").getJSONObject("urls").getString("createquote"));
        setPatternDirectoryUrl(o.getJSONObject("data").getJSONObject("urls").getString("patterndirectory"));
    }

    public static boolean isLogged(Context applicationContext) {

        return new SettingsManager(applicationContext).getUserId() != -1;
    }

    public void logout() {
        setName("");
        setEmail("");
        setToken("");
        setUserId(-1);
        setImage("");
        setSurname("");
        setFeedUrl("");
        setUsername("");
        setLoginType("");
        setImageDirectoryUrl("");
        setNotificationUrl("");
        setCreateQuoteUrl("");
    }


    public static void startService(Context mContext)
    {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean(SettingsManager.START_SERVICE_KEY, true).commit();
    }

    public static boolean hasStartedService(Context mContext)
    {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(SettingsManager.START_SERVICE_KEY, false);
    }

    public static void cancleService(Context mContext)
    {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean(SettingsManager.START_SERVICE_KEY, false).commit();
    }

    public String getAccountUrl() {
        return mManager.getString(KEY_URL_ACCOUNT,"");
    }
    public void setAccountUrl(String s) {
        mManager.edit().putString(KEY_URL_ACCOUNT,s).apply();
    }

    public String getImageDirectoryUrl() {
        return mManager.getString(KEY_URL_IMAGE_DIRECTORY,"") + "/";
    }
    public void setImageDirectoryUrl(String s) {
        mManager.edit().putString(KEY_URL_IMAGE_DIRECTORY,s).apply();
    }

    public String getPatternDirectoryUrl() {
        return mManager.getString(KEY_URL_IMAGE_PATTERN,"") + "/";
    }
    public void setPatternDirectoryUrl(String s) {
        mManager.edit().putString(KEY_URL_IMAGE_PATTERN,s).apply();
    }

    public String getCreateQuoteUrl() {
        return mManager.getString(KEY_URL_CREATE_QUOTE,"");
    }
    public void setCreateQuoteUrl(String s) {
        mManager.edit().putString(KEY_URL_CREATE_QUOTE,s).apply();
    }

    public String getUsername() {
        return mManager.getString(KEY_USERNAME,"");
    }
    public void setUsername(String s) {
        mManager.edit().putString(KEY_USERNAME,s).apply();
    }


    public String getLoginType() {
        return mManager.getString(KEY_LOGIN_TYPE,"");
    }
    public void setLoginType(String s) {
        mManager.edit().putString(KEY_LOGIN_TYPE,s).apply();
    }

    public long getLastNotification() {
        return mManager.getLong(KEY_LAST_NOTIFICATION_MORNING, 1477291800);
    }
    public void setLastNotification(long s) {
        mManager.edit().putLong(KEY_LAST_NOTIFICATION_MORNING,s).apply();
    }


}
