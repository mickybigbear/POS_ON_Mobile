package com.example.sin.projectone;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by naki_ on 7/7/2017.
 */

public class PaymentMethodManager extends AppCompatActivity {

    /**
     * KEY_PREFS ไว้สำหรับเป็น key ของ SharedPreferences
     */
    private final String KEY_PREFS = "com.example.sin.projectone_preferences";

    /**
     * ชื่อ key ที่ไว้เซฟ username ใน SharedPreferences
     */
    private final String KEY_ENABLE_PAYPAL = "paypal_status";
    private final String KEY_ENABLE_KTB = "kb_qrcode_status";
    private final String KEY_ENABLE_KBANK = "kbank_qrcode_status";




    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    /**
     * รับค่า Context เพื่อเอาไว้ใช้สำหรับ getSharedPreferences
     * @param context
     */
    public PaymentMethodManager(Context context) {
        mPrefs = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }


    public String getValue(String key, String type){
        String value = "";
        if(type=="boolean"){
            value = String.valueOf(mPrefs.getBoolean(key, false));
        }
        else if(type=="int"){
            value = String.valueOf(mPrefs.getInt(key, 0));
        }
        else{
            value = mPrefs.getString(key, "");
        }
        return value;
    }



    public boolean setValue(String key, String value, String type){
        if(type=="boolean"){
            Boolean varBool = Boolean.valueOf(value);
            mEditor.putBoolean(key, varBool);
        }
        else if(type=="int"){
            Integer varInt = Integer.valueOf(value);
            mEditor.putInt(key, varInt);
        }
        else{
            mEditor.putString(key, value);
        }

        return mEditor.commit();
    }



}