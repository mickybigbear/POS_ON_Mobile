package com.example.sin.projectone.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.sin.projectone.ImgManager;
import com.example.sin.projectone.R;

import java.util.ArrayList;

public class SaveQRCode extends AppCompatActivity {
    private ImgManager imgManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_qrcode);
        // Get intent, action and MIME type
        imgManager = ImgManager.getInstance();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent);
                // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent);
                // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }
    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            String TAG = "imhRI";
            Log.d(TAG, "handleSendImage: "+imageUri.getPath());
            Log.d(TAG, "handleSendImage: "+imageUri.getUserInfo());
            Log.d(TAG, "handleSendImage: "+imageUri.getHost());
            imgManager.saveImgURIToInternalStorage(imageUri, "xxxx", this);
            Bitmap myBitmap = imgManager.loadImageFromStorage("xxxx");
            ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

            myImage.setImageBitmap(myBitmap);
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
}
