package com.example.sin.projectone.payment;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sin.projectone.ApplicationHelper;
import com.example.sin.projectone.Constant;
import com.example.sin.projectone.MessageAlertDialog;
import com.example.sin.projectone.Product;
import com.example.sin.projectone.ProductAdapter;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.ProductPaymentDialog;
import com.example.sin.projectone.R;
import com.example.sin.projectone.SwipeDetector;

import java.util.ArrayList;

/**
 * Created by nanth on 12/8/2016.
 */

public class Main extends Fragment  {
    private ProgressDialog progress;
    Button _btn_next, btn_last_scan;
    private ListView _productList;
    public ArrayList<Product> products = new ArrayList<Product>();
    private ProductAdapter adapter;
    private boolean flagShowListView = false;
    private SwipeDetector swipeDetector = new SwipeDetector();
    private LinearLayout layout_listview, layout_scanner;
    private FragmentManager fragmentManager;
    public String lastBarcodeScan="-1";
    public ImageView imgProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_payment_main, container, false);
        fragmentManager = getFragmentManager();
        imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
        Fragment newFragment = new ScanPayment();
        String tagFragment = Constant.TAG_FRAGMENT_SCAN_PAYMENT;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment checkFragment = fragmentManager.findFragmentByTag(tagFragment);
        if(checkFragment!=null){
            transaction.remove(checkFragment);
        }
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.frame_container_scanner, newFragment, tagFragment);
        //transaction.addToBackStack();
        // Commit the transaction
        transaction.commit();
        _btn_next = (Button) view.findViewById(R.id.pay_next);
        btn_last_scan = (Button) view.findViewById(R.id.btn_last_scan);
        _btn_next.setOnClickListener(nextBtnClick());
        btn_last_scan.setOnClickListener(onLastScanClick());
        // get layout
        layout_listview = (LinearLayout) view.findViewById(R.id.sub_layout_listView);
        layout_scanner = (LinearLayout) view.findViewById(R.id.sub_layout_scanner);
        // set list view
        _productList = (ListView)view.findViewById(R.id.product_list);
        int a = R.layout.list_item_endpayment;
        int b = R.layout.list_item;
        adapter = new ProductAdapter(ApplicationHelper.getAppContext(),products, R.layout.list_item_endpayment);
        _productList.setAdapter(adapter);
        _productList.setOnTouchListener(swipeDetector);
        _productList.setOnItemClickListener(onItemClickListener());
        return view;
    }

    private View.OnClickListener nextBtnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!products.isEmpty() && products.size()>0){
                    Bundle product_bundle = new Bundle();
                    product_bundle.putParcelableArrayList(Constant.KEY_BUNDLE_ARRAYLIST_PRODUCT, products);
                    String tagFragment = Constant.TAG_FRAGMENT_PAYMENT_END;
                    Fragment endPayment = new EndPayment();
                    endPayment.setArguments(product_bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Fragment checkFragment = fragmentManager.findFragmentByTag(tagFragment);
                    if(checkFragment!=null){
                        transaction.remove(checkFragment);
                    }
                    transaction.replace(R.id.frame_container_payment, endPayment, Constant.TAG_FRAGMENT_PAYMENT_END);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG, Constant.MESSAGE_ALERT_PRODUCT_SCAN_FIRST);
                    MessageAlertDialog.newInstance(bundle).show(fragmentManager, Constant.TAG_FRAGMENT_DIALOG_ALERT);
                }
            }
        };
    }

    private View.OnClickListener onLastScanClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = Constant.TAG_FRAGMENT_DIALOG_PRODUCT_DETAIL;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment prev = fragmentManager.findFragmentByTag(tag);
                if(prev!=null){
                    transaction.remove(prev);
                }
                String s = lastBarcodeScan;
                int index = adapter.searchIndexProduct(lastBarcodeScan);
                if(index>=0){
                    Product p = adapter.getItem(index);
                    ProductPaymentDialog detailDialog = ProductPaymentDialog.newInstance(p);
                    detailDialog.setTargetFragment(Main.this, Constant.REQUEST_CODE_PRODUCT_PAYMENT_DIALOG);
                    detailDialog.show(transaction, tag);
                }
                else{
                    String tagAlert = Constant.TAG_FRAGMENT_DIALOG_ALERT;
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG, "Scan Product first");
                    bundle.putBoolean(Constant.KEY_BYNDLE_HAS_OK_CANCEL_DIALOG, false);
                    final MessageAlertDialog alertDialog = MessageAlertDialog.newInstance(bundle);
                    alertDialog.show(fragmentManager, tagAlert);
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub
                        }
                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            alertDialog.dismiss();
                        }
                    }.start();
                }
            }
        };
    }

    private AdapterView.OnItemClickListener onItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(swipeDetector.swipeDetected()){
                    if(swipeDetector.getAction()== SwipeDetector.Action.LR){
                        adapter.remove(adapter.getItem(position));
                        if(adapter.getCount()==0){
                            layout_listview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0.2f));
                            layout_scanner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0.5f));
                            flagShowListView = false;
                        }
                    }
                    else if(swipeDetector.getAction()== SwipeDetector.Action.RL){
                        if(flagShowListView){
                            layout_listview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0.2f));
                            layout_scanner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0.5f));
                        }else{
                            layout_scanner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0.2f));
                            layout_listview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0.5f));
                        }
                        flagShowListView = !flagShowListView;
                    }
                }
                else{
                    Product pCheck = adapter.getItem(position);
                    int qty_check = ProductDBHelper.getInstance(getActivity()).searchProductByID(pCheck.id).qty;
                    if(pCheck.qty>qty_check){
                        Toast.makeText(Main.this.getActivity(), "Product must be less than"+(qty_check+1), Toast.LENGTH_LONG).show();
                        return;
                    }
                    adapter.addQtyProduct(adapter.getItem(position).id,1);
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    public int addProductPayment(Product product){
        if(!adapter.addQtyProduct(product.id,1)){
            adapter.add(product);
        }
        adapter.notifyDataSetChanged();
        return Integer.parseInt(product.id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CODE_PRODUCT_PAYMENT_DIALOG ) {
            //Product product = data.getParcelableExtra(Constant.KEY_INTENT_PRODUCT);
            adapter.notifyDataSetChanged();
        }
        if (requestCode==Constant.REQUEST_CODE_OK_CANCEL && resultCode==Constant.RESULT_CODE_CANCEL){
            reset();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(products.size()>0){
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG, "Continue Scan last product ?");
            MessageAlertDialog dialog = MessageAlertDialog.newInstance(bundle);
            dialog.setTargetFragment(Main.this, Constant.REQUEST_CODE_OK_CANCEL);
            dialog.show(fragmentManager,Constant.TAG_FRAGMENT_DIALOG_ALERT);
        }
    }

    public void reset(){
        flagShowListView = false;
        products.clear();
        adapter.clear();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
