package com.example.sin.projectone.item;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProduct2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProduct2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProduct2 extends Fragment implements UpdatePageFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Product targetProduct;
    private Bitmap targetImg;
    private ImageView imgProduct;
    private FragmentManager fragmentManager;
    private EditText edt_p_name, edt_p_barcode, edt_p_qty, edt_p_price, edt_p_type, edt_p_cost, edt_p_detail;
    private Button  btn_add;
    private ImgManager imgManager;
    private ImageButton btn_scan;
    private String lastBarcodeResult="";

    public AddProduct2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProduct2.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProduct2 newInstance(String param1, String param2, OnFragmentInteractionListener mListener) {
        AddProduct2 fragment = new AddProduct2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_item_add2, container, false);
        fragmentManager = getFragmentManager();
        imgManager = ImgManager.getInstance();
        btn_add = (Button) view.findViewById(R.id.btn_add);

        edt_p_name = (EditText) view.findViewById(R.id.edt_text_product_name);
        edt_p_barcode = (EditText) view.findViewById(R.id.edt_text_product_barcode);
        edt_p_qty = (EditText) view.findViewById(R.id.edt_num_product_qty);
        edt_p_price = (EditText) view.findViewById(R.id.edt_num_product_price);
        edt_p_cost = (EditText) view.findViewById(R.id.edt_num_product_cost);
        edt_p_detail = (EditText) view.findViewById(R.id.edt_text_product_details);
        edt_p_type = (EditText) view.findViewById(R.id.edt_text_product_type);
        imgProduct = (ImageView) view.findViewById(R.id.img_product);
        if(targetImg!=null){
            imgProduct.setImageBitmap(targetImg);
        }
        btn_scan = (ImageButton) view.findViewById(R.id.btn_scan);

        btn_add.setOnClickListener(onAddClick());
        //btn_back.setOnClickListener(onBackClick());
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
                    p_name = AddProduct2.this.edt_p_name.getText().toString();
                    p_barcode = AddProduct2.this.edt_p_barcode.getText().toString();
                    p_price = AddProduct2.this.edt_p_price.getText().toString();
                    try{
                        p_qty = Integer.parseInt(AddProduct2.this.edt_p_qty.getText().toString());
                    }catch (Exception ex){
                        System.out.println(ex);
                        p_qty=0;
                    }
                    p_type = AddProduct2.this.edt_p_type.getText().toString();
                    p_imgName = Constant.IMG_NAME_TEMP; // temp data send
                    p_cost = AddProduct2.this.edt_p_cost.getText().toString();
                    p_detail = AddProduct2.this.edt_p_detail.getText().toString();
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

                    final ProgressDialog progress = ProgressDialog.show(AddProduct2.this.getActivity(), "Loading",
                            "Please wait ...", true);

                    WebService.sendAddProduct(new AsyncHttpResponseHandler() {
                        final String tag = Constant.TAG_FRAGMENT_DIALOG_ALERT;
                        Bundle bundleDialog = new Bundle();
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
                            bundleDialog.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG,getString(R.string.add_product_finish));
                            bundleDialog.putString(Constant.KEY_BUNDLE_TITLE_DIALOG, getString(R.string.finished_sending_data));
                            bundleDialog.putBoolean(Constant.KEY_BYNDLE_HAS_OK_CANCEL_DIALOG,false);
                            final MessageAlertDialog dialogSuccess = MessageAlertDialog.newInstance(bundleDialog);
                            dialogSuccess.show(fragmentManager,tag);
                            new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {}
                                @Override
                                public void onFinish() {dialogSuccess.dismiss(); }
                            }.start();
                            reset();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            progress.dismiss();
                            bundleDialog.putString(Constant.KEY_BUNDLE_MESSAGE_DIALOG,getString(R.string.can_not_connect_server));
                            bundleDialog.putString(Constant.KEY_BUNDLE_TITLE_DIALOG, getString(R.string.error_sending_data));
                            bundleDialog.putBoolean(Constant.KEY_BYNDLE_HAS_OK_CANCEL_DIALOG,false);
                            final MessageAlertDialog dialogFailure = MessageAlertDialog.newInstance(bundleDialog);
                            dialogFailure.show(fragmentManager,tag);
                            new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {}
                                @Override
                                public void onFinish() {dialogFailure.dismiss();}
                            }.start();
                        }
                    }, JsonProduct,imgProduct);
                }catch (Exception e){

                }
            }
        };
    }

//    private View.OnClickListener onBackClick(){
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.remove(AddProduct2.this).commit();
//                fragmentManager.popBackStack();
//            }
//        };
//    }

    private ImageView.OnClickListener onImgClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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

    private ImageButton.OnClickListener onBtnScanClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                String tag = Constant.TAG_FRAGMENT_SCAN_BARCODE;
                Fragment scanBarcode = new ScanBarCode();
                scanBarcode.setTargetFragment(AddProduct2.this, Constant.REQUEST_CODE_BARCODE);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.replace(R.id.fragment_container_main, scanBarcode, tag);
//                fragmentTransaction.commit();
                mListener.onFragmentChange(scanBarcode,null);
            }
        };
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updatePage() {

    }

    private void reset(){
        targetProduct = null;
        targetImg = null;
        edt_p_name.setText("");
        edt_p_barcode.setText("");
        edt_p_qty.setText("");
        edt_p_price.setText("");
        edt_p_cost.setText("");
        edt_p_detail.setText("");
        edt_p_type.setText("");
        edt_p_barcode.setText("");
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
        void onFragmentChange(Fragment fragment, Bundle bundle);
    }

    @Override
    public void onResume(){
        super.onResume();
        edt_p_barcode.setText(lastBarcodeResult);
    }
}
