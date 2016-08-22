package com.zacharylee.whattoeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class activity_main_view extends AppCompatActivity {

    private final int ADD_NEW = 1, AMEND_DETAIL = 2;
    private String[] contentArray = new String[1000], statusArray = new String[1000], fileArray = new String[1000];
    private String[][] content = new String[1000][4];
    private String[] tempSubArray;
    private String tempFileArray;
    private SimpleAdapter adapter;
    private ListView listView;
    private int count = 0;
    int tempPosition;

    ArrayList<HashMap<String, String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton addNew = (FloatingActionButton) findViewById(R.id.addNew);
        View.OnClickListener handler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity_main_view.this,activity_event_detail.class);
                startActivityForResult(intent,ADD_NEW);

            }
        };
        addNew.setOnClickListener(handler);

        setListView();
    }

    private void setListView() {
        listView = (ListView) findViewById(R.id.listView);
        listData = new ArrayList<HashMap<String, String>>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                Intent intent = new Intent(activity_main_view.this, activity_event_detail.class);
                intent.putExtra("content",content[position]);
                intent.putExtra("photoUrl",fileArray[position]);
                intent.putExtra("position",position);
                startActivityForResult(intent,AMEND_DETAIL);
            }

        });

        initListView();
    }

    private void initListView() {
        for (int i = 0; i < count; i++) {
            HashMap<String, String> datum = new HashMap<String, String>();
            datum.put("content", contentArray[i]);
            datum.put("status", statusArray[i]);
            listData.add(datum);
        }
        adapter = new SimpleAdapter(this, listData, android.R.layout.simple_list_item_2,
                new String[]{"content", "status"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    private void updateListView(){

        if (adapter != null) adapter.notifyDataSetChanged();
        adapter = new SimpleAdapter(this, listData, android.R.layout.simple_list_item_2,
                new String[]{"content", "status"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main_view, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case ADD_NEW:
                if (resultCode == RESULT_OK && data != null) {
                    tempSubArray = data.getStringArrayExtra("content");
                    tempFileArray = data.getStringExtra("thumbnail");
                    content[count] = tempSubArray;
                    contentArray[count] = tempSubArray[0];
                    statusArray[count] = "未完成";
                    fileArray[count] = tempFileArray;
                    HashMap<String, String> datum = new HashMap<String, String>();
                    datum.put("content", contentArray[count]);
                    datum.put("status", statusArray[count]);
                    listData.add(datum);
                    updateListView();
                    count++;
                }
                break;
            case AMEND_DETAIL:
                if (resultCode == RESULT_OK && data != null) {
                    tempSubArray = data.getStringArrayExtra("content");
                    tempFileArray = data.getStringExtra("thumbnail");
                    tempPosition = data.getIntExtra("position", -1);
                    if (tempPosition != -1) {
                        content[tempPosition] = tempSubArray;
                        contentArray[tempPosition] = tempSubArray[0];
                        fileArray[tempPosition] = tempFileArray;
                    }
                    listData.get(tempPosition).put("content", contentArray[tempPosition]);
                    updateListView();
                }
            default:
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
