package com.example.sin.projectone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterStoreActivity extends AppCompatActivity {

    private static boolean flagCountDownExit = false;
    private static int backPressCount = 0;
    private EditText shopNameText;
    private EditText shopPassText;
    private Button regisStoreButt;
    private RadioButton radioEmp;
    private RadioButton  radioOwn;
    private RadioGroup radioGroup;
    private String shopName ="", shopPass="", userName="", roleValue="";
    ProgressDialog progress;
    final int duration = Toast.LENGTH_SHORT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);
        shopNameText = (EditText) findViewById(R.id.register_store_store_name);
        shopPassText = (EditText) findViewById(R.id.register_store_store_pass);
        radioGroup = (RadioGroup) findViewById(R.id.register_store_radio);
        radioEmp = (RadioButton) findViewById(R.id.register_store_radio_emp);
        radioOwn = (RadioButton) findViewById(R.id.register_store_radio_owner);
        regisStoreButt = (Button) findViewById(R.id.register_store_store_button);
        radioGroup.check(R.id.register_store_radio_owner);


        regisStoreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRegis();
            }
        });
    }
    private void submitRegis(){
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        shopName = shopNameText.getText().toString();
        shopPass = shopPassText.getText().toString();
        Log.d("text", shopName);
        Log.d("text", shopPass);
        if(radioEmp.isChecked()){
            roleValue= "employee";
        }
        if(radioOwn.isChecked()){
            roleValue= "owner";
        }
        if(TextUtils.isEmpty(shopName)){
            shopNameText.setError("Request this field");
            Log.d("text", shopName);
            Toast toast = Toast.makeText(this, "Please fill all fields", duration);
            toast.show();
            return;
        }
        if(TextUtils.isEmpty(shopPass)){
            shopPassText.setError("Request this field");
            Log.d("text", shopPass);
            Toast toast = Toast.makeText(this, "Please fill all fields", duration);
            toast.show();
            return;
        }
        registerProcess();
    }
    private void registerProcess(){
        progress = ProgressDialog.show(this, "Loading",
                "Please wait ...", true);
        RequestParams params = new RequestParams();
        params.put("userName",userName);
        params.put("shopName",shopName);
        params.put("shopPass",shopPass);
        params.put("roleValue",roleValue);
        Log.d("params", params.toString());
        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_SHORT;
        HttpUtilsAsync.post(Constant.URL_SERVER+"/api/shop/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.dismiss();
                try {
                    System.out.println(response);
                    String res = (String) response.get("Message");
                    if(res.equals("wrong password or shop not exist !")){
                        Toast toast = Toast.makeText(getBaseContext(), res, duration);
                        toast.show();
                    }
                    else if(res.equals("Please contact your real owner to become owner!")){
                        Toast toast = Toast.makeText(getBaseContext(), res, duration);
                        toast.show();
                    }
                    else{
                        String userID = response.get("userID").toString();
                        String shopID = response.get("shopID").toString();
                        CharSequence text = res;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        System.out.println(userID);
                        System.out.println("======================================");
                        System.out.println(shopID);
                        Log.d("userID", userID);
                        Log.d("shopID", shopID);
                        Constant.SHOP_ID = Integer.valueOf(shopID);
                        Constant.USER_ID = Integer.valueOf(userID);
                        Intent mainIntent = new Intent(getApplicationContext(),MainNav.class);
                        startActivity(mainIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse + " " + statusCode);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("Failed: "+ ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
//        HttpUtilsAsync.post("http://188.166.239.218:3001/api/user/", new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//
//            }
//        });

    }

    @Override
    public void onBackPressed(){
        flagCountDownExit = true;
        backPressCount ++;
        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                flagCountDownExit = false;
                backPressCount = 0;
            }
        }.start();
        if(flagCountDownExit && backPressCount==2){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
