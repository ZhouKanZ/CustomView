package com.zkqueen.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoukan on 2018/8/6.
 *
 * @desc:
 */

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTitle;
    RecyclerView.Adapter adapter;
    List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titles.add("KEEP RUNNING");
        titles.add("ANGLE SELECTOR");
        titles.add("ROTATE BITMAP");

        rvTitle = findViewById(R.id.rv_title);
        rvTitle.setLayoutManager(new LinearLayoutManager(this));
        rvTitle.setAdapter(new CommonAdapter<String>(this, R.layout.item_main, titles) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                holder.setText(R.id.tv_title, titles.get(position));
                holder.setOnClickListener(R.id.tv_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        switch (position) {
                            case 0:
                                i.setClass(MainActivity.this, KeepButtonActivity.class);
                                break;
                            case 1:
                                i.setClass(MainActivity.this, PointerActivity.class);
                                break;
                            case 2:
                                i.setClass(MainActivity.this, BitmapRotateActivity.class);
                                break;
                        }
                        startActivity(i);
                    }
                });
            }
        });

    }
}
