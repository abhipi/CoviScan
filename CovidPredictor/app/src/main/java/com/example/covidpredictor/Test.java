package com.example.covidpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.IOException;

public class Test extends AppCompatActivity {
    Uri myUri;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Bundle extras = getIntent().getExtras();
        img=(ImageView) findViewById(R.id.imageView1);
        if (extras != null) {
            String imguri = extras.getString("URI");
             myUri = Uri.parse(extras.getString("URI"));
        }
        try {
            if (Build.VERSION.SDK_INT < 28) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), myUri);
                img.setImageBitmap(bitmap);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),  myUri);
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                img.setImageBitmap(bitmap);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}