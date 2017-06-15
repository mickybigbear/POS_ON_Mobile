package com.example.sin.projectone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nanth on 12/11/2016.
 */

public class ProductViewAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;
    private int layout;
    private int idText;
    public ProductViewAdapter(Context context, ArrayList<Product> products, int layout, int idLabel, int idText){
        super(context, 0, products);
        this.products = products;
        this.layout = layout;
        this.idText = idText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(ProductViewAdapter.this.layout, parent, false);
        }
        // Lookup view for data population
        if(product.id!=null && !product.id.isEmpty()){
//            String id, String name, String barcode,
//                    String price, int qty, String type, String imgName,
//                    String cost, String details, String createAt){

            TextView productName = (TextView) convertView.findViewById(idText);
            if(productName!=null){
                productName.setText(product.name);
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

    public ArrayList<Product> getAllProducts(){
        return products;
    }

}

