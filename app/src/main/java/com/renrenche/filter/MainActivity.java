package com.renrenche.filter;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.renrenche.filterlibrary.FilterItemModel;
import com.renrenche.filterlibrary.MultipleFilter;
import com.renrenche.filterlibrary.MultipleFilterAdapter;
import com.renrenche.filterlibrary.OnFilterItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MultipleFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilter = (MultipleFilter) findViewById(R.id.filter);
        ArrayMap<String, List<FilterItemModel>> data = new ArrayMap<>();
        for (int i = 0; i < 3; i++) {
            String title = "测试" + i;
            List<FilterItemModel> value = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                String valueStr = title + "的内容" + j;
                FilterItemModel model = new FilterItemModel();
                model.mTitle = title;
                model.mValue = valueStr;
                value.add(model);
            }
            data.put(title, value);
        }
        mFilter.setAdapter(new MultipleFilterAdapter(data, new int[]{300, 400, 500}));
        mFilter.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onFilterItemClick(String category, String value) {
                Toast.makeText(MainActivity.this, category + ":" + value, Toast.LENGTH_SHORT).show();
            }
        });
        mFilter.setMask(findViewById(R.id.filter_mask));
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
