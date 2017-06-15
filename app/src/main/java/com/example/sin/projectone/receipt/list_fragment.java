package com.example.sin.projectone.receipt;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.HttpUtilsAsync;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by naki_ on 11/25/2016.
 */

public class list_fragment extends ListFragment implements AdapterView.OnItemClickListener {
    ProgressDialog progress;
    ListView lv;
    TextView empTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_receipt_list_fragment, container, false);
//        ListView lvItems = (ListView) view.findViewById(R.id.list);
        progress = ProgressDialog.show(this.getActivity(), "Loading",
                "Please wait ...", true);

//        System.out.println(transList);
        loadTransaction();
        Cursor todoCursor = ProductDBHelper.getInstance(this.getActivity()).getTransaction();
//        System.out.println(transList);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.Planets, android.R.layout.simple_list_item_1);

        lv = (ListView)view.findViewById(android.R.id.list);
        empTxt = (TextView) view.findViewById(android.R.id.empty);
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Cursor todoCursor = ProductDBHelper.getInstance(this.getActivity()).getTransaction();
////        System.out.println(transList);
////        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
////                R.array.Planets, android.R.layout.simple_list_item_1);
//        TransListCursor todoAdapter = new TransListCursor(this.getActivity(), todoCursor);
//        setListAdapter(todoAdapter);
//        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        String strTrans = cursor.getString(cursor.getColumnIndex("transactionID"));
        String strUser = cursor.getString(cursor.getColumnIndex("username"));
        String strTotal = String.valueOf(cursor.getDouble(cursor.getColumnIndex("total")));
        String strDisc = String.valueOf(cursor.getDouble(cursor.getColumnIndex("discount")));
        String strCreate = cursor.getString(cursor.getColumnIndex("createAt"));
        Log.d("listView", strTrans);
        Log.d("listView", strUser);
        Log.d("listView", strTotal);
        Log.d("listView", strDisc);
        Log.d("listView", strCreate);
        DetailList transDetail = new DetailList ();
        Bundle args = new Bundle();
        args.putString("transactionID", strTrans);
        args.putString("username", strUser);
        args.putString("total", strTotal);
        args.putString("discount", strDisc);
        args.putString("create", strCreate);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        transDetail.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_receipt_container, transDetail).commit();
    }

    private boolean loadTransaction(){
        // debug
        HttpUtilsAsync.setTimeout(2);
        HttpUtilsAsync.get(Constant.URL_SEND_TRANSACTION+ Constant.SHOP_ID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("Response", String.valueOf(response.getJSONArray("transaction").length()));
                    if(response.getJSONArray("transaction").length()>0){
                        System.out.println(response);
                        ProductDBHelper.getInstance(list_fragment.this.getActivity()).loadTransaction(response.getJSONArray("transaction"));
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
                                        ProductDBHelper.getInstance(list_fragment.this.getActivity()).loadTransactionDetail(response.getJSONArray("transactionDetail"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    progress.dismiss();
                                    Cursor todoCursor = ProductDBHelper.getInstance(list_fragment.this.getActivity()).getTransaction();
                                    TransListCursor todoAdapter = new TransListCursor(list_fragment.this.getActivity(), todoCursor);
                                    setListAdapter(todoAdapter);
                                    getListView().setOnItemClickListener(list_fragment.this);
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
