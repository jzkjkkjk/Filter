package com.renrenche.fabexpanablemenu;

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

import com.renrenche.filter.R;

public class FabMenuActivity extends Activity {

    private ListView mLv;
    private FabExpandableMenu mFabExpandableMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_menu);

        mLv = (ListView) findViewById(R.id.lv);
        mLv.setAdapter(new Adapter(this));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FabMenuActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });

        mFabExpandableMenu = (FabExpandableMenu) findViewById(R.id.fab_menu);
        mFabExpandableMenu.setSwitcherSrcId(R.mipmap.ic_apply_to_see);
        mFabExpandableMenu.setAdapter(new FabExpandableMenu.MenuAdapter(new String[]{"测试一", "测试二二", "测试三三三", "测试四四四四"}));
        mFabExpandableMenu.setOnItemClickListener(new FabExpandableMenu.OnItemClickListener() {
            @Override
            public void onItemClick(String title) {
                Toast.makeText(FabMenuActivity.this, title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mFabExpandableMenu.isMenuOpen()) {
            mFabExpandableMenu.menuClose();
        } else {
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
