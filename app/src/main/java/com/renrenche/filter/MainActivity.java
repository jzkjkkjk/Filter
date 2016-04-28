package com.renrenche.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.renrenche.filterlibrary.FilterLayout;

public class MainActivity extends AppCompatActivity {

    private FilterLayout mFilterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilterLayout = (FilterLayout) findViewById(R.id.filter);
    }
}
