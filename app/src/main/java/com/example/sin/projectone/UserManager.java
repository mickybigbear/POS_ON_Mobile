package com.example.sin.projectone;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by naki_ on 7/7/2017.
 */

public class UserManager extends AppCompatActivity {

    /**
     * KEY_PREFS ไว้สำหรับเป็น key ของ SharedPreferences
     */
    private final String KEY_PREFS = "prefs_user";

    /**
     * ชื่อ key ที่ไว้เซฟ username ใน SharedPreferences
     */
    private final String KEY_USERNAME = "username";

    /**
     * ชื่อ key ที่ไว้เซฟ password ใน SharedPreferences.
     */
    private final String KEY_PASSWORD = "password";
    private final String KEY_SHOP_ID = "shop_id";
    private final String KEY_USER_ID = "user_id";
    private final String KEY_TYPE = "type";



    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    /**
     * รับค่า Context เพื่อเอาไว้ใช้สำหรับ getSharedPreferences
     * @param context
     */
    public UserManager(Context context) {
        mPrefs = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    /**
     * ทำการเช็ค Username กับ Password ใน SharedPreferences<br />
     * โดยเงื่อนไข EditText ของ Username และ password ต้องไม่เป็นค่าว่าง <br />
     * และค่าที่ได้ ต้องตรงกับใน SharedPreferences
     * @param username - username จาก EditText ที่ใส่
     * @param password - password จาก EditText ที่ใส่
     * @return หากใส่ข้อมูล ให้ส่งค่ากลับเป็น true, ถ้าใส่ผิดก็ส่ง false
     */
    public boolean checkLoginValidate(String username, String password) {
        String realUsername = mPrefs.getString(KEY_USERNAME, "");
        String realPassword = mPrefs.getString(KEY_PASSWORD, "");

        if ( username.equals(realUsername) && password.equals(realPassword)) {
            return true;
        }
        return false;
    }

    public boolean checkSession(){
        String username = mPrefs.getString(KEY_USERNAME, "");
        String shopId = mPrefs.getString(KEY_SHOP_ID, "");
        String userId = mPrefs.getString(KEY_USER_ID, "");
        if(username.equals("") || shopId.equals("") || userId.equals("")){
            return false;
        }
            return true;
    }

    /**
     * เมธอดสำหรับลงทะเบียนสมาชิกใหม่ โดยส่งค่า username และ password มา<br />
     * จากนั้นจะเซฟลง SharedPreferences
     * @param username - username จาก EditText ที่ใส่
     * @return ส่งค่ากลับไปเป็น false หากลงทะเบียนไม่สำเร็จ <br />
     * เป็น true หากลงทะเบียนสำเร็จ
     */
    public boolean saveSession(String username, String shopId, String userId, String type) {

        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_USER_ID, userId);
        mEditor.putString(KEY_SHOP_ID, shopId);
        mEditor.putString(KEY_TYPE, type);
        Log.d(TAG, "saveSession: Done");
        return mEditor.commit();
    }

    public boolean clearSession(){

        return mEditor.clear().commit();

    }

    public String getValue(String KEY){
        return mPrefs.getString(KEY, "");
    }

    public String getShopId(){
        // ชั่วคราว
        String shopId = mPrefs.getString(KEY_SHOP_ID,"");
        if(!shopId.equals("")){
            return shopId;
        }
        return String.valueOf(Constant.SHOP_ID);
    }
    public String getUserId(){
        // ชั่วคราว
        String userId = mPrefs.getString(KEY_USER_ID,"");
        if(!userId.equals("")){
            return userId;
        }
        return String.valueOf(Constant.USER_ID);
    }

    public String getUserType(){
        String type = mPrefs.getString(KEY_TYPE,"");
        if(!type.equals("")){
            return type;
        }
        return String.valueOf(Constant.USER_ID);
    }

}