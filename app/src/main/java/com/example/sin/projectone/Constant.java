package com.example.sin.projectone;

/**
 * Created by nanth on 11/24/2016.
 */

public class Constant {
    private Constant(){

    }

//    public static int SHOP_ID = 34;
//    public static int USER_ID = 35; // debug test

    public static int SHOP_ID;
    public static int USER_ID; // debug test

    //Key json
    public final static String KEY_JSON_PRODUCTLIST = "productList";
    public final static String KEY_JSON_PRODUCT_ID = "productID";
    public final static String KEY_JSON_PRODUCT_BARCODE = "barcode";
    public final static String KEY_JSON_PRODUCT_QTY = "qty";
    public final static String KEY_JSON_PRODUCT_NAME = "name";
    public final static String KEY_JSON_PRODUCT_TYPE = "type";
    public final static String KEY_JSON_PRODUCT_PRICE = "price";
    public final static String KEY_JSON_PRODUCT_IMG = "imgName";
    public final static String KEY_JSON_PRODUCT_COST = "cost";
    public final static String KEY_JSON_PRODUCT_DETAILS = "details";
    public final static String KEY_JSON_PRODUCT_CREATE_AT = "createAt";
    public final static String KEY_JSON_SHOPID = "shopID";
    public final static String KEY_JSON_DETAIL_DISCOUNT = "discountDetatil";
    public final static String KEY_JSON_DISCOUNT = "discount";
    public final static String KEY_JSON_TOTAL = "total";
    public final static String KEY_JSON_TRANSACTIONID = "transactionID";


    public final static String KEY_JSON_USERID = "userID";


    //
    // Permission
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    //
    //url web Service
    public final static String URL_SERVER                   = "http://192.168.1.34:3001";
    public final static String URL_GET_ALL_PRODUCT          = URL_SERVER+"/api/product/";
    public final static String URL_SEND_TRANSACTION         = URL_SERVER+"/api/transaction/";
    public final static String URL_GET_TRANSACTION_DETAIL   = URL_SERVER+"/api/transactionDetail/";
    public final static String URL_SEND_UPDATE_PRODUCT      = URL_SERVER+"/api/product/";
    public final static String URL_SEND_ADD_PRODUCT         = URL_SERVER+"/api/product2/";
    // Tag Fragment
    public final static String TAG_FRAGMENT_ITEM_EDIT = "tag_fragment_item_edit";
    public final static String TAG_FRAGMENT_ITEM_ADD = "tag_fragment_item_add";
    public final static String TAG_FRAGMENT_ITEM_VIEW = "tag_fragment_item_view";
    public final static String TAG_FRAGMENT_RECEIPT_MAIN = "tag_receipt_main";
    public final static String TAG_FRAGMENT_PAYMENT_MAIN = "tag_fragment_payment_main";
    public final static String TAG_FRAGMENT_PAYMENT_END = "tag_fragment_payment_end";
    public final static String TAG_FRAGMENT_ITEM_MAIN = "tag_fragment_item_main";
    public final static String TAG_FRAGMENT_SCAN_PAYMENT = "tag_fragment_scanpayment";
    public final static String TAG_FRAGMENT_SCAN_BARCODE = "tag_fragment_scan_barcode";
    public final static String TAG_FRAGMENT_DIALOG_ALERT = "tag_fragment_dialog_alert";
    public final static String TAG_FRAGMENT_DIALOG_PRODUCT_DETAIL = "tag_fragment_dialog_alert";
    // Key Intent
    public final static String KEY_INTENT_PRODUCT = "key_intent_product";
    public final static String KEY_INTENT_BARCODE = "key_intetn_barcode";
    // Key Bundle
    public final static String KEY_BUNDLE_ARRAYLIST_PRODUCT = "key_bundle_array_list_product";
    public final static String KEY_BUNDLE_MESSAGE_DIALOG = "key_bundle_message_dialog";
    public final static String KEY_BUNDLE_TITLE_DIALOG = "key_bundle_title_dialog";
    public final static String KEY_BYNDLE_HAS_OK_CANCEL_DIALOG = "key_bundle_ok_cancel_dialog";
    public final static String KEY_BYNDLE_HAS_OK_DIALOG = "key_bundle_ok_dialog";
    public final static String KEY_BUNDLE_PRODUCT= "key_bundle_product";
    // Request code
    public final static int REQUEST_CODE_PRODUCT_PAYMENT_DIALOG          = 1000001;
    public final static int RESULT_CODE_PRODUCT_PAYMENT_DIALOG_SUBMIT    = 1000002;
    public final static int RESULT_CODE_PRODUCT_PAYMENT_DIALOG_CANCEL    = 1000003;

    public final static int REQUEST_CODE_PRODUCT_DETAIL_DIALOG           = 3000001;


    public final static int REQUEST_CODE_OK_CANCEL                       = 2000001;
    public final static int RESULT_CODE_OK                               = 2000002;
    public final static int RESULT_CODE_CANCEL                           = 2000003;

    public final static int REQUEST_CODE_BARCODE                         = 4000001;
    public final static int RESULT_CODE_BARCODE                          = 4000002;
    //message
    public final static String MESSAGE_ALERT_PRODUCT_SCAN_FIRST = "Please scan the product first";

    // Key param
    public final static String KEY_REQUEST_PAREMS_PRODUCT_ADD = "key_request_param_product_add";
    public final static String KEY_REQUEST_PAREMS_PRODUCT_IMG_FILE = "key_request_param_img_product";

    // other
    // path to /data/data/com.example.sin.projectone/app_productImg
    public final static String FOLDER_PHOTO = "productImg";
    public final static int PRODUCT_ID_INSERT_TEMP = 0;
    public final static String HEAD_NAME_IMG = "img_";
    public final static String CREATE_AT_TEMP = "createAt_temp";
    public final static String IMG_NAME_TEMP = Constant.HEAD_NAME_IMG+"temp.png";




}
