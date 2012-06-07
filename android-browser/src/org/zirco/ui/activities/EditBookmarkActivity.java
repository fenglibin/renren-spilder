/*
 * Zirco Browser for Android Copyright (C) 2010 J. Devauchelle and contributors. This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 */

package org.zirco.ui.activities;

import org.zirco.R;
import org.zirco.providers.BookmarksProviderWrapper;
import org.zirco.utils.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Add / Edit bookmark activity.
 */
public class EditBookmarkActivity extends Activity {

    private EditText mTitleEditText;
    private EditText mUrlEditText;

    private Button   mOkButton;
    private Button   mCancelButton;

    private long     mRowId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        /* 标题栏左边显示一个ICON的标识 */
        w.requestFeature(Window.FEATURE_LEFT_ICON);
        /* 显示编辑书签的activity */
        setContentView(R.layout.edit_bookmark_activity);

        w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_input_add);
        /* 书签标题 */
        mTitleEditText = (EditText) findViewById(R.id.EditBookmarkActivity_TitleValue);
        /* 书签URL */
        mUrlEditText = (EditText) findViewById(R.id.EditBookmarkActivity_UrlValue);
        /* OK按钮 */
        mOkButton = (Button) findViewById(R.id.EditBookmarkActivity_BtnOk);
        /* 取消按钮 */
        mCancelButton = (Button) findViewById(R.id.EditBookmarkActivity_BtnCancel);
        /* 给OK按钮增加监控事情 */
        mOkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setAsBookmark();
                setResult(RESULT_OK);
                finish();
            }
        });
        /* 给取消息按钮增加监控事件 */
        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        /* 取得由调用的activity传入的变量 */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String title = extras.getString(Constants.EXTRA_ID_BOOKMARK_TITLE);
            if ((title != null) && (title.length() > 0)) {
                mTitleEditText.setText(title);
            }

            String url = extras.getString(Constants.EXTRA_ID_BOOKMARK_URL);
            if ((url != null) && (url.length() > 0)) {
                mUrlEditText.setText(url);
            } else {
                mUrlEditText.setHint("http://");
            }

            mRowId = extras.getLong(Constants.EXTRA_ID_BOOKMARK_ID);

        }

        if (mRowId == -1) {
            setTitle(R.string.EditBookmarkActivity_TitleAdd);
        }
    }

    /**
     * 增加一个书签 Set the current title and url values as a bookmark, e.g. adding a record if necessary or set only the
     * bookmark flag.
     */
    private void setAsBookmark() {
        BookmarksProviderWrapper.setAsBookmark(getContentResolver(), mRowId, mTitleEditText.getText().toString(),
                                               mUrlEditText.getText().toString(), true);
    }

}
