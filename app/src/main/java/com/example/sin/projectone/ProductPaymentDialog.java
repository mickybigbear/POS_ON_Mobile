package com.example.sin.projectone;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nanth on 12/10/2016.
 */

public class ProductPaymentDialog extends DialogFragment {
    private Product saveProduct;
    private Product product;
    private EditText edt_p_qty;
    private TextView text_p_name, text_p_price;
    private Button btn_cancel, btn_submit;
    private ImageView img_product;

    public static ProductPaymentDialog newInstance(Product product){
        ProductPaymentDialog dialog = new ProductPaymentDialog();
        dialog.product = product;
        dialog.saveProduct = (Product) product.clone();
        return  dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_detail_payment, container, false);
        img_product = (ImageView) v.findViewById(R.id.img_product);
        edt_p_qty = (EditText) v.findViewById(R.id.edt_text_product_qty);
        btn_submit = (Button) v.findViewById(R.id.btn_submit);
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        text_p_name = (TextView) v.findViewById(R.id.text_product_name);
        text_p_price = (TextView) v.findViewById(R.id.text_product_price);
        edt_p_qty = (EditText) v.findViewById(R.id.edt_text_product_qty);
        text_p_name.setText(product.name);
        text_p_price.setText(product.price);
        edt_p_qty.setText(String.valueOf(product.qty));
        Bitmap img = ImgManager.getinstance().loadImageFromStorage(product.imgName);
        if(img!=null){
            img_product.setImageBitmap(img);
        }
        btn_submit.setOnClickListener(onSubmit());
        btn_cancel.setOnClickListener(onCancel());
        return v;
    }

    public View.OnClickListener onSubmit(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty_check = ProductDBHelper.getInstance(getActivity()).searchProductByID(saveProduct.id).qty;
                int qtyInput = Integer.parseInt(edt_p_qty.getText().toString());
                if(qtyInput>qty_check){
                    Toast.makeText(ProductPaymentDialog.this.getActivity(), "Product must be less than"+(qty_check+1), Toast.LENGTH_LONG).show();
                    product.qty = saveProduct.qty;
                    edt_p_qty.setText(String.valueOf(product.qty));
                    return;
                }
                try{
                    product.qty = Integer.parseInt(edt_p_qty.getText().toString());// pass reference .qty
                }
                catch (NumberFormatException e){
                    product.qty = 0;
                }
                Intent data = new Intent();
                data.putExtra(Constant.KEY_INTENT_PRODUCT, product);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),Constant.RESULT_CODE_PRODUCT_PAYMENT_DIALOG_SUBMIT, data
                );
                ProductPaymentDialog.this.dismiss();
            }
        };
    }

    public View.OnClickListener onCancel(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),Constant.RESULT_CODE_PRODUCT_PAYMENT_DIALOG_CANCEL, null
                );
                ProductPaymentDialog.this.dismiss();
            }
        };
    }


}
