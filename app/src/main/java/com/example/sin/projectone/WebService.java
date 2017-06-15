package com.example.sin.projectone;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


/**
 * Created by nanth on 11/11/2016.
 */
public class WebService {
    private WebService(){}

    private static AsyncHttpClient _Client = new AsyncHttpClient();
    public static void getAllProduct(JsonHttpResponseHandler handler){
        String url = Constant.URL_GET_ALL_PRODUCT+Constant.SHOP_ID;
        _Client.get(url,null, handler);
    }

    public static void sendTransaction(AsyncHttpResponseHandler handler, JSONObject transaction){
        String url = Constant.URL_SEND_TRANSACTION;
        String data = transaction.toString();
        StringEntity entity = null;
        try {
            entity = new StringEntity(data, "UTF-8");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {
        }
        _Client.post(ApplicationHelper.getAppContext(),url,entity,"application/json",handler);
    }

    public static void sendUpdateProduct(AsyncHttpResponseHandler handler, JSONObject product){
        String url = Constant.URL_SEND_UPDATE_PRODUCT;
        String data = product.toString();
        StringEntity entity = null;
        try {
            entity = new StringEntity(data);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {
        }
        _Client.put(ApplicationHelper.getAppContext(),url,entity,"application/json",handler);
    }

    public static void sendAddProduct(AsyncHttpResponseHandler handler, JSONObject product, File file){
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();
        try {
            params.put(Constant.KEY_REQUEST_PAREMS_PRODUCT_IMG_FILE, file, "image/jpg");
            params.put(Constant.KEY_REQUEST_PAREMS_PRODUCT_ADD, product);
        } catch(FileNotFoundException e) {}
        String url = Constant.URL_SEND_ADD_PRODUCT;
        String data = product.toString();
        StringEntity entity = null;
        try {
            entity = new StringEntity(data);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {
        }

        _Client.post(ApplicationHelper.getAppContext(),url,params,handler);
    }

    public static void postABC(AsyncHttpResponseHandler handler,String url){
        _Client.post(url,handler);
    }

    //* Example
    //  WebService.getABC(new AsyncHttpResponseHandler(){
    //      @Override
    //    public void onStart() {
    //        // Initiated the request
    //    }
    //
    //    @Override
    //    public void onSuccess(String response) {
    //        // Successfully got a response
    //    }
    //
    //    @Override
    //    public void onFailure(Throwable e, String response) {
    //        // Response failed :(
    //    }
    //
    //    @Override
    //    public void onFinish() {
    //        // Completed the request (either success or failure)
    //    }
    // })

}
