package org.hyochan.testapplication.pinchzoom_test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.hyochan.testapplication.R;
import org.hyochan.testapplication.utils.LoadThumbImgTask;

import java.util.ArrayList;

public class PinchzoomTestActivity extends AppCompatActivity {

    private final static String TAG = "PinchzoomTestActivity";

    private TextView txtTitle;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinchzoom_test);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("핀치줌 테스트");

        gridView = (GridView) findViewById(R.id.grid_view);


        final ArrayList<MyItem> arrayList = new ArrayList<>();
        arrayList.add(new MyItem(R.drawable.wallpapaer1, "wallpaper 1" ));
        arrayList.add(new MyItem(R.drawable.wallpapaer2, "wallpaper 2" ));
        arrayList.add(new MyItem(R.drawable.wallpapaer3, "wallpaper 3" ));

        gridView.setAdapter(new MyGridAdapter(arrayList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ZoomImgActivity.class);
                // intent.putExtra("drawable", arrayList.get(i).getDrawable());
                intent.putExtra("myitems", arrayList);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });
    }

    private class MyGridAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private ArrayList<MyItem> arrayList;
        private ViewHolder viewHolder;

        private class ViewHolder {
            public ImageView imgView;
            public TextView txtView;
        }

        public MyGridAdapter(ArrayList<MyItem> arrayList) {
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public MyItem getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view = layoutInflater.inflate(R.layout.grid_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.txtView = (TextView) view.findViewById(R.id.txt);
                viewHolder.imgView = (ImageView) view.findViewById(R.id.img);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            new LoadThumbImgTask(getApplicationContext(), viewHolder.imgView)
                    .execute(arrayList.get(i).getDrawable());
            viewHolder.txtView.setText(arrayList.get(i).getTxt());

            return view;

        }
    }
}
