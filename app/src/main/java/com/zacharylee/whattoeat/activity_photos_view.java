package com.zacharylee.whattoeat;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by zepli on 16-08-30.
 */
public class activity_photos_view extends AppCompatActivity {

    private GridView photoView;
    private Cursor imageCursor;
    private int count, index;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gridview);

        getInternalImages();
        photoView = (GridView) findViewById(R.id.photoGridView);
        photoView.setAdapter(new image_adapter(this));

    }

    private void getInternalImages() {

        String[] image = {MediaStore.Images.Media.DATA};
        imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image, null, null, null);
        index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        count = imageCursor.getCount();

    }

}

public class image_adapter extends BaseAdapter {

    private Context m_Context;

    public image_adapter(Context initActivity) {
        m_Context = initActivity;
    }
    public int getCount() {
        return count;
    }

}

