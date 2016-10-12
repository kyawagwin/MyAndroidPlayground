package com.passioncreativestudio.myandroidplayground;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button recyclerViewBtn;
    private Button CABBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewBtn = (Button) findViewById(R.id.activity_main_recyclerViewBtn);
        CABBtn = (Button) findViewById(R.id.activity_main_CABBtn);

        recyclerViewBtn.setOnClickListener(this);
        CABBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.activity_main_recyclerViewBtn:
                intent = new Intent(this, AlbumListActivity.class);
                startActivity(intent);
                finish();
                return;
            case R.id.activity_main_CABBtn:
                intent = new Intent(this, CustomerListCABActivity.class);
                startActivity(intent);
                finish();
                return;
            default:
        }
    }
}
