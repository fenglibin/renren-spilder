package com.xmobileapp.filebrowser;

/**
 * 作者：人人IT网<br>
 * Site: www.renren.it
 */
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xmobileapp.filebrowser.util.FileUtil;

public class TextViewActivity extends Activity {

    TextView textView = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.text_view_activity);
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("filePath");
        String content = null;
        try {
            content = FileUtil.read(filePath);
        } catch (IOException e) {
            Log.e(Log.ERROR + "", "read content err.", e);
            content = "Read content from file error:" + filePath;
        }
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(content);
    }
}
