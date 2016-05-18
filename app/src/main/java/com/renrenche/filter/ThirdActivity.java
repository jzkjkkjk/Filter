package com.renrenche.filter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.renrenche.filterlibrary.Filter;
import com.renrenche.filterlibrary.FilterAdapter;
import com.renrenche.filterlibrary.FilterItemModel;
import com.renrenche.filterlibrary.OnFilterItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends Activity {

    private Filter mFilter;
    private ListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        mFilter = (Filter) findViewById(R.id.filter);
        String title = "测试";
        List<FilterItemModel> data = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            FilterItemModel model = new FilterItemModel(title + "的内容" + j);
            data.add(model);
        }
        mFilter.setTitle(title);
        mFilter.setAdapter(new FilterAdapter(data, R.layout.item_filter));
        mFilter.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onFilterItemClick(String category, FilterItemModel value) {
                Toast.makeText(ThirdActivity.this, category + ":" + value.mValue, Toast.LENGTH_SHORT).show();

            }
        });
        mLv = (ListView) findViewById(R.id.lv);
        mLv.setAdapter(new Adapter(this));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ThirdActivity.this, mLv.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mFilter.canGoBack()) {
            super.onBackPressed();
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
