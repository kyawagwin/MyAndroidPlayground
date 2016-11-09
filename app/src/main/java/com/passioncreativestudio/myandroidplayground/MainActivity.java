package com.passioncreativestudio.myandroidplayground;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button recyclerViewBtn;
    private Button CABBtn;
    private Button takePictureButton;
    private Button openMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewBtn = (Button) findViewById(R.id.activity_main_recyclerViewBtn);
        CABBtn = (Button) findViewById(R.id.activity_main_CABBtn);
        takePictureButton = (Button) findViewById(R.id.activity_main_takePictureButton);
        openMapButton = (Button) findViewById(R.id.activity_main_openMapButton);

        recyclerViewBtn.setOnClickListener(this);
        CABBtn.setOnClickListener(this);
        takePictureButton.setOnClickListener(this);
        openMapButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.activity_main_recyclerViewBtn:
                intent = new Intent(this, AlbumListActivity.class);
                startActivity(intent);
                return;
            case R.id.activity_main_CABBtn:
                intent = new Intent(this, CustomerListCABActivity.class);
                startActivity(intent);
                return;
            case R.id.activity_main_takePictureButton:
                intent = new Intent(this, TakePhotoActivity.class);
                startActivity(intent);
            case R.id.activity_main_openMapButton:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            default:
        }
    }
}
