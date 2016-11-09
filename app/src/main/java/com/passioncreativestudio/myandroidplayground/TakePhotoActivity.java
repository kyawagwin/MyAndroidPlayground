package com.passioncreativestudio.myandroidplayground;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_TAKE_PICTURE = 1;
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";

    private Button uploadImageButton;
    private Button takePictureButton;
    private ImageView imageView;

    private String uploadImageUrl = "http://express-api-sample-kyawagwin.c9users.io/imageupload";
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        imageView = (ImageView) findViewById(R.id.activity_take_picture_imageView);
        uploadImageButton = (Button) findViewById(R.id.activity_take_picture_uploadImage);
        uploadImageButton.setOnClickListener(this);
        takePictureButton = (Button) findViewById(R.id.activity_take_picture_button);
        takePictureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.activity_take_picture_button)
            dispatchTakePictureIntent();
        else if(id == R.id.activity_take_picture_uploadImage)
            uploadImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            imageBitmap = (Bitmap) extra.get("data");
            imageView.setImageBitmap(imageBitmap);
            uploadImageButton.setVisibility(View.VISIBLE);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
        }
    }

    private void uploadImage() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait...");

        StringRequest sr = new StringRequest(Request.Method.POST,
                uploadImageUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Toast.makeText(TakePhotoActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(TakePhotoActivity.this, "Image upload error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imageStr = getStringImage(imageBitmap);
                String name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                Map<String, String> params = new Hashtable<>();
                params.put(KEY_IMAGE, imageStr);
                params.put(KEY_NAME, name);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(sr);
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
