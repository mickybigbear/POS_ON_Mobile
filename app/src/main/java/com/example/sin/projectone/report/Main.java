package com.example.sin.projectone.report;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.HttpUtilsAsync;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;
import com.example.sin.projectone.UserManager;
import com.example.sin.projectone.receipt.TransListCursor;
import com.example.sin.projectone.receipt.list_fragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by naki_ on 11/25/2016.
 */

public class Main extends Fragment {
    private Button dailyReport;
    private Button topSell;
    ProgressDialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_report_main, container, false);
        progress = ProgressDialog.show(this.getActivity(), "Loading",
                "Please wait ...", true);
        loadTransaction();
        dailyReport =(Button) view.findViewById(R.id.report_main_butt_daily);
        topSell = (Button) view.findViewById(R.id.report_main_butt_tops);
        dailyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check BTOON CLICK", "onClick: ");
                Fragment newFragment = new Daily();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                transaction.replace(R.id.fragment_report_container, newFragment, null);
                transaction.addToBackStack(null);
// Commit the transaction
                transaction.commit();
            }
        });
        topSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check BTOON CLICK", "onClick: ");
                Fragment newFragment = new TopSellerPieChart();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                transaction.replace(R.id.fragment_report_container, newFragment, null);
                transaction.addToBackStack(null);
// Commit the transaction
                transaction.commit();

            }
        });
        return view;
    }

    private boolean loadTransaction(){
        // debug
        UserManager userManager = new UserManager(getActivity().getApplicationContext());
        HttpUtilsAsync.setTimeout(5000);
        HttpUtilsAsync.get(Constant.URL_SEND_TRANSACTION + userManager.getShopId() /*Constant.SHOP_ID*/, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("Response", String.valueOf(response.getJSONArray("transaction").length()));
                    if(response.getJSONArray("transaction").length()>0){
                        System.out.println(response);
                        ProductDBHelper.getInstance(Main.this.getActivity()).loadTransaction(response.getJSONArray("transaction"));
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
                                        ProductDBHelper.getInstance(Main.this.getActivity()).loadTransactionDetail(response.getJSONArray("transactionDetail"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progress.dismiss();
                                }
                            });
                        }
                    }
                    else {
                        progress.dismiss();
//                        lv.setEmptyView(empTxt);
                        System.out.println("Empty");
                    }
                    System.out.println("finish");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println(errorResponse + " " + statusCode);
                CharSequence text = "Failed to connect with server";
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("Failed: "+ ""+statusCode);
                CharSequence text = "Failed to connect with server";
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
                Log.d("Error : ", "" + throwable);
            }
            @Override
            public void onFinish() {
                super.onFinish();
                if(progress.isShowing()){
                    progress.dismiss();
                }
            }
        });
        return true;
    }
}
