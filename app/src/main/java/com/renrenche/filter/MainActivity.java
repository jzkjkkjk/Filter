package com.renrenche.filter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.renrenche.filterlibrary.FilterItemModel;
import com.renrenche.filterlibrary.MultipleFilter;
import com.renrenche.filterlibrary.MultipleFilterAdapter;
import com.renrenche.filterlibrary.OnFilterItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private MultipleFilter mFilter;
    private ListView mLv;

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

        mLv = (ListView) findViewById(R.id.lv);
        mLv.setAdapter(new Adapter(this));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, mLv.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private static class Adapter extends BaseAdapter {

        private Context mContext;

        public Adapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView item;
            if (convertView == null) {
                item = new TextView(mContext);
                item.setTextColor(Color.BLACK);
                item.setGravity(Gravity.CENTER);
                item.setPadding(40, 40, 0, 40);
            } else {
                item = (TextView) convertView;
            }
            item.setText(position + "");
            return item;
        }
    }
}
