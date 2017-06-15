package com.example.sin.projectone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nanth on 11/25/2016.
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;
    private int layout;
    public ProductAdapter(Context context, ArrayList<Product> products, int layout){
        super(context, 0, products);
        this.products = products;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(ProductAdapter.this.layout, parent, false);
        }
        // Lookup view for data population
        if(product.id!=null && !product.id.isEmpty()){
//            String id, String name, String barcode,
//                    String price, int qty, String type, String imgName,
//                    String cost, String details, String createAt){

            TextView productName = (TextView) convertView.findViewById(R.id.text_product_name);
            TextView productBarcode = (TextView) convertView.findViewById(R.id.text_product_barcode);
            TextView productPrice = (TextView) convertView.findViewById(R.id.text_product_price);
            TextView productQty = (TextView) convertView.findViewById(R.id.text_product_qty);
            TextView productType = (TextView) convertView.findViewById(R.id.text_product_type);
            TextView productCost = (TextView) convertView.findViewById(R.id.text_product_cost);
            TextView productdetail = (TextView) convertView.findViewById(R.id.text_product_details);
            if(productName!=null){
                productName.setText(product.name);
            }
            if(productBarcode!=null){
                productBarcode.setText(product.barcode);
            }
            if(productPrice!=null){
                productPrice.setText(product.price);
            }
            if(productQty!=null){
                productQty.setText(Integer.toString(product.qty));
            }
            if(productType!=null){
                productType.setText(product.type);
            }
            if(productCost!=null){
                productCost.setText(product.cost);
            }
            if(productdetail!=null){
                productdetail.setText(product.details);
            }

        }
        // Populate the data into the template view using the data object
        // Return the completed view to render on screen
        return convertView;
    }

    public boolean addQtyProduct(String productID, int qty){
        if(productID==null || productID.isEmpty()){
            return false;
        }
        for(int i=0;i<products.size();i++){
            Product p  = products.get(i);
            if(p.id.equals(productID)){
                p.qty+=qty;
                products.set(i,p);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Product> getAllItem(){
        return products;
    }

    public void UpdateItems(ArrayList<Product> products){
        this.products = products;
        return;
    }

    public int searchIndexProduct(String barCode){
        Product p;
        for(int i=0;i<products.size();i++){
            p = products.get(i);
            if(p.barcode.equals(barCode)){
                return i;
            }
        }
        return -1;
    }




}
