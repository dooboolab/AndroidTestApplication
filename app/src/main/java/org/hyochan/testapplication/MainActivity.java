package org.hyochan.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.hyochan.testapplication.clipboard_test.ClipboardTestActivity;
import org.hyochan.testapplication.google_test.login.GoogleLoginTestActivity;
import org.hyochan.testapplication.google_test.map.GoogleMapTestActivity;
import org.hyochan.testapplication.multi_imagepicker_test.MultiImagePickerTest;
import org.hyochan.testapplication.naver_test.NaverLoginTestActivity;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private TextView txtTitle;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("Lists of Tests");
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "클립보드 복붙 테스트",
                "멀티 이미지 픽커 테스트",
                "네이버 로그인 테스트",
                "구글 로그인 테스트",
                "구글 지도 테스트"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = ((TextView) view.findViewById(android.R.id.text1));
                textView.setMinHeight(0); // Min Height
                textView.setMinimumHeight(0); // Min Height
                textView.setHeight(150); // Height
                return view;
            }
        };

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getApplicationContext(), ClipboardTestActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), MultiImagePickerTest.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), NaverLoginTestActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), GoogleLoginTestActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), GoogleMapTestActivity.class));
                }
            }
        });
    }
}
