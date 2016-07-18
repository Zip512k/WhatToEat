package com.zacharylee.whattoeat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by zepli on 16-07-18.
 */
public class activity_view_image extends AppCompatActivity{

    private ImageView image;
    Intent intent;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_image);
        image = (ImageView) findViewById(R.id.fullImage);

        intent = getIntent();
        url = intent.getStringExtra("url");
        loadLocalImage(url);

    }

    private void loadLocalImage(String fileName) {
        File imgFile = new  File(fileName);

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(myBitmap);

        }
        else {
            image.setImageResource(R.drawable.default_image);
        }
    }
}
