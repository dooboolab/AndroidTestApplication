package org.hyochan.testapplication.multi_imagepicker_test;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import org.hyochan.testapplication.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hyochan on 2016-08-25.
 */
public class MultiImageGridAdapter extends BaseAdapter {

    private final String TAG = "MultiImageGridAdapter";

    private LayoutInflater layoutInflater;
    private ArrayList<MultiImageItem> items;
    private ViewHolder viewHolder;
    private Context context;

    private class ViewHolder {
        public ImageView imgView;
        public Button btnClose;
    }

    public MultiImageGridAdapter(Context context, ArrayList<MultiImageItem> items) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(MultiImageItem item){
        items.add(item);
    }

    @Override
    public MultiImageItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = layoutInflater.inflate(R.layout.multiimage_grid_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.btnClose = (Button) view.findViewById(R.id.btn_close);
            viewHolder.imgView = (ImageView) view.findViewById(R.id.img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        new MultiImageLoadTumbImgTask(context, viewHolder.imgView, items.get(i)).execute();

        viewHolder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 썸네일 삭제
                File tmpPath = new File(Environment.getExternalStorageDirectory(), "/TestApplication/thumb_" + items.get(i).getimageName());
                Log.d(TAG, "file delete : " + tmpPath);
                if (tmpPath.exists()) {
                    // 파일이 존재하면 삭제
                    tmpPath.delete();
                }


                // 2. 매인 이미지 삭제
                tmpPath = new File(Environment.getExternalStorageDirectory(), "TestApplication/" + items.get(i).getimageName());
                if(tmpPath.exists()){
                    tmpPath.delete();
                }
                items.remove(i);
                notifyDataSetChanged();
            }
        });

        viewHolder.imgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, MultiImageZoomActivity.class);
                intent.putExtra("myitems", items);
                intent.putExtra("position", i);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
