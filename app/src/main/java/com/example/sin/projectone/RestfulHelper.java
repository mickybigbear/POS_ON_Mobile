package com.example.sin.projectone;

/**
 * Created by naki_ on 11/3/2016.
 */

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class RestfulHelper {
    Context context;
    String host ="http://127.0.0.1/"; //default URL (localhost)
    String paramStr = "";//set Parameter for sending request
    String httpMethod =""; //set httpMethod(GET, POST, PUT, DELETE)
    String api ="";//api location (Eg. host/api)
    AsyncHttpResponseHandler responseHandler;
    String responseStr="set0"; // response string for create JSON object
    public void setHost(String newHost){
        this.host = newHost.toLowerCase();
    }

    public void setParamStr(String paramStr){
        this.paramStr = paramStr;
    }

    public void setHttpMethod(String httpMethod){
        this.httpMethod = httpMethod;
    }

    public void setApi(String api){
        this.api = api;
    }

    public void setContext(Context c){
        this.context = c;
    }

    public void setResponse(){

    }
    public String getResponse(){
        return this.responseStr;
    }



    public void httpReq(){
        RequestParams params = new RequestParams();
        String[] createParams = this.paramStr.split(",");
        System.out.println(createParams.length);
        System.out.println(createParams[1]);
//        params.put("name", name);
        for (String value:createParams) {
            String[] temp = value.split(":");
            params.put(temp[0],temp[1]);
        }
        System.out.println(params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        responseHandler = new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // Hide Progress Dialog
//                prgDialog.hide();
                // JSON Object

                try {
                    responseStr = new String(response, "UTF-8");
                    System.out.println(responseStr);
                    System.out.println(response);
//                    JSONObject obj = new JSONObject(responseStr);
//                    str = obj.getString("Message");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // When the JSON response has status boolean value assigned with true

//                Toast.makeText(getApplicationContext(), "You are successfully registered!\n"+str, Toast.LENGTH_LONG).show();
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // Hide Progress Dialog
//                prgDialog.hide();
                // When Http response code is '404'
                    // {"Message":"Hello World !"}
                    responseStr = "{\"status\":\""+statusCode+"\"}";
            }
        };
            client.get(this.host+this.api,responseHandler);


    }



}
