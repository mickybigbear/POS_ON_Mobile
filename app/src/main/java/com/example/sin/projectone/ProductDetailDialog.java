package com.example.sin.projectone;


import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nanth on 12/10/2016.
 */

public class ProductDetailDialog extends DialogFragment {

    private Product product;
    private String title ="Product Detail";
    private TextView text_name, text_barcode, text_price, text_qty,
            text_type, text_cost, text_detail;
    private Button btn_back, btn_edit;
    private ImageView img_product;
    private View.OnClickListener onBackClick, onEditClick;
    public static ProductDetailDialog newInstance(Bundle data){
        ProductDetailDialog dialog = new ProductDetailDialog();
        dialog.product = data.getParcelable(Constant.KEY_BUNDLE_PRODUCT);
        return  dialog;
    }

    public static ProductDetailDialog newInstance(Bundle data, View.OnClickListener onBackClick
    , View.OnClickListener onEditClick){
        ProductDetailDialog dialog = new ProductDetailDialog();
        dialog.product = data.getParcelable(Constant.KEY_BUNDLE_PRODUCT);
        dialog.onBackClick = onBackClick;
        dialog.onEditClick = onEditClick;
        return  dialog;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail, container, false);
        img_product = (ImageView) view.findViewById(R.id.img_product);
        text_name = (TextView) view.findViewById(R.id.text_product_name);
        text_barcode = (TextView) view.findViewById(R.id.text_product_barcode);
        text_price = (TextView) view.findViewById(R.id.text_product_price);
        text_qty = (TextView) view.findViewById(R.id.text_product_qty);
        text_type = (TextView) view.findViewById(R.id.text_product_type);
        text_cost = (TextView) view.findViewById(R.id.text_product_cost);
        text_detail = (TextView) view.findViewById(R.id.text_product_details);
        btn_back = (Button) view.findViewById(R.id.btn_cancel);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);


        btn_back.setOnClickListener(onBackClick);
        btn_edit.setOnClickListener(onEditClick);

        text_name.setText(product.name);
        text_barcode.setText(product.barcode);
        text_price.setText(product.price);
        text_qty.setText(String.valueOf(product.qty));
        text_type.setText(product.type);
        text_cost.setText(product.cost);
        text_detail.setText(product.details);

        String fileName = product.imgName;
        Bitmap img =  ImgManager.getinstance().loadImageFromStorage(fileName);
        if(img!=null){
            img_product.setImageBitmap(img);
        }

        return view;
    }

}
