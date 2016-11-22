package com.passioncreativestudio.myandroidplayground;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CrescentoActivity extends AppCompatActivity {

    /*
    If you are using gradle then add these lines in build.gradle file at project level.

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }

    Add below lines in build.gradle at app level.

    compile 'com.github.developer-shivam:crescento:1.0.0'
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crescento);
    }
}
