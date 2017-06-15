package com.example.sin.projectone.item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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

import java.io.File;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nanth on 12/12/2016.
 */

public class AddProduct extends Fragment {

    private boolean flagReset = true;
    private Product targetProduct;
    private Bitmap targetImg;
    private ImageView imgProduct;
    private FragmentManager fragmentManager;
    private EditText edt_p_name, edt_p_barcode, edt_p_qty, edt_p_price, edt_p_type, edt_p_cost, edt_p_detail;
    private Button btn_back, btn_add;
    private ImgManager imgManager;
    private ImageButton btn_scan;
    private String lastBarcodeResult="";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_add, container, false);
        fragmentManager = getFragmentManager();
        imgManager = ImgManager.getinstance();
        btn_back = (Button) view.findViewById(R.id.btn_cancel);
        btn_add = (Button) view.findViewById(R.id.btn_add);

        edt_p_name = (EditText) view.findViewById(R.id.edt_text_product_name);
        edt_p_barcode = (EditText) view.findViewById(R.id.edt_text_product_barcode);
        edt_p_qty = (EditText) view.findViewById(R.id.edt_num_product_qty);
        edt_p_price = (EditText) view.findViewById(R.id.edt_num_product_price);
        edt_p_cost = (EditText) view.findViewById(R.id.edt_num_product_cost);
        edt_p_detail = (EditText) view.findViewById(R.id.edt_text_product_details);
        edt_p_type = (EditText) view.findViewById(R.id.edt_text_product_type);
        imgProduct = (ImageView) view.findViewById(R.id.img_product);
        btn_scan = (ImageButton) view.findViewById(R.id.btn_scan);

        btn_add.setOnClickListener(onAddClick());
        btn_back.setOnClickListener(onBackClick());
        btn_scan.setOnClickListener(onBtnScanClick());
        imgProduct.setOnClickListener(onImgClick());
        return view;
    }

    private View.OnClickListener onAddClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p_id, p_name, p_barcode, p_price, p_type, p_imgName, p_cost, p_detail, p_createAt;
                int p_qty;
                JSONObject JsonProduct;
                try{
                    // get data from edit text
                    p_id =  String.valueOf(Constant.PRODUCT_ID_INSERT_TEMP); // 0 temp data send
                    p_name = AddProduct.this.edt_p_name.getText().toString();
                    p_barcode = AddProduct.this.edt_p_barcode.getText().toString();
                    p_price = AddProduct.this.edt_p_price.getText().toString();
                    p_qty = Integer.parseInt(AddProduct.this.edt_p_qty.getText().toString());
                    p_type = AddProduct.this.edt_p_type.getText().toString();
                    p_imgName = Constant.IMG_NAME_TEMP; // temp data send
                    p_cost = AddProduct.this.edt_p_cost.getText().toString();
                    p_detail = AddProduct.this.edt_p_detail.getText().toString();
                    p_createAt = Constant.CREATE_AT_TEMP; // temp data send;
                    //  Bad Input -> return
                    if(p_name.isEmpty()|| p_barcode.isEmpty() || p_price.isEmpty() ||
                            p_qty<0 || p_cost.isEmpty() || targetImg ==null){
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG, "Input not complete");
                        bundle.putBoolean(Constant.KEY_BYNDLE_HAS_OK_DIALOG, true);
                        MessageAlertDialog messageAlert = MessageAlertDialog.newInstance(bundle);
                        messageAlert.show(fragmentManager, Constant.TAG_FRAGMENT_DIALOG_ALERT);
                        return;
                    }
                    // make obj product
                    targetProduct = new Product(p_id, p_name, p_barcode, p_price, p_qty,
                            p_type, p_imgName, p_cost, p_detail, p_createAt);
                    // make Json Product add send
                    JsonProduct = targetProduct.toJSONObject();
                    JsonProduct.put(Constant.KEY_JSON_SHOPID, Constant.SHOP_ID); // add shop id
                    //
                    File imgProduct = imgManager.saveImgToInternalStorage(targetImg, Constant.IMG_NAME_TEMP);

                    final ProgressDialog progress = ProgressDialog.show(AddProduct.this.getActivity(), "Loading",
                            "Please wait ...", true);

                    WebService.sendAddProduct(new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try { // insert data , save img
                                JSONObject jsonObject = new JSONObject(new String(responseBody));
                                ProductDBHelper.getInstance(getActivity()).insertProduct(jsonObject); // insert db
                                String imgName = jsonObject.getString(Constant.KEY_JSON_PRODUCT_IMG);
                                imgManager.saveImgToInternalStorage(targetImg, imgName);
                                targetImg =null;
                                targetProduct= null;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progress.dismiss();
                            final String tag = Constant.TAG_FRAGMENT_DIALOG_ALERT;
                            Bundle b = new Bundle();
                            b.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG,"ADD Product Finish");
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
                            reset();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            progress.dismiss();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.remove(AddProduct.this).commit();
                            fragmentManager.popBackStack();
                        }
                    }, JsonProduct,imgProduct);
                }catch (Exception e){

                }
            }
        };
    }

    private View.OnClickListener onBackClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(AddProduct.this).commit();
                fragmentManager.popBackStack();
            }
        };
    }

    private ImageView.OnClickListener onImgClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        };
    }

    private ImageButton.OnClickListener onBtnScanClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String tag = Constant.TAG_FRAGMENT_SCAN_BARCODE;
                Fragment scanBarcode = new ScanBarCode();
                scanBarcode.setTargetFragment(AddProduct.this, Constant.REQUEST_CODE_BARCODE);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame_container_item, scanBarcode, tag);
                fragmentTransaction.commit();
            }
        };
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constant.REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            targetImg = (Bitmap) extras.get("data");
            //String path = imgManager.saveImgToInternalStorage(imageBitmap, imgName);
            imgProduct.setImageBitmap(targetImg);
        }
        if(requestCode == Constant.REQUEST_CODE_BARCODE && resultCode == Constant.RESULT_CODE_BARCODE){
            lastBarcodeResult = data.getCharSequenceExtra(Constant.KEY_INTENT_BARCODE).toString();
        }
    }

    private void reset(){

//          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//          fragmentTransaction.remove(this);
//          String tag = Constant.TAG_FRAGMENT_ITEM_ADD;
//          Fragment addProduct = new AddProduct();
//          fragmentTransaction.replace(R.id.frame_container_item, addProduct, tag );
//          fragmentTransaction.commit();
        targetProduct = null;
        targetImg = null;
        edt_p_name.setText("");
        edt_p_barcode.setText("");
        edt_p_qty.setText("");
        edt_p_price.setText("");
        edt_p_cost.setText("");
        edt_p_detail.setText("");
        edt_p_type.setText("");
//        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
//                R.drawable.ic_menu_gallery);
//        imgProduct.setImageBitmap(icon);
    }
    @Override
    public void onResume(){
        super.onResume();
        edt_p_barcode.setText(lastBarcodeResult);
    }

}
