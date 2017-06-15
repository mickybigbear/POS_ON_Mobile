package com.example.sin.projectone;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nanth on 12/2/2016.
 */

public class ImgManager {

    private static ImgManager imgManager;

    private ImgManager(){

    }

    public static ImgManager getinstance(){
        if(imgManager==null){
            imgManager = new ImgManager();
        }
        return imgManager;
    }

    public Bitmap loadImageFromStorage(String imgName){
        String path = ApplicationHelper.getAppContext().getApplicationInfo().dataDir+"/app_"+Constant.FOLDER_PHOTO;
        System.out.println("Path :" + path);
        try {
            File f=new File(path, imgName);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            return bitmap;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public File saveImgToInternalStorage(Bitmap bitmapImage,String imgName){
        ContextWrapper cw = new ContextWrapper(ApplicationHelper.getAppContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Constant.FOLDER_PHOTO, Context.MODE_PRIVATE);
        // Create imageDir
        File imgFile = new File(directory,imgName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imgFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return directory.getAbsolutePath();
        return imgFile;
    }



//    public void dispatchTakePictureIntent(Fragment fragment) {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
//            fragment.startActivityForResult(takePictureIntent, Constant.REQUEST_IMAGE_CAPTURE);
//        }
//    }
//
//    public void dispatchTakePictureIntent(Activity activity) {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
//            activity.startActivityForResult(takePictureIntent, Constant.REQUEST_IMAGE_CAPTURE);
//        }
//    }


}
