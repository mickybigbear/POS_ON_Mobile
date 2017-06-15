package com.example.sin.projectone.item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.ImgManager;
import com.example.sin.projectone.MessageAlertDialog;
import com.example.sin.projectone.Product;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;
import com.example.sin.projectone.WebService;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by nanth on 12/11/2016.
 */

public class EditProduct extends Fragment {

    private Product products;
    private Product targetProduct;
    private Product saveProduct;
    private FragmentManager fragmentManager;
    private Button btn_back, btn_submit;
    private TextView text_product_type;
    private ImageView img_product;
    private EditText edt_p_name, edt_p_barcode, edt_p_qty, edt_p_price, edt_p_cost, edt_p_detail;
    private MessageAlertDialog alertDialog;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_edit, container, false);
        fragmentManager = getFragmentManager();
        Bundle productBundle = this.getArguments();
        if(productBundle!=null){
            products =  productBundle.getParcelable(Constant.KEY_BUNDLE_PRODUCT);
            saveProduct = (Product) products.clone();
        }
        img_product = (ImageView) view.findViewById(R.id.img_product);

        btn_back = (Button) view.findViewById(R.id.btn_cancel);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        edt_p_name = (EditText) view.findViewById(R.id.edt_text_product_name);
        edt_p_barcode = (EditText) view.findViewById(R.id.edt_text_product_barcode);
        edt_p_qty = (EditText) view.findViewById(R.id.edt_num_product_qty);
        edt_p_price = (EditText) view.findViewById(R.id.edt_num_product_price);
        edt_p_cost = (EditText) view.findViewById(R.id.edt_num_product_cost);
        edt_p_detail = (EditText) view.findViewById(R.id.edt_text_product_details);
        text_product_type = (TextView) view.findViewById(R.id.text_product_type);

        edt_p_name.setText(products.name);
        edt_p_barcode.setText(products.barcode);
        edt_p_qty.setText(String.valueOf(products.qty));
        edt_p_price.setText(products.price);
        edt_p_cost.setText(products.cost);
        edt_p_detail.setText(products.details);
        text_product_type.setText(products.type);

        Bitmap img = ImgManager.getinstance().loadImageFromStorage(products.imgName);
        if(img!=null){
            img_product.setImageBitmap(img);
        }

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_back = (Button) view.findViewById(R.id.btn_cancel);
        btn_submit.setOnClickListener(onSubmitClick());
        btn_back.setOnClickListener(onBackClick());

        return view;
    }

    private View.OnClickListener onBackClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(EditProduct.this).commit();
                fragmentManager.popBackStack();
            }
        };
    }

    private View.OnClickListener onSubmitClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p_id, p_name, p_barcode, p_price, p_type, p_imgName, p_cost, p_detail, p_createAt;
                int p_qty;
                try{
                    p_id = saveProduct.id;
                    p_name = EditProduct.this.edt_p_name.getText().toString();
                    p_barcode = EditProduct.this.edt_p_barcode.getText().toString();
                    p_price = EditProduct.this.edt_p_price.getText().toString();
                    p_qty = Integer.parseInt(EditProduct.this.edt_p_qty.getText().toString());
                    p_type = saveProduct.type; // can't edit
                    p_imgName = saveProduct.imgName; //debug;
                    p_cost = EditProduct.this.edt_p_cost.getText().toString();
                    p_detail = EditProduct.this.edt_p_detail.getText().toString();
                    p_createAt = saveProduct.createAt; // debug
                    targetProduct = new Product(p_id, p_name, p_barcode, p_price, p_qty,
                            p_type, p_imgName, p_cost, p_detail, p_createAt);
                    if(Product.isEquals(targetProduct,saveProduct)){
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(EditProduct.this).commit();
                        fragmentManager.popBackStack();
                        return;
                    }
                    else{
                        final ProgressDialog progress = ProgressDialog.show(EditProduct.this.getActivity(), "Loading",
                                "Please wait ...", true);

                        JSONObject JSProduct = targetProduct.toJSONObject();
                        if(JSProduct!=null){
                            WebService.sendUpdateProduct(new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {//new StringEntity(data, "UTF-8");
                                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                                        ProductDBHelper.getInstance(getActivity()).UpdateProduct(jsonObject); // update db
                                        ((ViewProduct)fragmentManager.findFragmentByTag(Constant.TAG_FRAGMENT_ITEM_VIEW)).RefreshProduct();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    ProductDBHelper.getInstance(getActivity()).UpdateProduct(EditProduct.this.targetProduct);
                                    progress.dismiss();
                                    final String tag = Constant.TAG_FRAGMENT_DIALOG_ALERT;
                                    Bundle b = new Bundle();
                                    b.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG,"Update Product Finish");
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
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.remove(EditProduct.this).commit();
                                    fragmentManager.popBackStack();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    progress.dismiss();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.remove(EditProduct.this).commit();
                                    fragmentManager.popBackStack();
                                }
                            },JSProduct);
                        }
                    }
                }catch (Exception e){
                    return;
                }
            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();
    }


}
