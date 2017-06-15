package com.example.sin.projectone;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nanth on 11/25/2016.
 */

public class Product implements Parcelable , Cloneable {
    public String id, name, barcode, price, type, imgName, cost, details, createAt;
    public int qty;

    public Product(String id, String name, String barcode,
                   String price, int qty, String type, String imgName,
                   String cost, String details, String createAt){
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.price = price;
        this.qty = qty;
        this.type = type;
        this.imgName = imgName;
        this.cost = cost;
        this.details =details;
        this.createAt = createAt;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        barcode = in.readString();
        price = in.readString();
        type = in.readString();
        imgName = in.readString();
        qty = in.readInt();
        cost = in.readString();
        details = in.readString();
        createAt = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static Product CursorToProduct(Cursor cursor){
        try{
            if(cursor.moveToFirst()){
                Product p =new Product(//id name bacode price
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_P_ID)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_BARCODE)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_QTY)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_IMG)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_COST)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_DETAILS)),
                        cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_CREATE_AT))
                        );
                return  p;
            }
        }finally {
            cursor.close();
        }
        return null;
    }

    public static ArrayList<Product> CursorToProductArrayList(Cursor cursor){
        ArrayList<Product> products = new ArrayList();
        try{
            while(cursor.moveToNext()){
                Product p = null;
                try{
                    String a = cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_P_ID));
                    p =new Product(//id name bacode price
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_P_ID)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_BARCODE)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_PRICE)),
                            cursor.getInt(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_QTY)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_TYPE)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_IMG)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_COST)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_DETAILS)),
                            cursor.getString(cursor.getColumnIndex(ProductDBHelper.Table.COLUMN_CREATE_AT)
                            ));
                }
                catch (Exception e){
                    String a =e.toString();
                    System.out.println(e);

                }
                if(p!=null){
                    products.add(p);
                }
            }
            return products;
        }finally {
            cursor.close();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(barcode);
        dest.writeString(price);
        dest.writeString(type);
        dest.writeString(imgName);
        dest.writeInt(qty);
        dest.writeString(cost);
        dest.writeString(details);
        dest.writeString(createAt);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new Error("Something impossible just happened");
        }
    }

    public static boolean isEquals(Product a, Product b){
        return (a.id.equals(b.id)) && (a.name.equals(b.name)) && (a.barcode.equals(b.barcode)) &&
                (a.price.equals(b.price)) && (a.type.equals(b.type)) && (a.imgName.equals(b.imgName)) &&
                (a.qty == b.qty) && (a.cost.equals(b.cost)) && (a.details.equals(b.details))
                && (a.createAt.equals(b.createAt));
    }

    public JSONObject toJSONObject(){
        JSONObject jsProduct = new JSONObject();

        try {
            jsProduct.put(Constant.KEY_JSON_PRODUCT_ID, this.id);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_NAME, this.name);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_BARCODE, this.barcode);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_PRICE, this.price);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_TYPE, this.type);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_IMG, this.imgName);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_QTY, this.qty);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_COST, this.cost);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_DETAILS, this.details);
            jsProduct.put(Constant.KEY_JSON_PRODUCT_CREATE_AT, this.createAt);
            return jsProduct;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }




}
