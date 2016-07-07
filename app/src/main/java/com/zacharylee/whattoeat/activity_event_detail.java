package com.zacharylee.whattoeat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ZacharyLee on 16-06-18.
 */
public class activity_event_detail extends AppCompatActivity implements dialog_content_change.contentChangedListener {

    ArrayList<HashMap<String, String>> listData;
    private String[] titleArray = {"内容","日期","地点","人"}, subItemArray = {"新 添加","新 添加","新 添加","新 添加"};
    final List<String> permissionsList = new ArrayList<String>();
    private SimpleAdapter adapter;
    private ListView listView;
    private Uri fileUri;
    final int MAP_REQUEST = 10, IMAGE_REQUEST = 20, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0;
    static int MEDIA_TYPE_IMAGE = 1, MEDIA_TYPE_VIDEO = 2;
    ImageView eventImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        TextView title = (TextView) findViewById(R.id.detail_title);
        eventImage = (ImageView) findViewById(R.id.event_image);

        setListView();

        requestPermissions();

    }

    private View.OnClickListener imageClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
            File f = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(intent, IMAGE_REQUEST);

        }
    };

    private void setListView() {
        listView = (ListView) findViewById(R.id.detail_list);
        listData = new ArrayList<HashMap<String, String>>();
        initListView();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                switch (position) {
                    case 0:
                    case 3:
                        showContentChangeDialog(titleArray[position], subItemArray[position]);
                        break;
                    case 1:
                        showDatePickerDialog();
                        break;
                    case 2:
                        Intent intent = new Intent(activity_event_detail.this, activity_map_fragment.class);
                        intent.putExtra("oldPosition", subItemArray[position]);
                        startActivityForResult(intent, MAP_REQUEST);
                }
                
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case MAP_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    subItemArray[2] = data.getStringExtra("position");
                    updateListView();
                }
                break;
            case IMAGE_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    public void contentOnChanged(String type, String newContent){
        for (int i = 0; i < titleArray.length; i++){
            if (titleArray[i] == type) {
                subItemArray[i] = newContent;
                updateListView();
                break;
            }
        }
    }

    private void initListView(){
        for(int i = 0; i < titleArray.length; i++){
            HashMap<String,String> datum = new HashMap<String, String>();
            datum.put("title", titleArray[i]);
            datum.put("content", subItemArray[i]);
            listData.add(datum);
        }
        adapter = new SimpleAdapter(this, listData, android.R.layout.simple_list_item_2,
                new String[]{"title", "content"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

    }

    private void updateListView(){

        for(int i=0; i < titleArray.length; i++){
            listData.get(i).put("content",subItemArray[i]);
        }
        if (adapter != null) adapter.notifyDataSetChanged();
        adapter = new SimpleAdapter(this, listData, android.R.layout.simple_list_item_2,
                new String[]{"title", "content"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

    }

    private void showContentChangeDialog(String type, String oldContent) {
        DialogFragment newFragment = new dialog_content_change();
        newFragment = dialog_content_change.newInstance(type, oldContent);
        newFragment.show(this.getFragmentManager(), "change_content");
    }

    private void showDatePickerDialog() {

        final Calendar currentDate = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {

                Integer YEAR = new Integer(year);
                Integer MONTH = new Integer(month + 1);
                Integer DAY = new Integer(day);

                subItemArray[1] = YEAR.toString() + " - " + MONTH.toString() + " - " + DAY.toString();
                updateListView();

            }
        };

        DatePickerDialog dateDialog = new DatePickerDialog(this, dateSetListener, currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));

        dateDialog.show();

    }

    private void requestPermissions(){

        permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsList.add(Manifest.permission.CAMERA);
        ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                HashMap<String, Integer> perms = new HashMap<String, Integer>();

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    eventImage.setOnClickListener(imageClickListener);
                } else {

                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "eatapp");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                System.out.println("Eatapp failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
