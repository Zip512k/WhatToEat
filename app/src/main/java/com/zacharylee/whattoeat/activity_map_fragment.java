package com.zacharylee.whattoeat;

/**
 * Created by zepli on 16-07-05.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_map_fragment extends AppCompatActivity{

    private Map map = null;
    private MapGesture m_mapGesture;
    private TextView positionText;
    private Button applyButton, cancelButton;

    private MapFragment mapFragment = null;

    final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 0;
    final List<String> permissionsList = new ArrayList<String>();
    Double oldLong, oldLai;
    MapMarker initMarker = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        positionText = (TextView) findViewById(R.id.positionText);
        applyButton = (Button) findViewById(R.id.mapButton);
        cancelButton = (Button) findViewById(R.id.cancleButton);

        requestPermissions();

        applyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("position", positionText.getText().toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    private void requestPermissions(){

        permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissionsList.add(Manifest.permission.INTERNET);
        permissionsList.add(Manifest.permission.ACCESS_WIFI_STATE);

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

                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {

                    getOldPosition();
                    createMap();
                } else {

                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getOldPosition(){
        Intent intent = getIntent();
        String oldPosition = intent.getStringExtra("oldPosition");
        if (oldPosition != null){
            String[] data = oldPosition.split(",");
            if (data.length == 2) {
                positionText.setText(oldPosition);
                oldLai = Double.parseDouble(data[0]);
                oldLong = Double.parseDouble(data[1]);
            }
        }

    }

    private void createMap(){

        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapfragment);

        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error)
            {
                if (error == OnEngineInitListener.Error.NONE) {

                    map = mapFragment.getMap();
                    m_mapGesture = mapFragment.getMapGesture();
                    map.setCenter(new GeoCoordinate(49.196261, -123.004773, 0.0),
                            Map.Animation.NONE);
                    map.setZoomLevel(
                            (map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);

                    if (oldLai != null && oldLong != null) {
                        initMarker = new MapMarker();
                        initMarker.setCoordinate(new GeoCoordinate(oldLai, oldLong));
                        initMarker.setTitle("Old Position");
                        map.addMapObject(initMarker);
                    }

                    m_mapGesture.addOnGestureListener(MapOnGestureListener);

                } else {
                    System.out.println("ERROR: Cannot initialize Map Fragment " + error.name());
                }
            }
        });
    }

    private MapGesture.OnGestureListener MapOnGestureListener = new MapGesture.OnGestureListener.OnGestureListenerAdapter() {

        private MapMarker lastMarker = null;
        @Override
        public boolean onLongPressEvent(PointF point) {

            MapMarker positionMarker = new MapMarker();
            if (lastMarker != null) map.removeMapObject(lastMarker);
            positionMarker.setCoordinate(map.pixelToGeo(point));
            positionMarker.setTitle("New Position");
            map.addMapObject(positionMarker);
            lastMarker = positionMarker;

            Double longitude = new Double (map.pixelToGeo(point).getLongitude());
            Double latitude = new Double (map.pixelToGeo(point).getLatitude());
            positionText.setText(latitude.toString() + "," + longitude.toString());
            return true;

        }


    };

    }
