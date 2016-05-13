package com.renrenche.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.renrenche.filterlibrary.Filter;

public class MainActivity extends AppCompatActivity {

    private Filter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilter = (Filter) findViewById(R.id.filter);
    }

    @Override
    public void onBackPressed() {
        if (!mFilter.isAnimating()) {
            if (mFilter.isOpened()) {
                mFilter.close();
            } else {
                super.onBackPressed();
            }
        }
    }
}
