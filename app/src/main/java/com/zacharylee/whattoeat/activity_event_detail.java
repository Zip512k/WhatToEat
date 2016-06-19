package com.zacharylee.whattoeat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ZacharyLee on 16-06-18.
 */
public class activity_event_detail extends AppCompatActivity {

    String[] event_array = {"内容","日期","地点","人"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        TextView title = (TextView) findViewById(R.id.detail_title);
        ImageView eventImage = (ImageView) findViewById(R.id.event_image);

        ListView listView = (ListView) findViewById(R.id.detail_list);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, event_array);
        listView.setAdapter(adapter);

    }

}
