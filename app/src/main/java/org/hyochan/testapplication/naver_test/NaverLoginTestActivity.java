package org.hyochan.testapplication.naver_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.hyochan.testapplication.R;

public class NaverLoginTestActivity extends AppCompatActivity {

    private final static String TAG = "NaverLoginTestActivity";

    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_login_test);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("Naver Login Test");
    }
}
