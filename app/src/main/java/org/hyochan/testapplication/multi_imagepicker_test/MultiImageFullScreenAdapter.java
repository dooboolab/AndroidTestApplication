package org.hyochan.testapplication.multi_imagepicker_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.hyochan.testapplication.R;
import org.hyochan.testapplication.utils.TouchImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by hyochan on 2016-08-23.
 */
public class MultiImageFullScreenAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<MultiImageItem> arrayList;
    private LayoutInflater inflater;

    // constructor
    public MultiImageFullScreenAdapter(Activity activity, ArrayList<MultiImageItem> arrayList) {
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
        View viewLayout = inflater.inflate(R.layout.fullscreen_image, container, false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.img_display);
        btnClose = (Button) viewLayout.findViewById(R.id.btn_close);
        // new MultiImageLoadTumbImgTask(activity, imgDisplay, arrayList.get(position)).execute();
        try{
            final InputStream imageStream = activity.getContentResolver().openInputStream(Uri.parse(arrayList.get(position).getStrUri()));
            imgDisplay.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            ((ViewPager) container).addView(viewLayout);
        } catch (FileNotFoundException e){
            Log.d("tag", "exception : " + e.getMessage());
        }

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}