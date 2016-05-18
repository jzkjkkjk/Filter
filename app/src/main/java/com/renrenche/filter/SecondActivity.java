package com.renrenche.filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.renrenche.filterlibrary.FilterItemModel;
import com.renrenche.filterlibrary.FilterMap;
import com.renrenche.filterlibrary.MultipleFilter;
import com.renrenche.filterlibrary.MultipleFilterAdapter;
import com.renrenche.filterlibrary.OnFilterItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends Activity {

    private MultipleFilter mFilter;
    private ListView mLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mFilter = (MultipleFilter) findViewById(R.id.filter);
        FilterMap data = new FilterMap();
        for (int i = 0; i < 3; i++) {
            String title = null;
            switch (i) {
                case 0:
                    title = "状态";
                    break;
                case 1:
                    title = "时间";
                    break;
                case 2:
                    title = "编号";
                    break;
            }
            List<FilterItemModel> value = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                FilterItemModel model = new FilterItemModel(title + "的内容" + j);
                value.add(model);
            }
            data.put(title, value);
        }
        mFilter.setAdapter(new MultipleFilterAdapter(data, new int[]{400, 400, 400}));
        mFilter.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onFilterItemClick(String category, FilterItemModel value) {
                Toast.makeText(SecondActivity.this, category + ":" + value.mValue, Toast.LENGTH_SHORT).show();
            }
        });

        mLv = (ListView) findViewById(R.id.lv);
        mLv.setAdapter(new Adapter(this));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
                Toast.makeText(SecondActivity.this, mLv.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
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
