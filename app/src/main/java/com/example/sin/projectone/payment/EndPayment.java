package com.example.sin.projectone.payment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sin.projectone.ApplicationHelper;
import com.example.sin.projectone.Constant;
import com.example.sin.projectone.MessageAlertDialog;
import com.example.sin.projectone.Product;
import com.example.sin.projectone.ProductAdapter;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.ProductPaymentDialog;
import com.example.sin.projectone.R;
import com.example.sin.projectone.WebService;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nanth on 12/8/2016.
 */

public class EndPayment extends Fragment {
    private ListView _productList;
    public ArrayList<Product> products = new ArrayList<Product>();
    private ProductAdapter adapter;
    private TextView text_total;
    private EditText edt_discount;
    private Button btn_back, btn_send;
    private FragmentManager fragmentManager;
    private Product productMaster;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_payment_end, container, false);
        fragmentManager = getFragmentManager();
        Bundle product_bundle = this.getArguments();
        if(product_bundle!=null){
            products = product_bundle.getParcelableArrayList(Constant.KEY_BUNDLE_ARRAYLIST_PRODUCT);
        }
        // set list view
        _productList = (ListView)view.findViewById(R.id.product_list);
        adapter = new ProductAdapter(ApplicationHelper.getAppContext(),products,R.layout.list_item_endpayment);
        _productList.setAdapter(adapter);
        _productList.setOnItemClickListener(onItemClickListener());
        //
        btn_back = (Button) view.findViewById(R.id.btn_cancel);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        text_total =(TextView) view.findViewById(R.id.text_total);
        edt_discount = (EditText) view.findViewById(R.id.edit_text_discount);
        text_total.setText(String.valueOf(getTotal()));
        btn_back.setOnClickListener(onBackClick());
        btn_send.setOnClickListener(onSendClick());
        //
        return view;
    }

    private ListView.OnItemClickListener onItemClickListener(){
        return new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = Constant.TAG_FRAGMENT_DIALOG_PRODUCT_DETAIL;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment prev = fragmentManager.findFragmentByTag(tag);
                if(prev!=null){
                    transaction.remove(prev);
                }
                Product product = adapter.getItem(position);
                ProductPaymentDialog productPaymentDialog =  ProductPaymentDialog.newInstance(product);
                productPaymentDialog.setTargetFragment(EndPayment.this, Constant.REQUEST_CODE_PRODUCT_PAYMENT_DIALOG);
                productPaymentDialog.show(fragmentManager, tag);
            }
        };
    }

    private float getTotal(){
        float total = 0;
        for(Product p : adapter.getAllItem()){
            float price = 0;
            try{
                price = Integer.parseInt(p.price);
            }catch (Exception e){
            }
            total += p.qty * price;
        }
        return total;
    }

    private View.OnClickListener onSendClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progress = ProgressDialog.show(EndPayment.this.getActivity(), "Loading",
                        "Please wait ...", true);

                String detail = "";
                float discount = 0.0f;
                float total = 0.0f;
                try{
                    discount = Float.parseFloat(edt_discount.getText().toString());
                    total = Float.parseFloat(text_total.getText().toString());
                }
                catch (Exception e){
                    return;
                }
                JSONObject transaction =  ProductDBHelper.getInstance(ApplicationHelper.getAppContext()).getJSONTransaction(products,detail,discount,total);
                //products.clear(); //  block send data more once transaction
                WebService.sendTransaction(new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Update DB
                        Product updateP;
                        ProductDBHelper dbHelper = ProductDBHelper.getInstance(getActivity());
                        for(Product p : products){
                            int a = p.qty;
                            updateP = (Product) p.clone();
                            updateP.qty = dbHelper.searchProductByID(p.id).qty - p.qty;
                            dbHelper.UpdateProduct(updateP);
                        }
                        progress.dismiss();
                        final String tag = Constant.TAG_FRAGMENT_DIALOG_ALERT;
                        int tranId = -1;
                        try {
                            JSONObject jsonObject = new JSONObject(new String(responseBody));
                            tranId = jsonObject.getInt(Constant.KEY_JSON_TRANSACTIONID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Bundle b = new Bundle();
                        b.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG,"TrasactionID : "+ String.valueOf(tranId));
                        b.putString(Constant.KEY_BUNDLE_TITLE_DIALOG, "Finished sending data");
                        b.putBoolean(Constant.KEY_BYNDLE_HAS_OK_CANCEL_DIALOG,false);
                        final MessageAlertDialog dialog2 = MessageAlertDialog.newInstance(b);
                        dialog2.show(fragmentManager,tag);
                        new CountDownTimer(3000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub
                            }
                            @Override
                            public void onFinish() {
                                // TODO Auto-generated method stub
                                dialog2.dismiss();
                            }
                        }.start();


                        ((Main)fragmentManager.findFragmentByTag(Constant.TAG_FRAGMENT_PAYMENT_MAIN)).reset();
                        fragmentManager.popBackStack();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        progress.dismiss();
                        ((Main)fragmentManager.findFragmentByTag(Constant.TAG_FRAGMENT_PAYMENT_MAIN)).reset();
                        fragmentManager.popBackStack();

                    }
                },transaction);

            }
        };
    }

    private View.OnClickListener onBackClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(EndPayment.this);
                fragmentManager.popBackStack();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Constant.REQUEST_CODE_PRODUCT_PAYMENT_DIALOG &&
                resultCode==Constant.RESULT_CODE_PRODUCT_PAYMENT_DIALOG_SUBMIT){
            text_total.setText(String.valueOf(getTotal()));
            adapter.notifyDataSetChanged();
        }
    }






}
