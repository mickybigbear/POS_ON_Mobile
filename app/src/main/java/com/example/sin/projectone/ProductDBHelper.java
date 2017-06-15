package com.example.sin.projectone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by nanth on 11/23/2016.
 */

public class ProductDBHelper extends SQLiteOpenHelper {
    private static ProductDBHelper productDBHelper;
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "pgms.db";
    private static ArrayList<Product> products = new ArrayList();

    public class Table{
        public static final String TABLE_PRODUCT = "product";

        public static final String COLUMN_P_ID = "productID"; // primary key
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BARCODE = "bacode"; // unique key
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_QTY = "qty";
        public static final String COLUMN_IMG = "imgName";
        public static final String COLUMN_COST = "cost";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_CREATE_AT = "createAt";

        public static final String COLUMN_S_ID = "shopID"; // dabasbase
        public static final String COLUMN_USER_ID = "userID";

        public static final String TABLE_TRANS = "transaction_table";
        public static final String COLUMN_TRANS_ID = "transactionID";
        public static final String COLUMN_TRANS_REF_ID ="refTransactionID";
        public static final String COLUMN_TRANS_SHOPID = "shopID";
        public static final String COLUMN_TRANS_USERNAME = "username";
        public static final String COLUMN_TRANS_TOTAL = "total";
        public static final String COLUMN_TRANS_DISCOUNT = "discount";
        public static final String COLUMN_TRANS_DISCOUNT_DETAIL = "discountDetail";
        public static final String COLUMN_TRANS_STATUS = "status";
        public static final String COLUMN_TRANS_CREATE_AT = "createAt";
        public static final String COLUMN_TRANS_UPDATE_AT = "updateAt";

        public static final String TABLE_TRANS_D = "transactionDetail";
        public static final String COLUMN_TRANS_D_ID ="transactionID";
        public static final String COLUMN_TRANS_D_NAME = "name";
        public static final String COLUMN_TRANS_D_PRICE = "price";
        public static final String COLUMN_TRANS_D_COST = "cost";
        public static final String COLUMN_TRANS_D_PRODUCT_ID ="productID";
        public static final String COLUMN_TRANS_D_QTY = "qty";
        public static final String COLUMN_TRANS_D_CREATE_AT = "createAt";
        public static final String COLUMN_TRANS_D_UPDATE_AT ="updateAt";

        private static final String TAG = "MyActivity";
    }
    private ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ProductDBHelper getInstance(Context context){
        if(productDBHelper==null){

            productDBHelper = new ProductDBHelper(context.getApplicationContext());
        }
        return productDBHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String DROP_PRODUCT = "DROP TABLE IF EXISTS " +Table.TABLE_PRODUCT ;
        String DROP_TRANS = "DROP TABLE IF EXISTS " +Table.TABLE_TRANS ;
        String DROP_TRANS_D = "DROP TABLE IF EXISTS " +Table.TABLE_TRANS_D ;
        String CREATE_TABLE_PRODUCT="CREATE TABLE "+Table.TABLE_PRODUCT +" ( " +
                Table.COLUMN_P_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                //Table.COLUMN_S_ID+" INTEGER, " +
                Table.COLUMN_BARCODE +" TEXT UNIQUE, " +
                Table.COLUMN_NAME+" TEXT, " +
                //Table.COLUMN_BRAND+" TEXT, " +
                Table.COLUMN_PRICE+" TEXT,"+
                Table.COLUMN_TYPE+" TEXT, " +
                Table.COLUMN_QTY+" INTEGER, "+
                Table.COLUMN_COST+" TEXT, "+
                Table.COLUMN_DETAILS +" TEXT, "+
                Table.COLUMN_CREATE_AT+" TEXT, "+
                Table.COLUMN_IMG+" TEXT)";

        String CREATE_TABLE_TRANS = "CREATE TABLE IF NOT EXISTS "+Table.TABLE_TRANS + " ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                Table.COLUMN_TRANS_ID+" INTEGER  , "+
                Table.COLUMN_TRANS_REF_ID+" INTEGER , "+
                Table.COLUMN_TRANS_USERNAME+" TEXT, " +
                Table.COLUMN_TRANS_TOTAL+" REAL, " +
                Table.COLUMN_TRANS_DISCOUNT+ " REAL, "+
                Table.COLUMN_TRANS_DISCOUNT_DETAIL + " TEXT, " +
                Table.COLUMN_TRANS_CREATE_AT + " TEXT, " +
                Table.COLUMN_TRANS_STATUS+" INTEGER," +
                "UNIQUE ( "+Table.COLUMN_TRANS_D_ID+" ) ON CONFLICT IGNORE)";

        String CREATE_TABLE_TRANS_D = "CREATE TABLE IF NOT EXISTS "+Table.TABLE_TRANS_D + " ( " +
//                Table.COLUMN_TRANS_SHOPID+" INTEGER , "+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                Table.COLUMN_TRANS_D_ID+" INTEGER, " +
                Table.COLUMN_TRANS_D_NAME+" TEXT, " +
                Table.COLUMN_TRANS_D_PRICE+" REAL, " +
                Table.COLUMN_TRANS_D_COST+" REAL, " +
                Table.COLUMN_TRANS_D_QTY+ " INTEGER, "+
                Table.COLUMN_TRANS_D_CREATE_AT + " TEXT, " +
                "UNIQUE ("+Table.COLUMN_TRANS_D_ID+", "+ Table.COLUMN_TRANS_D_NAME+") ON CONFLICT IGNORE" +
                ")";
//        db.execSQL(DROP_PRODUCT);
//        db.execSQL(DROP_TRANS); เอาออกดีกว่า เราลบแค่ครั้งแรกตอนเข้าแอพ , เ method นี้ทำงาน มากกว่า 1 ครั้ง ถ้าไปสั่ง new
//        db.execSQL(DROP_TRANS_D);
        log.d("DB",CREATE_TABLE_TRANS);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_TRANS);
        db.execSQL(CREATE_TABLE_TRANS_D);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



    public Product searchProductByBarCode(String bacode){
        String sql = "SELECT * FROM "+Table.TABLE_PRODUCT +
                " WHERE "+Table.COLUMN_BARCODE +" = '"+bacode+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()) {
            db.close();
            System.out.println(cursor.getString(cursor.getColumnIndex(Table.COLUMN_P_ID)));
            return Product.CursorToProduct(cursor);
        }
        else{
            db.close();
            return null;
        }
    }

    public ArrayList<Product> getAllProductFromDB(){
        ArrayList<Product> products;
            String sql = "SELECT * FROM "+Table.TABLE_PRODUCT;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            products = Product.CursorToProductArrayList(cursor);
            cursor.close();
            db.close();
        return products;
    }

    public Product searchProductByID(String id){
        Product p;
        String sql = "SELECT * FROM "+Table.TABLE_PRODUCT +
                " WHERE "+Table.COLUMN_P_ID +" = '"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()) {
            p = Product.CursorToProduct(cursor);
            db.close();
            cursor.close();
            return p;
        }
        else{
            cursor.close();
            db.close();
            return null;
        }

    }

    public void ShowListProduct(){
        String sql = "SELECT * FROM "+Table.TABLE_PRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        for(int i=0;i< cursor.getCount();i++){
            String a = cursor.getString(cursor.getColumnIndex(Table.COLUMN_P_ID));
            System.out.println(a);
            cursor.moveToNext();
        }
        return;
    }

    public void LoadProduct(JSONArray jsonArray){// bug insert null
        SQLiteDatabase db = this.getWritableDatabase();
        int numberErrorInsert = 0;
        long errorCheck;
        for(int i=0;i<jsonArray.length();i++){
            try {
                ContentValues values = new ContentValues();
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                values.put(Table.COLUMN_P_ID, jsonObj.getInt(Constant.KEY_JSON_PRODUCT_ID));
                values.put(Table.COLUMN_BARCODE, jsonObj.getString(Constant.KEY_JSON_PRODUCT_BARCODE));
                values.put(Table.COLUMN_QTY,jsonObj.getInt(Constant.KEY_JSON_PRODUCT_QTY));
                values.put(Table.COLUMN_NAME, jsonObj.getString(Constant.KEY_JSON_PRODUCT_NAME));
                values.put(Table.COLUMN_TYPE, jsonObj.getString(Constant.KEY_JSON_PRODUCT_TYPE));
                values.put(Table.COLUMN_PRICE, jsonObj.getString(Constant.KEY_JSON_PRODUCT_PRICE));
                values.put(Table.COLUMN_IMG, jsonObj.getString(Constant.KEY_JSON_PRODUCT_IMG));
                values.put(Table.COLUMN_COST, jsonObj.getString(Constant.KEY_JSON_PRODUCT_COST));
                values.put(Table.COLUMN_DETAILS, jsonObj.getString(Constant.KEY_JSON_PRODUCT_DETAILS));
                values.put(Table.COLUMN_CREATE_AT, jsonObj.getString(Constant.KEY_JSON_PRODUCT_CREATE_AT));
                errorCheck = db.insert(Table.TABLE_PRODUCT, null, values);
                if(errorCheck<0){
                    numberErrorInsert++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                numberErrorInsert++;
                continue;
            }
        }
        int showDebugError = numberErrorInsert;
       // values.put();
        db.close();

    }

    public void loadTransaction(JSONArray jsonArray){// bug insert null
        SQLiteDatabase db2 = this.getWritableDatabase();
        for(int i=0;i<jsonArray.length();i++){
            try {
                ContentValues values2 = new ContentValues();
                JSONObject jsonObj = jsonArray.getJSONObject(i);
//                Log.d(Table.TAG, jsonObj.toString());

                values2.put(Table.COLUMN_TRANS_ID, jsonObj.getInt(Table.COLUMN_TRANS_ID));
                values2.put(Table.COLUMN_TRANS_REF_ID, jsonObj.getInt(Table.COLUMN_TRANS_REF_ID));
                values2.put(Table.COLUMN_TRANS_USERNAME, jsonObj.getString(Table.COLUMN_TRANS_USERNAME));
                values2.put(Table.COLUMN_TRANS_TOTAL,jsonObj.getDouble(Table.COLUMN_TRANS_TOTAL));
                values2.put(Table.COLUMN_TRANS_DISCOUNT, jsonObj.getDouble(Table.COLUMN_TRANS_DISCOUNT));
                values2.put(Table.COLUMN_TRANS_DISCOUNT_DETAIL, jsonObj.getString(Table.COLUMN_TRANS_DISCOUNT_DETAIL));
                values2.put(Table.COLUMN_TRANS_STATUS, jsonObj.getString(Table.COLUMN_TRANS_STATUS));
                values2.put(Table.COLUMN_TRANS_CREATE_AT, jsonObj.getString(Table.COLUMN_TRANS_CREATE_AT));
                db2.insert(Table.TABLE_TRANS, null, values2);
                System.out.println("Insert "+values2.getAsString(Table.COLUMN_TRANS_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // values.put();
        db2.close();

    }

    public void loadTransactionDetail(JSONArray jsonArray){// bug insert null
        SQLiteDatabase db2 = this.getWritableDatabase();
        for(int i=0;i<jsonArray.length();i++){
            try {
                ContentValues values2 = new ContentValues();
                JSONObject jsonObj = jsonArray.getJSONObject(i);
//                Log.d(Table.TAG, jsonObj.toString());

                values2.put(Table.COLUMN_TRANS_D_ID, jsonObj.getInt(Table.COLUMN_TRANS_D_ID));
                values2.put(Table.COLUMN_TRANS_D_NAME, jsonObj.getString(Table.COLUMN_TRANS_D_NAME));
                values2.put(Table.COLUMN_TRANS_D_PRICE, jsonObj.getDouble(Table.COLUMN_TRANS_D_PRICE));
                values2.put(Table.COLUMN_TRANS_D_COST, jsonObj.getDouble(Table.COLUMN_TRANS_D_COST));
                values2.put(Table.COLUMN_TRANS_D_QTY, jsonObj.getInt(Table.COLUMN_TRANS_D_QTY));
                values2.put(Table.COLUMN_TRANS_D_CREATE_AT,jsonObj.getString(Table.COLUMN_TRANS_D_CREATE_AT));

                db2.insert(Table.TABLE_TRANS_D, null, values2);
                System.out.println("Insert "+values2.getAsString(Table.COLUMN_TRANS_D_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // values.put();
        db2.close();

    }

    public JSONArray getTrans(){
        JSONArray transDetailList = new JSONArray();
        String sql = "SELECT tt.transactionID,tt.username,tt.total,tt.createAt FROM transaction_table tt";
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        for(int i=0;i< cursor.getCount();i++){
            JSONObject transDetail = new JSONObject();
            try {
                transDetail.put(Table.COLUMN_TRANS_ID,cursor.getString(cursor.getColumnIndex(Table.COLUMN_TRANS_ID)));
                transDetail.put(Table.COLUMN_TRANS_USERNAME,cursor.getString(cursor.getColumnIndex(Table.COLUMN_TRANS_USERNAME)));
                transDetail.put(Table.COLUMN_TRANS_TOTAL,cursor.getString(cursor.getColumnIndex(Table.COLUMN_TRANS_TOTAL)));
                transDetail.put(Table.COLUMN_TRANS_CREATE_AT,cursor.getString(cursor.getColumnIndex(Table.COLUMN_TRANS_CREATE_AT)));
                transDetailList.put(transDetail);
                cursor.moveToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return  transDetailList;
    }

    public void UpdateProduct(JSONObject jsonObject){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Table.COLUMN_P_ID, jsonObject.getInt(Constant.KEY_JSON_PRODUCT_ID));
            values.put(Table.COLUMN_BARCODE, jsonObject.getString(Constant.KEY_JSON_PRODUCT_BARCODE));
            values.put(Table.COLUMN_QTY,jsonObject.getInt(Constant.KEY_JSON_PRODUCT_QTY));
            values.put(Table.COLUMN_NAME, jsonObject.getString(Constant.KEY_JSON_PRODUCT_NAME));
            values.put(Table.COLUMN_TYPE, jsonObject.getString(Constant.KEY_JSON_PRODUCT_TYPE));
            values.put(Table.COLUMN_PRICE, jsonObject.getString(Constant.KEY_JSON_PRODUCT_PRICE));
            values.put(Table.COLUMN_IMG, jsonObject.getString(Constant.KEY_JSON_PRODUCT_IMG));
            values.put(Table.COLUMN_COST, jsonObject.getString(Constant.KEY_JSON_PRODUCT_COST));
            values.put(Table.COLUMN_DETAILS, jsonObject.getString(Constant.KEY_JSON_PRODUCT_DETAILS));
            values.put(Table.COLUMN_CREATE_AT, jsonObject.getString(Constant.KEY_JSON_PRODUCT_CREATE_AT));
            db.update(Table.TABLE_PRODUCT, values, Table.COLUMN_P_ID+ " = "
                    +jsonObject.getInt(Constant.KEY_JSON_PRODUCT_ID),null );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void insertProduct(JSONObject jsonObject){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Table.COLUMN_P_ID, jsonObject.getInt(Constant.KEY_JSON_PRODUCT_ID));
            values.put(Table.COLUMN_BARCODE, jsonObject.getString(Constant.KEY_JSON_PRODUCT_BARCODE));
            values.put(Table.COLUMN_QTY,jsonObject.getInt(Constant.KEY_JSON_PRODUCT_QTY));
            values.put(Table.COLUMN_NAME, jsonObject.getString(Constant.KEY_JSON_PRODUCT_NAME));
            values.put(Table.COLUMN_TYPE, jsonObject.getString(Constant.KEY_JSON_PRODUCT_TYPE));
            values.put(Table.COLUMN_PRICE, jsonObject.getString(Constant.KEY_JSON_PRODUCT_PRICE));
            values.put(Table.COLUMN_IMG, jsonObject.getString(Constant.KEY_JSON_PRODUCT_IMG));
            values.put(Table.COLUMN_COST, jsonObject.getString(Constant.KEY_JSON_PRODUCT_COST));
            values.put(Table.COLUMN_DETAILS, jsonObject.getString(Constant.KEY_JSON_PRODUCT_DETAILS));
            values.put(Table.COLUMN_CREATE_AT, jsonObject.getString(Constant.KEY_JSON_PRODUCT_CREATE_AT));
            db.insert(Table.TABLE_PRODUCT, null, values );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void UpdateProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Table.COLUMN_P_ID, product.id);
            values.put(Table.COLUMN_BARCODE, product.barcode);
            values.put(Table.COLUMN_QTY,product.qty);
            values.put(Table.COLUMN_NAME, product.name);
            values.put(Table.COLUMN_TYPE, product.type);
            values.put(Table.COLUMN_PRICE, product.id);
            values.put(Table.COLUMN_IMG, product.imgName);
            values.put(Table.COLUMN_COST, product.cost);
            values.put(Table.COLUMN_DETAILS, product.details);
            values.put(Table.COLUMN_CREATE_AT, product.createAt);
            db.update(Table.TABLE_PRODUCT, values, Table.COLUMN_P_ID+ " = "
                    +product.id,null );
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public JSONObject getJSONTransaction(ArrayList<Product> products, String detail, float discount, float total){
        JSONObject transation = new JSONObject();
        JSONObject obj;
        JSONArray productList = new JSONArray();
        if(!products.isEmpty()&& products.size()>0){
            try {
                transation.put(Constant.KEY_JSON_USERID,Constant.USER_ID);
                transation.put(Constant.KEY_JSON_SHOPID,Constant.SHOP_ID);
                transation.put(Constant.KEY_JSON_DETAIL_DISCOUNT, detail);
                transation.put(Constant.KEY_JSON_DISCOUNT, discount);
                transation.put(Constant.KEY_JSON_TOTAL, total);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(Product p: products){
                try {
                    obj = new JSONObject();
                    obj.put(Constant.KEY_JSON_PRODUCT_ID, p.id);
                    obj.put(Constant.KEY_JSON_PRODUCT_QTY, p.qty);
                    productList.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                transation.put(Constant.KEY_JSON_PRODUCTLIST,productList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return transation;
        }
        else{
            return null;
        }

    }
    public boolean getDailyReport(String date){
        String sql = "SELECT td.name, td.qty FROM transactionDetail td WHERE createAt like '"+date+"%' ORDER BY name";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.getCount() > 0;
    }
    public JSONArray getDailyDetail(String date){
        JSONArray transDetailList = new JSONArray();
        String sql = "SELECT SUM(tt.total) AS total, SUM(tt.discount) AS discount FROM transaction_table tt WHERE createAt like '"+date+"%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        for(int i=0;i< cursor.getCount();i++){
            JSONObject transDetail = new JSONObject();
            try {
                transDetail.put("total",cursor.getString(cursor.getColumnIndex("total")));
                transDetail.put("discount",cursor.getString(cursor.getColumnIndex("discount")));
                transDetailList.put(transDetail);
                cursor.moveToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return  transDetailList;
    }

    public  Cursor getTransaction(){
        String sql = "SELECT tt._id, tt.transactionID,tt.total,tt.createAt, tt.username, tt.discount FROM transaction_table tt";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return  cursor;
    }
    public Cursor getTransactionDetail(String whereCol){
        String sql = "SELECT _id, name, price,qty FROM transactionDetail WHERE transactionID = "+whereCol;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return  cursor;
    }

    public Cursor getDailyEmployee(String createAt){
        String sql = "SELECT _id,username, SUM(tt.total) AS total, SUM(tt.discount) AS discount FROM transaction_table tt  WHERE createAt like '"+createAt+"%' GROUP BY username";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return  cursor;
    }

    public Cursor getDailyProduct(String createAt){
        String sql = "SELECT _id,td.name,SUM(td.qty) AS qty FROM transactionDetail td WHERE createAt like '"+createAt+"%' GROUP BY name ORDER BY name";
        Log.d("sql", sql);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return  cursor;
    }

    public Cursor getTopCursor(String type){
        String sql= "";
        if(type.equals("week")){
            sql = "SELECT _id,td.name,SUM(td.qty) AS qty FROM transactionDetail td  WHERE createAt BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime') GROUP BY name  ORDER BY qty DESC LIMIT 10";
        }
        else{
             sql = "SELECT _id,td.name,SUM(td.qty) AS qty FROM transactionDetail td  WHERE createAt BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime') GROUP BY name  ORDER BY qty DESC LIMIT 10";
        }
        Log.d("sql", sql);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return  cursor;
    }
    public JSONArray getTopDetail(String type){
        String sql= "";
        JSONArray transDetailList = new JSONArray();
        if(type.equals("week")){
            sql = "SELECT _id,td.name,SUM(td.qty) AS qty FROM transactionDetail td  WHERE createAt BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime') GROUP BY name  ORDER BY qty DESC LIMIT 10";
        }
        else{
            sql = "SELECT _id,td.name,SUM(td.qty) AS qty FROM transactionDetail td  WHERE createAt BETWEEN datetime('now', 'start of month') AND datetime('now', 'localtime') GROUP BY name  ORDER BY qty DESC LIMIT 10";
        }
        Log.d("sql", sql);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        for(int i=0;i< cursor.getCount();i++){
            JSONObject transDetail = new JSONObject();
            try {
                transDetail.put("name",cursor.getString(cursor.getColumnIndex("name")));
                transDetail.put("qty",cursor.getString(cursor.getColumnIndex("qty")));
                transDetailList.put(transDetail);
                cursor.moveToNext();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  transDetailList;
    }




}
