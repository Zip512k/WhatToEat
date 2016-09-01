package com.zacharylee.whattoeat;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by zepli on 16-08-30.
 */
public class activity_photos_view extends AppCompatActivity {

    private GridView photoView;
    private Cursor imageCursor;
    public int count, index;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gridview);

        getInternalImages();
        photoView = (GridView) findViewById(R.id.photoGridView);
        photoView.setAdapter(new image_adapter(this));

        photoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(activity_photos_view.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getInternalImages() {

        String[] image = {MediaStore.Images.Thumbnails._ID};
        imageCursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, image, null, null, null);
        index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
        count = imageCursor.getCount();

    }

    public class image_adapter extends BaseAdapter {

        private Context photoView;

        public image_adapter(Context initActivity) {
            photoView = initActivity;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image;
            if (convertView == null) {
                image = new ImageView(photoView);
                imageCursor.moveToPosition(position);
                int id = imageCursor.getInt(index);
                image.setImageURI(Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""+ id));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setLayoutParams(new GridView.LayoutParams(430, 430));
                image.setPadding(8, 8, 8, 8);
            }
            else {
                image = (ImageView) convertView;
            }
            return image;
        }

    }

}



