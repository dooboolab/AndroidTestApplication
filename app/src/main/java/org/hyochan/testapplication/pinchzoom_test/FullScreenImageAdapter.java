package org.hyochan.testapplication.pinchzoom_test;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.hyochan.testapplication.R;
import org.hyochan.testapplication.utils.LoadThumbImgTask;
import org.hyochan.testapplication.utils.TouchImageView;

import java.util.ArrayList;

/**
 * Created by hyochan on 2016-08-23.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<MyItem> arrayList;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity, ArrayList<MyItem> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fullscreen_image, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.img_display);
        btnClose = (Button) viewLayout.findViewById(R.id.btn_close);
        new LoadThumbImgTask(activity, imgDisplay)
                .execute(arrayList.get(position).getDrawable());

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}