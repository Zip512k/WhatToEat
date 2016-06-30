package com.zacharylee.whattoeat;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ZacharyLee on 16-06-18.
 */
public class activity_event_detail extends AppCompatActivity implements dialog_content_change.contentChangedListener {

    ArrayList<HashMap<String, String>> listData;
    private String[] titleArray = {"内容","日期","地点","人"}, subItemArray = {"添加","添加","添加","添加"};
    private activity_event_detail d_activity;
    private SimpleAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        TextView title = (TextView) findViewById(R.id.detail_title);
        ImageView eventImage = (ImageView) findViewById(R.id.event_image);

        setListView();

    }

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
                        showContentChangeDialog(titleArray[position]);
                        break;
                    case 1:
                        showDatePickerDialog();
                        break;
                }
                
            }
        });

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

    private void showContentChangeDialog(String type) {
        DialogFragment newFragment = new dialog_content_change();
        newFragment = dialog_content_change.newInstance(type);
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

}
