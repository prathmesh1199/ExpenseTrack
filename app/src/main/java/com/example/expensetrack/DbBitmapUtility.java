package com.example.expensetrack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class DbBitmapUtility {

    public  byte[] getBytes(Bitmap bitmap) {

        Log.d("h5", "submit: ");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        Log.d("h6", "submit: ");

        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public  Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
