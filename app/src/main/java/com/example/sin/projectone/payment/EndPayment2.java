package com.example.sin.projectone.payment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link EndPayment2.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link EndPayment2#newInstance} factory method to
// * create an instance of this fragment.
// */
public class EndPayment2 extends Fragment implements UpdatePageFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProductAdapter adapter;
    private ListView _productList;
    public ArrayList<Product> products = new ArrayList<Product>();

    private TextView text_total;
    private Button btn_send;
    private EditText edt_discount;

    private OnFragmentInteractionListener mListener;

    public EndPayment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EndPayment2.
     */
    // TODO: Rename and change types and number of parameters
//    public static EndPayment2 newInstance(String param1, String param2, OnFragmentInteractionListener mListener) {
//        EndPayment2 fragment = new EndPayment2();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        if(mListener!=null){
//            fragment.mListener = mListener;
//        }
//        return fragment;
//    }

    public static EndPayment2 newInstance(String param1, String param2, ArrayList<Product> products, OnFragmentInteractionListener mListener) {
        EndPayment2 fragment = new EndPayment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.products = products;
        fragment.mListener = mListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_end2, container, false);
        _productList = (ListView)view.findViewById(R.id.product_list);
        adapter = new ProductAdapter(ApplicationHelper.getAppContext(),products,R.layout.list_item_endpayment);
        _productList.setAdapter(adapter);
        _productList.setOnItemClickListener(onItemClickListener());

        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setEnabled(false);
        text_total =(TextView) view.findViewById(R.id.text_total);
        edt_discount = (EditText) view.findViewById(R.id.edit_text_discount);
        text_total.setText(String.valueOf(getTotal()));
        btn_send.setOnClickListener(onSendClick());
        return view;
    }

    private View.OnClickListener onSendClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(products.size()<=0){
                    return;
                }
                for(int i=0;i<products.size();i++){
                    Product pdInBaskett = products.get(i);
                    int maxStock = ProductDBHelper.getInstance(getActivity().getApplicationContext()).searchProductByID(pdInBaskett.id).qty;
                    if(maxStock<pdInBaskett.qty){
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Product "+pdInBaskett.name+" in stock is not enough.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                final FragmentManager fragmentManager = getFragmentManager();
                final ProgressDialog progress = ProgressDialog.show(EndPayment2.this.getActivity(), "Loading",
                        "Please wait ...", true);

                String detail = "";
                float discount = 0.0f;
                float total = 0.0f;
                try{
                    discount = Float.parseFloat(edt_discount.getText().toString());
                    //total = Float.parseFloat(text_total.getText().toString());
                    total = getTotal();
                }
                catch (Exception e){
                    return;
                }
                JSONObject transaction =  ProductDBHelper.getInstance(ApplicationHelper.getAppContext()).getJSONTransaction(products,detail,discount,total);
                //basketProduct.clear(); //  block send data more once transaction
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
                        mListener.onSuccessPayment();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updatePage(){
        removeProductEmpty();
        adapter.notifyDataSetChanged();
        text_total.setText(String.valueOf(getTotal()));
        btn_send.setEnabled(true);
        if(adapter.getCount()<=0){btn_send.setEnabled(false); }
        else{btn_send.setEnabled(true);}
    }

    private void removeProductEmpty(){
        for(int i=0;i<products.size();i++){
            Product pdInBasket = products.get(i);
            if(pdInBasket.qty==0){
                products.remove(i);
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSuccessPayment();
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

    private ListView.OnItemClickListener onItemClickListener(){
        return new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                String tag = Constant.TAG_FRAGMENT_DIALOG_PRODUCT_DETAIL;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment prev = fragmentManager.findFragmentByTag(tag);
                if(prev!=null){
                    transaction.remove(prev);
                }
                Product product = adapter.getItem(position);
                ProductPaymentDialog productPaymentDialog =  ProductPaymentDialog.newInstance(product);
                productPaymentDialog.setTargetFragment(EndPayment2.this, Constant.REQUEST_CODE_PRODUCT_PAYMENT_DIALOG);
                productPaymentDialog.show(fragmentManager, tag);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Constant.REQUEST_CODE_PRODUCT_PAYMENT_DIALOG &&
                resultCode==Constant.RESULT_CODE_PRODUCT_PAYMENT_DIALOG_SUBMIT){
            updatePage();
        }
    }

}
