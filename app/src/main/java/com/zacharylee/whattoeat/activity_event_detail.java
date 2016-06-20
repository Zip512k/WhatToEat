package com.zacharylee.whattoeat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZacharyLee on 16-06-18.
 */
public class activity_event_detail extends AppCompatActivity {

    ArrayList<HashMap<String, String>> listData;
    private String[] titleArray = {"内容","日期","地点","人"}, subItemArray = {"添加","添加","添加","添加"};
    private activity_event_detail d_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        TextView title = (TextView) findViewById(R.id.detail_title);
        ImageView eventImage = (ImageView) findViewById(R.id.event_image);

        setListView();

    }

    private void setListView() {
        ListView listView = (ListView) findViewById(R.id.detail_list);
        listData = new ArrayList<HashMap<String, String>>();
        updateListView();

        SimpleAdapter adapter = new SimpleAdapter(this, listData, android.R.layout.simple_list_item_2,
                new String[]{"title", "content"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                switch (position) {
                    case 0:
                        finish();
                        break;
                }
                //String main = listView.getSelectedItem().toString();
            }
        });

    }

    private void updateListView(){
        for(int i=0; i < titleArray.length; i++){
            HashMap<String,String> datum = new HashMap<String, String>();
            datum.put("title", titleArray[i]);
            datum.put("content", subItemArray[i]);
            listData.add(datum);
        }
    }

}
