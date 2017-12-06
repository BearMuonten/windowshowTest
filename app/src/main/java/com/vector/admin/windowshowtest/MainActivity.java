package com.vector.admin.windowshowtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatWindow floatWindow=new FloatWindow(this);
        floatWindow.setLayout(R.layout.windowlayout);
        floatWindow.show();
    }
}
