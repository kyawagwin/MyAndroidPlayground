package com.passioncreativestudio.myandroidplayground;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_TAKE_PICTURE = 1;

    private Button takePictureButton;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        imageView = (ImageView) findViewById(R.id.activity_take_picture_imageView);
        takePictureButton = (Button) findViewById(R.id.activity_take_picture_button);
        takePictureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extra.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
        }
    }
}
