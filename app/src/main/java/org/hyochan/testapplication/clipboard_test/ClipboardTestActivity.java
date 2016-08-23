package org.hyochan.testapplication.clipboard_test;

import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.hyochan.testapplication.R;

public class ClipboardTestActivity extends AppCompatActivity {

    private final String TAG = "ClipboardTestActivity";

    TextView txtTitle;
    EditText editText;
    TextView txtConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard_test);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("Clipboard Test");
        editText = (EditText) findViewById(R.id.edit_text);
        txtConsole = (TextView) findViewById(R.id.txt_console);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if(clipboard.getPrimaryClip() != null){
            editText.setText(clipboard.getPrimaryClip().getItemAt(0).coerceToText(this));

            txtConsole.setText(
                    "clipboard.hasPrimaryClip() : " + clipboard.hasPrimaryClip() + "\n" +
                            "clipboard.getPrimaryClip().getItemAt(0).coerceToText() : " + clipboard.getPrimaryClip().getItemAt(0).coerceToText(this)+ "\n" +
                            "clipboard.getPrimaryDescription().toString() : " + clipboard.getPrimaryClipDescription().toString()
            );

            String[] msgSplit = clipboard.getPrimaryClip().getItemAt(0).coerceToText(this).toString().split("\n");
            // 문자열이 6줄 이하면 리턴
            if(msgSplit.length < 6) return;

            Log.d(TAG, "msgSplit[1] : " + msgSplit[1].substring(4,6));
            Log.d(TAG, "msgSplit[1] : " + msgSplit[1].substring(7,9));

            for(String str : msgSplit){
                Log.d(TAG, "str : " + str);
            }
        }
    }
}
