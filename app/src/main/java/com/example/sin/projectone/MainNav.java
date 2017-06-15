package com.example.sin.projectone;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.widget.Toast;

import com.example.sin.projectone.payment.Main;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by naki_ on 11/25/2016.
 */

public class MainNav extends MaterialNavigationDrawer {
    FragmentManager fragmentManager;
    private static Boolean flagCountDownExit = false;
    private static int backPressCount = 0;
    @Override
    public void init(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        this.deleteDatabase(ProductDBHelper.DATABASE_NAME); // debug
        int a = Constant.SHOP_ID;
        loadProducts();

        loadTransaction();
        this.addSection(newSection("Item", new com.example.sin.projectone.item.Container()));
        this.addSection(newSection("Payment", new com.example.sin.projectone.payment.Container()));
        this.addSection(newSection("Receipt", new com.example.sin.projectone.receipt.Container()));
        this.addSection(newSection("Report", new com.example.sin.projectone.report.Container()));
        this.addBottomSection(newSection("Contact us",new com.example.sin.projectone.help.Main()));
//        this.addSubheader("Account");
//        this.addSection(newSection("Profile", new com.example.sin.projectone.profile.Main()));
//        this.addDivisor();
//        this.addSection(newSection("Help & Feedback", new com.example.sin.projectone.help.Main()));
//        this.addSection(newSection("Credit", new com.example.sin.projectone.credit.Main()));

    }

    private boolean loadProducts(){
        WebService.getAllProduct(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.length()>0){
                        ProductDBHelper.getInstance(MainNav.this.getApplicationContext()).LoadProduct(response.getJSONArray("Products"));
                    }
                    else if(response.length()==0){
                        System.out.println("Empty");
                    }
                    loadTransaction();
                    System.out.println("finish");
                    //ProductDBHelper.getInstance(getApplicationContext()).ShowListProduct();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private boolean loadTransaction(){
        // debug
        HttpUtilsAsync.get(Constant.URL_SEND_TRANSACTION+Constant.SHOP_ID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.length()>0){
                        ProductDBHelper.getInstance(MainNav.this.getApplicationContext()).loadTransaction(response.getJSONArray("transaction"));
                        for(int i=0;i<response.getJSONArray("transaction").length();i++){
                            System.out.println(response.getJSONArray("transaction").length());
                            System.out.println(response.getJSONArray("transaction").getJSONObject(i));
                            JSONObject jsonObj = response.getJSONArray("transaction").getJSONObject(i);
                            String createDate = jsonObj.getString("createAt");
                            createDate = createDate.replace(' ','T');
                            System.out.println(jsonObj.getString("transactionID"));
                            System.out.println(jsonObj.getString("createAt"));
                            HttpUtilsAsync.get(Constant.URL_GET_TRANSACTION_DETAIL+jsonObj.getString("transactionID")+"/"+createDate, null, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    System.out.println(response);
                                    try {
                                        ProductDBHelper.getInstance(MainNav.this.getApplicationContext()).loadTransactionDetail(response.getJSONArray("transactionDetail"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    else if(response.length()==0){
                        System.out.println("Empty");
                    }
                    System.out.println("finish");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
//        if(fragmentManager.getBackStackEntryCount()>0){
//            super.onBackPressed();
//            return;
//        }
//        backPressCount ++;
//        flagCountDownExit = true;
//        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
//        new CountDownTimer(3000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                // TODO Auto-generated method stub
//            }
//            @Override
//            public void onFinish() {
//                // TODO Auto-generated method stub
//                flagCountDownExit = false;
//            }
//        }.start();
//        if(flagCountDownExit && backPressCount==2){
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
//        }

    }

}
