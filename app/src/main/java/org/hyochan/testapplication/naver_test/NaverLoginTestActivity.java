package org.hyochan.testapplication.naver_test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.hyochan.testapplication.R;

public class NaverLoginTestActivity extends AppCompatActivity {

    private final static String TAG = "NaverLoginTestActivity";

    private Context context;

    private TextView txtTitle;
    private OAuthLoginButton oAuthLoginButton;
    private OAuthLoginHandler oAuthLoginHandler;
    private OAuthLogin mOAuthLoginModule;

    private TextView txtOauthAt;
    private TextView txtOauthRt;
    private TextView txtOauthExpires;
    private TextView txtOauthTokenType;
    private TextView txtOauthState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naver_login_test);

        context = this;

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("Naver Login Test");

        txtOauthAt = (TextView) findViewById(R.id.txt_oauth_at);
        txtOauthRt = (TextView) findViewById(R.id.txt_oauth_rt);
        txtOauthExpires = (TextView) findViewById(R.id.txt_oauth_expires);
        txtOauthState = (TextView) findViewById(R.id.txt_oauth_state);
        txtOauthTokenType = (TextView) findViewById(R.id.txt_oauth_type);

        oAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        oAuthLoginButton.setOAuthLoginHandler(oAuthLoginHandler);
        oAuthLoginButton.setBgResourceId(R.drawable.btn_naver_login);
        oAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOAuthLoginModule.startOauthLoginActivity(NaverLoginTestActivity.this, mOAuthLoginHandler);
            }
        });

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                NaverLoginTestActivity.this
                ,"k6_Ancuyek3Yg5vX_LuT"
                ,"tDJWpJPosX"
                ,"테스트 어플리케이션"
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );

        /**
         * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
         객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
         */

    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(context);
                String refreshToken = mOAuthLoginModule.getRefreshToken(context);
                long expiresAt = mOAuthLoginModule.getExpiresAt(context);
                String tokenType = mOAuthLoginModule.getTokenType(context);
                txtOauthAt.setText(accessToken);
                txtOauthRt.setText(refreshToken);
                txtOauthExpires.setText(String.valueOf(expiresAt));
                txtOauthTokenType.setText(tokenType);
                txtOauthState.setText(mOAuthLoginModule.getState(context).toString());
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);
                Toast.makeText(context, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };
}
