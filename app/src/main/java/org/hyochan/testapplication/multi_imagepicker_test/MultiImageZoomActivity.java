package org.hyochan.testapplication.multi_imagepicker_test;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.hyochan.testapplication.R;

import java.util.ArrayList;

public class MultiImageZoomActivity extends AppCompatActivity {

    private String TAG = "PinchZoomImgActivity";

    ViewPager viewPager;

    private ArrayList<MultiImageItem> arrayList;
    private MultiImageFullScreenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_img);

        // touchImageView = (TouchImageView) findViewById(R.id.touch_img);
        arrayList = (ArrayList<MultiImageItem>) getIntent().getSerializableExtra("myitems");

        viewPager = (ViewPager) findViewById(R.id.pager);

        int position = getIntent().getIntExtra("position", 0);

        adapter = new MultiImageFullScreenAdapter(this, arrayList);


        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
