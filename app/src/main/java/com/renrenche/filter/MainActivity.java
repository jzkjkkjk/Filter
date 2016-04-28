package com.renrenche.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.renrenche.filterlibrary.FilterLayout;

public class MainActivity extends AppCompatActivity {

    private FilterLayout mFilterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilterLayout = (FilterLayout) findViewById(R.id.filter);
        mFilterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
    }
}
