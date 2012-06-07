/*
 * Zirco Browser for Android Copyright (C) 2010 J. Devauchelle and contributors. This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 */

package org.zirco.ui.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.greendroid.QuickAction;
import org.greendroid.QuickActionGrid;
import org.greendroid.QuickActionWidget;
import org.greendroid.QuickActionWidget.OnQuickActionClickListener;
import org.zirco.R;
import org.zirco.controllers.Controller;
import org.zirco.events.EventConstants;
import org.zirco.events.EventController;
import org.zirco.events.IDownloadEventsListener;
import org.zirco.model.adapters.UrlSuggestionCursorAdapter;
import org.zirco.model.items.DownloadItem;
import org.zirco.providers.BookmarksProviderWrapper;
import org.zirco.providers.BookmarksProviderWrapper.BookmarksSource;
import org.zirco.ui.activities.preferences.PreferencesActivity;
import org.zirco.ui.components.CustomWebView;
import org.zirco.ui.components.CustomWebViewClient;
import org.zirco.ui.runnables.FaviconUpdaterRunnable;
import org.zirco.ui.runnables.HistoryUpdater;
import org.zirco.utils.AnimationManager;
import org.zirco.utils.ApplicationUtils;
import org.zirco.utils.Constants;
import org.zirco.utils.UrlUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * The application main activity.
 */
public class MainActivity extends Activity implements IToolbarsContainer, OnTouchListener, IDownloadEventsListener {

    public static MainActivity               INSTANCE                        = null;

    private static final int                 FLIP_PIXEL_THRESHOLD            = 200;
    private static final int                 FLIP_TIME_THRESHOLD             = 400;

    private static final int                 MENU_ADD_BOOKMARK               = Menu.FIRST;           /* 底部五个菜单之一：添加到书签 */
    private static final int                 MENU_SHOW_BOOKMARKS             = Menu.FIRST + 1;
    private static final int                 MENU_SHOW_DOWNLOADS             = Menu.FIRST + 2;
    private static final int                 MENU_PREFERENCES                = Menu.FIRST + 3;
    private static final int                 MENU_EXIT                       = Menu.FIRST + 4;
    private static final int                 MENU_SHOW_ADDRESS_BAR           = Menu.FIRST + 5;       /* 显示地址栏 */

    private static final int                 CONTEXT_MENU_OPEN               = Menu.FIRST + 10;
    private static final int                 CONTEXT_MENU_OPEN_IN_NEW_TAB    = Menu.FIRST + 11;
    private static final int                 CONTEXT_MENU_DOWNLOAD           = Menu.FIRST + 12;
    private static final int                 CONTEXT_MENU_COPY               = Menu.FIRST + 13;
    private static final int                 CONTEXT_MENU_SEND_MAIL          = Menu.FIRST + 14;
    private static final int                 CONTEXT_MENU_SHARE              = Menu.FIRST + 15;

    private static final int                 OPEN_BOOKMARKS_HISTORY_ACTIVITY = 0;
    private static final int                 OPEN_DOWNLOADS_ACTIVITY         = 1;
    private static final int                 OPEN_FILE_CHOOSER_ACTIVITY      = 2;
    /* 布局渲染器 */
    protected LayoutInflater                 mInflater                       = null;
    /* 包括地址栏的顶部工具条 */
    private LinearLayout                     mTopBar;
    /* 进度栏布局 */
    private LinearLayout                     mProgressBarLinear;
    /* 底部工具栏 */
    private LinearLayout                     mBottomBar;
    /* 文字查找工具工具条 */
    private LinearLayout                     mFindBar;
    /* 打开工具栏，这里的工具栏包括地址栏和底部工具栏 */
    private LinearLayout                     mOpenToolBar;
    /* 停止加载当前页面，位于页面的左下脚 */
    private LinearLayout                     mStopLoadingBar;

    private ImageButton                      mFindPreviousButton;
    private ImageButton                      mFindNextButton;
    private ImageButton                      mFindCloseButton;

    private EditText                         mFindText;

    private ImageView                        mPreviousTabView;
    private ImageView                        mNextTabView;

    private ImageButton                      mToolsButton;
    /** 可自动完成的url输入文本框 */
    private AutoCompleteTextView             mUrlEditText;
    private ImageButton                      mGoButton;
    /** 页面加载进度条 */
    private ProgressBar                      mProgressBar;
    /** 页面加载进度条是否可见 */
    private boolean                          mProgressBarVisible;

    private ImageView                        mBubbleRightView;
    private ImageView                        mBubbleLeftView;

    private CustomWebView                    mCurrentWebView;
    private List<CustomWebView>              mWebViews;

    private ImageButton                      mPreviousButton;
    private ImageButton                      mNextButton;

    private ImageButton                      mNewTabButton;
    private ImageButton                      mRemoveTabButton;

    private ImageButton                      mQuickButton;

    private ImageButton                      mOpenToolBarButton;
    private ImageButton                      mStopLoadingBarButton;
    /* 输入URL后，地址栏右边的滚动加载效果，是通过配置文件drawable/spinner.xml中的8张图片循环显示达到的效果 */
    private Drawable                         mCircularProgress;
    /* 地址栏是否可见 */
    private boolean                          mUrlBarVisible;
    /* 底部工具栏是否可见 */
    private boolean                          mButtomBarVisible;
    /* 页面两边的向前向后飜类似箭头的图标是否可见 */
    private boolean                          mBubbleVisible;
    private boolean                          mToolsActionGridVisible         = false;
    private boolean                          mFindDialogVisible              = false;
    private boolean                          mOpenToolBarVisible             = true;
    private boolean                          mStopLoadingBarVisible          = true;

    private TextWatcher                      mUrlTextWatcher;

    /* 简单的View展示器，一次只能够依附于Activity的一个View，并且支持循环展示 */
    private ViewFlipper                      mViewFlipper;

    private GestureDetector                  mGestureDetector;

    private SwitchTabsMethod                 mSwitchTabsMethod               = SwitchTabsMethod.BOTH;

    private QuickActionGrid                  mToolsActionGrid;

    private ValueCallback<Uri>               mUploadMessage;

    private OnSharedPreferenceChangeListener mPreferenceChangeListener;

    private enum SwitchTabsMethod {
        BUTTONS, FLING, BOTH
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // this.setTheme(R.style.Dark);
        super.onCreate(savedInstanceState);

        INSTANCE = this;

        Constants.initializeConstantsFromResources(this);

        Controller.getInstance().setPreferences(PreferenceManager.getDefaultSharedPreferences(this));

        if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_SHOW_FULL_SCREEN, false)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS, true)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        setProgressBarVisibility(true);
        /* 设置打开后显示的Activity */
        setContentView(R.layout.main);

        /* 页面加载时，地址栏旁边的滚动加载效果 */
        mCircularProgress = getResources().getDrawable(R.drawable.spinner);
        /* 增加当前Activity的下载监听事件 */
        EventController.getInstance().addDownloadListener(this);

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        buildComponents();

        mViewFlipper.removeAllViews();

        updateSwitchTabsMethod();
        updateBookmarksDatabaseSource();

        registerPreferenceChangeListener();

        Intent i = getIntent();
        if (i.getData() != null) {
            // App first launch from another app.
            addTab(false);
            navigateToUrl(i.getDataString());
        } else {
            // Normal start.
            int currentVersionCode = ApplicationUtils.getApplicationVersionCode(this);
            int savedVersionCode = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.PREFERENCES_LAST_VERSION_CODE,
                                                                                              -1);

            // If currentVersionCode and savedVersionCode are different, the
            // application has been updated.
            if (currentVersionCode != savedVersionCode) {
                // Save current version code.
                Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putInt(Constants.PREFERENCES_LAST_VERSION_CODE, currentVersionCode);
                editor.commit();

                // Display changelog dialog.
                Intent changelogIntent = new Intent(this, ChangelogActivity.class);
                startActivity(changelogIntent);
            }

            addTab(true);
        }

        initializeWebIconDatabase();

    }

    /**
     * Initialize the Web icons database.
     */
    private void initializeWebIconDatabase() {

        final WebIconDatabase db = WebIconDatabase.getInstance();
        db.open(getDir("icons", 0).getPath());
    }

    @Override
    protected void onDestroy() {
        WebIconDatabase.getInstance().close();

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.PREFERENCES_PRIVACY_CLEAR_CACHE_ON_EXIT,
                                                                           false)) {
            mCurrentWebView.clearCache(true);
        }

        EventController.getInstance().removeDownloadListener(this);

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(mPreferenceChangeListener);

        super.onDestroy();
    }

    /**
     * Handle url request from external apps.
     * 
     * @param intent The intent.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getData() != null) {
            addTab(false);
            navigateToUrl(intent.getDataString());
        }

        setIntent(intent);

        super.onNewIntent(intent);
    }

    /**
     * Restart the application.
     */
    public void restartApplication() {
        PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(), 0, new Intent(getIntent()),
                                                         getIntent().getFlags());
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
        System.exit(2);
    }

    /**
     * Create main UI.
     */
    private void buildComponents() {
        /* 浏览器左上角点击时出现的工具栏 */
        mToolsActionGrid = new QuickActionGrid(this);
        /* 回到首页 */
        mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_home, R.string.QuickAction_Home));
        /* 分享到微博等 */
        mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_share, R.string.QuickAction_Share));
        /* 在当前页面中进行内容的查找 */
        mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_find, R.string.QuickAction_Find));
        /* 选择文本 */
        mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_select, R.string.QuickAction_SelectText));
        /* 移动网关，这里指的是访问网站通过GOOGLE，让GOOGLE来格式化内容，加快用户的访问速度 */
        mToolsActionGrid.addQuickAction(new QuickAction(this, R.drawable.ic_btn_mobile_view,
                                                        R.string.QuickAction_MobileView));
        /* 指定它们相应的触发动作，这里是根据加入的位置顺序来确定的 */
        mToolsActionGrid.setOnQuickActionClickListener(new OnQuickActionClickListener() {

            @Override
            public void onQuickActionClicked(QuickActionWidget widget, int position) {
                switch (position) {
                    case 0:
                        navigateToHome();
                        break;
                    case 1:
                        ApplicationUtils.sharePage(MainActivity.this, mCurrentWebView.getTitle(),
                                                   mCurrentWebView.getUrl());
                        break;
                    case 2:
                        // Somewhat dirty hack: when the find dialog was
                        // shown from a QuickAction,
                        // the soft keyboard did not show... Hack is to wait
                        // a little before showing
                        // the file dialog through a thread.
                        startShowFindDialogRunnable();
                        break;
                    case 3:
                        swithToSelectAndCopyTextMode();
                        break;
                    case 4:
                        String currentUrl = mUrlEditText.getText().toString();

                        // Do not reload mobile view if already on it.
                        if (!currentUrl.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT)) {
                            String url = String.format(Constants.URL_GOOGLE_MOBILE_VIEW,
                                                       mUrlEditText.getText().toString());
                            navigateToUrl(url);
                        }
                        break;
                }
            }
        });
        /* 设置消失的监听器 */
        mToolsActionGrid.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mToolsActionGridVisible = false;
            }
        });
        /* 手势监听器 */
        mGestureDetector = new GestureDetector(this, new GestureListener());
        /* 页面刚加载的时候，显示url输入地址栏，这个可以优化为不显示 */
        mUrlBarVisible = false;

        mWebViews = new ArrayList<CustomWebView>();
        Controller.getInstance().setWebViewList(mWebViews);

        /* 右下脚一块类似阴影的图片，点击会显示顶部、底的工具栏 */
        mBubbleRightView = (ImageView) findViewById(R.id.BubbleRightView);
        mBubbleRightView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setToolbarsVisibility(true);
            }
        });
        mBubbleRightView.setVisibility(View.GONE);
        /* 这个左下脚的，目前还没有发现 */
        mBubbleLeftView = (ImageView) findViewById(R.id.BubbleLeftView);
        mBubbleLeftView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setToolbarsVisibility(true);
            }
        });
        mBubbleLeftView.setVisibility(View.GONE);

        mViewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);

        mTopBar = (LinearLayout) findViewById(R.id.BarLayout);
        mTopBar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Dummy event to steel it from the WebView, in case of clicking
                // between the buttons.
            }
        });
        /* 进度条的布局 */
        mProgressBarLinear = (LinearLayout) findViewById(R.id.WebViewProgressLayout);

        mBottomBar = (LinearLayout) findViewById(R.id.BottomBarLayout);
        mBottomBar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Dummy event to steel it from the WebView, in case of clicking
                // between the buttons.
            }
        });

        mFindBar = (LinearLayout) findViewById(R.id.findControls);
        mFindBar.setVisibility(View.GONE);

        /** 多TAB页的时候，会有上、下多个TAB页切换的（开始） */
        mPreviousTabView = (ImageView) findViewById(R.id.PreviousTabView);
        mPreviousTabView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPreviousTab();
            }
        });
        mPreviousTabView.setVisibility(View.GONE);

        mNextTabView = (ImageView) findViewById(R.id.NextTabView);
        mNextTabView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showNextTab();
            }
        });
        mNextTabView.setVisibility(View.GONE);
        /** 多TAB页的时候，会有上、下多个TAB页切换的（结束） */

        /* 加载进度栏 */
        mProgressBar = (ProgressBar) findViewById(R.id.WebViewProgress);
        mProgressBar.setMax(100);
        /* 左上脚的工具按钮 */
        mToolsButton = (ImageButton) findViewById(R.id.ToolsBtn);
        mToolsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mToolsActionGridVisible = true;
                mToolsActionGrid.show(v);
            }
        });

        /** 输入提示（开始） */
        String[] from = new String[] { UrlSuggestionCursorAdapter.URL_SUGGESTION_TITLE,
                UrlSuggestionCursorAdapter.URL_SUGGESTION_URL };
        int[] to = new int[] { R.id.AutocompleteTitle, R.id.AutocompleteUrl };

        UrlSuggestionCursorAdapter adapter = new UrlSuggestionCursorAdapter(this, R.layout.url_autocomplete_line, null,
                                                                            from, to);

        adapter.setCursorToStringConverter(new CursorToStringConverter() {

            @Override
            public CharSequence convertToString(Cursor cursor) {
                String aColumnString = cursor.getString(cursor.getColumnIndex(UrlSuggestionCursorAdapter.URL_SUGGESTION_URL));
                return aColumnString;
            }
        });
        /* 设置自动提示为和书签中比较的内容，这里可以优化为包括书答以及指定的额外的URL */
        adapter.setFilterQueryProvider(new FilterQueryProvider() {

            @Override
            public Cursor runQuery(CharSequence constraint) {
                if ((constraint != null) && (constraint.length() > 0)) {
                    return BookmarksProviderWrapper.getUrlSuggestions(getContentResolver(),
                                                                      constraint.toString(),
                                                                      PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(Constants.PREFERENCE_USE_WEAVE,
                                                                                                                                                  false));
                } else {
                    return BookmarksProviderWrapper.getUrlSuggestions(getContentResolver(),
                                                                      null,
                                                                      PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(Constants.PREFERENCE_USE_WEAVE,
                                                                                                                                                  false));
                }
            }
        });
        /** 输入提示（结整） */

        mUrlEditText = (AutoCompleteTextView) findViewById(R.id.UrlText);
        mUrlEditText.setThreshold(1);
        /* 设置URL自动完成的适配器 */
        mUrlEditText.setAdapter(adapter);
        /* 增加键盘事情，当按回车的时候，就指定转到指定URL的动作 */
        mUrlEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    navigateToUrl();
                    return true;
                }

                return false;
            }

        });
        /* 声明文本内容变化监听器 */
        mUrlTextWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                updateGoButton();
            }
        };
        /* 增加文本变化监听器 */
        mUrlEditText.addTextChangedListener(mUrlTextWatcher);
        /* 对输入框获取焦点的处理，此时全部选中文本框中的内容 */
        mUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Select all when focus gained.
                if (hasFocus) {
                    mUrlEditText.setSelection(0, mUrlEditText.getText().length());
                }
            }
        });

        mUrlEditText.setCompoundDrawablePadding(5);

        /* URL跳转按钮的事件 */
        mGoButton = (ImageButton) findViewById(R.id.GoBtn);
        /* 这个按钮有三种状态：正在加载的状态、加载完成的状态、待转到新URL的状态（地址栏中正在输入URL的时候） */
        mGoButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (mCurrentWebView.isLoading()) {/* 正在加载就停止加载 */
                    mCurrentWebView.stopLoading();
                } else if (!mCurrentWebView.isSameUrl(mUrlEditText.getText().toString())) {/* 与当前页面的URL不同，就转到指定的URL */
                    navigateToUrl();
                } else {/* 直接刷新当前页面 */
                    mCurrentWebView.reload();
                }
            }
        });

        /** <<<<<<<<<<底部工具栏（开始） */
        /* 返回上一页的按钮，在底部工具栏上 */
        mPreviousButton = (ImageButton) findViewById(R.id.PreviousBtn);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                navigatePrevious();
            }
        });
        /* 删除当前的TAB页 */
        mRemoveTabButton = (ImageButton) findViewById(R.id.RemoveTabBtn);
        mRemoveTabButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                removeCurrentTab();
            }
        });
        /* 显示书签 */
        mQuickButton = (ImageButton) findViewById(R.id.QuickBtn);
        mQuickButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                onQuickButton();
            }
        });
        /* 增另一个新的TAB，支持用户同时打开多个页面 */
        mNewTabButton = (ImageButton) findViewById(R.id.NewTabBtn);
        mNewTabButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                addTab(true);
            }
        });
        /* 进入下一页的按钮，在底部工具栏上 */
        mNextButton = (ImageButton) findViewById(R.id.NextBtn);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                navigateNext();
            }
        });
        /** 底部工具栏（结束） >>>>>>>>>> */

        /** <<<<<<<<<<查找工具栏（开始） */
        /* 查找工具栏的查找前一个 */
        mFindPreviousButton = (ImageButton) findViewById(R.id.find_previous);
        mFindPreviousButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentWebView.findNext(false);
                hideKeyboardFromFindDialog();
            }
        });
        /* 查找工具栏的查找下一个 */
        mFindNextButton = (ImageButton) findViewById(R.id.find_next);
        mFindNextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentWebView.findNext(true);
                hideKeyboardFromFindDialog();
            }
        });
        /* 查找工具栏的文本输入框 */
        mFindText = (EditText) findViewById(R.id.find_value);
        mFindText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doFind();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        /* 查找工具栏的关闭当前查找按钮 */
        mFindCloseButton = (ImageButton) findViewById(R.id.find_close);
        mFindCloseButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                closeFindDialog();
            }
        });
        /** 查找工具栏（结束）>>>>>>>>>> */
        /* 打开工具栏 */
        mOpenToolBar = (LinearLayout) findViewById(R.id.OpenToolBarLayout);
        mOpenToolBarButton = (ImageButton) findViewById(R.id.OpenToolBarBtn);
        mOpenToolBarButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setOpenToolBarVisibility(false);
                setUrlBarAndButtomBarVisibility(true);
            }

        });
        mStopLoadingBar = (LinearLayout) findViewById(R.id.StopLoadingBarLayout);
        mStopLoadingBarButton = (ImageButton) findViewById(R.id.StopLoadingBarBtn);
        mStopLoadingBarButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentWebView.isLoading()) {/* 正在加载就停止加载 */
                    mCurrentWebView.stopLoading();
                }
                setStopLoadingBarVisibility(false);
            }

        });

    }

    private void registerPreferenceChangeListener() {
        mPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(Constants.PREFERENCE_BOOKMARKS_DATABASE)) {
                    updateBookmarksDatabaseSource();
                }
            }
        };

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
    }

    /**
     * 个人设置保存后更新当前UI Apply preferences to the current UI objects.
     */
    public void applyPreferences() {
        // To update to Bubble position.
        setToolbarsVisibility(false);

        updateSwitchTabsMethod();

        for (CustomWebView view : mWebViews) {
            view.initializeOptions();
        }
    }

    private void updateSwitchTabsMethod() {
        String method = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREFERENCES_GENERAL_SWITCH_TABS_METHOD,
                                                                                      "buttons");

        if (method.equals("buttons")) {
            mSwitchTabsMethod = SwitchTabsMethod.BUTTONS;
        } else if (method.equals("fling")) {
            mSwitchTabsMethod = SwitchTabsMethod.FLING;
        } else if (method.equals("both")) {
            mSwitchTabsMethod = SwitchTabsMethod.BOTH;
        } else {
            mSwitchTabsMethod = SwitchTabsMethod.BUTTONS;
        }
    }

    private void updateBookmarksDatabaseSource() {
        String source = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREFERENCE_BOOKMARKS_DATABASE,
                                                                                      "STOCK");

        if (source.equals("STOCK")) {
            BookmarksProviderWrapper.setBookmarksSource(BookmarksSource.STOCK);
        } else if (source.equals("INTERNAL")) {
            BookmarksProviderWrapper.setBookmarksSource(BookmarksSource.INTERNAL);
        }
    }

    /**
     * Initialize a newly created WebView.
     */
    private void initializeCurrentWebView() {

        mCurrentWebView.setWebViewClient(new CustomWebViewClient(this));
        mCurrentWebView.setOnTouchListener(this);

        mCurrentWebView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                HitTestResult result = ((WebView) v).getHitTestResult();

                int resultType = result.getType();
                if ((resultType == HitTestResult.ANCHOR_TYPE) || (resultType == HitTestResult.IMAGE_ANCHOR_TYPE)
                    || (resultType == HitTestResult.SRC_ANCHOR_TYPE)
                    || (resultType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {

                    Intent i = new Intent();
                    i.putExtra(Constants.EXTRA_ID_URL, result.getExtra());

                    MenuItem item = menu.add(0, CONTEXT_MENU_OPEN, 0, R.string.Main_MenuOpen);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_OPEN_IN_NEW_TAB, 0, R.string.Main_MenuOpenNewTab);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_COPY, 0, R.string.Main_MenuCopyLinkUrl);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_DOWNLOAD, 0, R.string.Main_MenuDownload);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.Main_MenuShareLinkUrl);
                    item.setIntent(i);

                    menu.setHeaderTitle(result.getExtra());
                } else if (resultType == HitTestResult.IMAGE_TYPE) {
                    Intent i = new Intent();
                    i.putExtra(Constants.EXTRA_ID_URL, result.getExtra());

                    MenuItem item = menu.add(0, CONTEXT_MENU_OPEN, 0, R.string.Main_MenuViewImage);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_COPY, 0, R.string.Main_MenuCopyImageUrl);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_DOWNLOAD, 0, R.string.Main_MenuDownloadImage);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.Main_MenuShareImageUrl);
                    item.setIntent(i);

                    menu.setHeaderTitle(result.getExtra());

                } else if (resultType == HitTestResult.EMAIL_TYPE) {

                    Intent sendMail = new Intent(Intent.ACTION_VIEW, Uri.parse(WebView.SCHEME_MAILTO
                                                                               + result.getExtra()));

                    MenuItem item = menu.add(0, CONTEXT_MENU_SEND_MAIL, 0, R.string.Main_MenuSendEmail);
                    item.setIntent(sendMail);

                    Intent i = new Intent();
                    i.putExtra(Constants.EXTRA_ID_URL, result.getExtra());

                    item = menu.add(0, CONTEXT_MENU_COPY, 0, R.string.Main_MenuCopyEmailUrl);
                    item.setIntent(i);

                    item = menu.add(0, CONTEXT_MENU_SHARE, 0, R.string.Main_MenuShareEmailUrl);
                    item.setIntent(i);

                    menu.setHeaderTitle(result.getExtra());
                }
            }

        });

        mCurrentWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                doDownloadStart(url, userAgent, contentDisposition, mimetype, contentLength);
            }

        });

        final Activity activity = this;
        mCurrentWebView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            // This is an undocumented method, it _is_ used, whatever Eclipse
            // may think :)
            // Used to show a file chooser dialog.
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i,
                                                                              MainActivity.this.getString(R.string.Main_FileChooserPrompt)),
                                                         OPEN_FILE_CHOOSER_ACTIVITY);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ((CustomWebView) view).setProgress(newProgress);
                mProgressBar.setProgress(mCurrentWebView.getProgress());
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                new Thread(new FaviconUpdaterRunnable(MainActivity.this, view.getUrl(), view.getOriginalUrl(), icon)).start();
                updateFavIcon();

                super.onReceivedIcon(view, icon);
            }

            @Override
            public boolean onCreateWindow(WebView view, final boolean dialog, final boolean userGesture,
                                          final Message resultMsg) {

                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;

                addTab(false, mViewFlipper.getDisplayedChild());

                transport.setWebView(mCurrentWebView);
                resultMsg.sendToTarget();

                return false;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(String.format(getResources().getString(R.string.ApplicationNameUrl), title));

                startHistoryUpdaterRunnable(title, mCurrentWebView.getUrl(), mCurrentWebView.getOriginalUrl());

                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(activity).setTitle(R.string.Commons_JavaScriptDialog).setMessage(message).setPositiveButton(android.R.string.ok,
                                                                                                                                    new AlertDialog.OnClickListener() {

                                                                                                                                        public void onClick(DialogInterface dialog,
                                                                                                                                                            int which) {
                                                                                                                                            result.confirm();
                                                                                                                                        }
                                                                                                                                    }).setCancelable(false).create().show();

                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.Commons_JavaScriptDialog).setMessage(message).setPositiveButton(android.R.string.ok,
                                                                                                                                             new DialogInterface.OnClickListener() {

                                                                                                                                                 public void onClick(DialogInterface dialog,
                                                                                                                                                                     int which) {
                                                                                                                                                     result.confirm();
                                                                                                                                                 }
                                                                                                                                             }).setNegativeButton(android.R.string.cancel,
                                                                                                                                                                  new DialogInterface.OnClickListener() {

                                                                                                                                                                      public void onClick(DialogInterface dialog,
                                                                                                                                                                                          int which) {
                                                                                                                                                                          result.cancel();
                                                                                                                                                                      }
                                                                                                                                                                  }).create().show();

                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      final JsPromptResult result) {

                final LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View v = factory.inflate(R.layout.javascript_prompt_dialog, null);
                ((TextView) v.findViewById(R.id.JavaScriptPromptMessage)).setText(message);
                ((EditText) v.findViewById(R.id.JavaScriptPromptInput)).setText(defaultValue);

                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.Commons_JavaScriptDialog).setView(v).setPositiveButton(android.R.string.ok,
                                                                                                                                    new DialogInterface.OnClickListener() {

                                                                                                                                        public void onClick(DialogInterface dialog,
                                                                                                                                                            int whichButton) {
                                                                                                                                            String value = ((EditText) v.findViewById(R.id.JavaScriptPromptInput)).getText().toString();
                                                                                                                                            result.confirm(value);
                                                                                                                                        }
                                                                                                                                    }).setNegativeButton(android.R.string.cancel,
                                                                                                                                                         new DialogInterface.OnClickListener() {

                                                                                                                                                             public void onClick(DialogInterface dialog,
                                                                                                                                                                                 int whichButton) {
                                                                                                                                                                 result.cancel();
                                                                                                                                                             }
                                                                                                                                                         }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                                                                                                                                                                                    public void onCancel(DialogInterface dialog) {
                                                                                                                                                                                        result.cancel();
                                                                                                                                                                                    }
                                                                                                                                                                                }).show();

                return true;

            }

        });
    }

    /**
     * Select Text in the webview and automatically sends the selected text to the clipboard.
     */
    public void swithToSelectAndCopyTextMode() {
        try {
            KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
            shiftPressEvent.dispatch(mCurrentWebView);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Initiate a download. Check the SD card and start the download runnable.
     * 
     * @param url The url to download.
     * @param userAgent The user agent.
     * @param contentDisposition The content disposition.
     * @param mimetype The mime type.
     * @param contentLength The content length.
     */
    private void doDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                 long contentLength) {

        if (ApplicationUtils.checkCardState(this, true)) {
            DownloadItem item = new DownloadItem(this, url);
            Controller.getInstance().addToDownload(item);
            item.startDownload();

            Toast.makeText(this, getString(R.string.Main_DownloadStartedMsg), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Add a new tab.
     * 
     * @param navigateToHome If True, will load the user home page.
     */
    private void addTab(boolean navigateToHome) {
        addTab(navigateToHome, -1);
    }

    /**
     * Add a new tab.
     * 
     * @param navigateToHome If True, will load the user home page.
     * @param parentIndex The index of the new tab.
     */
    private void addTab(boolean navigateToHome, int parentIndex) {
        if (mFindDialogVisible) {
            closeFindDialog();
        }

        RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.webview, mViewFlipper, false);

        mCurrentWebView = (CustomWebView) view.findViewById(R.id.webview);

        initializeCurrentWebView();

        synchronized (mViewFlipper) {
            if (parentIndex != -1) {
                mWebViews.add(parentIndex + 1, mCurrentWebView);
                mViewFlipper.addView(view, parentIndex + 1);
            } else {
                mWebViews.add(mCurrentWebView);
                mViewFlipper.addView(view);
            }
            mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(view));
        }

        updateUI();
        updatePreviousNextTabViewsVisibility();

        mUrlEditText.clearFocus();

        if (navigateToHome) {
            navigateToHome();
        }
    }

    /**
     * Remove the current tab.
     */
    private void removeCurrentTab() {

        if (mFindDialogVisible) {
            closeFindDialog();
        }

        int removeIndex = mViewFlipper.getDisplayedChild();

        mCurrentWebView.doOnPause();

        synchronized (mViewFlipper) {
            mViewFlipper.removeViewAt(removeIndex);
            mViewFlipper.setDisplayedChild(removeIndex - 1);
            mWebViews.remove(removeIndex);
        }

        mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());

        updateUI();
        updatePreviousNextTabViewsVisibility();

        mUrlEditText.clearFocus();
    }

    private void doFind() {
        CharSequence find = mFindText.getText();
        if (find.length() == 0) {
            mFindPreviousButton.setEnabled(false);
            mFindNextButton.setEnabled(false);
            mCurrentWebView.clearMatches();
        } else {
            int found = mCurrentWebView.findAll(find.toString());
            if (found < 2) {
                mFindPreviousButton.setEnabled(false);
                mFindNextButton.setEnabled(false);
            } else {
                mFindPreviousButton.setEnabled(true);
                mFindNextButton.setEnabled(true);
            }
        }
    }

    private void showFindDialog() {
        setFindBarVisibility(true);
        mCurrentWebView.doSetFindIsUp(true);
        CharSequence text = mFindText.getText();
        if (text.length() > 0) {
            mFindText.setSelection(0, text.length());
            doFind();
        } else {
            mFindPreviousButton.setEnabled(false);
            mFindNextButton.setEnabled(false);
        }

        mFindText.requestFocus();
        showKeyboardForFindDialog();
    }

    /**
     * 关闭查找对话框
     */
    private void closeFindDialog() {
        hideKeyboardFromFindDialog();
        mCurrentWebView.doNotifyFindDialogDismissed();
        setFindBarVisibility(false);
    }

    private void setFindBarVisibility(boolean visible) {
        if (visible) {
            mFindBar.startAnimation(AnimationManager.getInstance().getTopBarShowAnimation());
            mFindBar.setVisibility(View.VISIBLE);
            mFindDialogVisible = true;
        } else {
            mFindBar.startAnimation(AnimationManager.getInstance().getTopBarHideAnimation());
            mFindBar.setVisibility(View.GONE);
            mFindDialogVisible = false;
        }
    }

    /**
     * 设置工具栏的可见性，包括顶部地址栏、底部工具栏、向前向后翻类似箭头的工具栏
     * 
     * @param visible If True, the tool bars will be shown.
     */
    private void setToolbarsVisibility(boolean visible) {
        setUrlBarVisibility(visible);
        setButtomBarVisibility(visible);
        setBubbleVisibility(visible);
    }

    /**
     * 设置地址栏所在工具栏是否可见
     * 
     * @param visible
     */
    private void setUrlBarVisibility(boolean visible) {
        if (visible) {
            if (!mUrlBarVisible) {
                mTopBar.startAnimation(AnimationManager.getInstance().getTopBarShowAnimation());
                mTopBar.setVisibility(View.VISIBLE);
            }
            mUrlBarVisible = true;

        } else {
            if (mUrlBarVisible) {
                mTopBar.startAnimation(AnimationManager.getInstance().getTopBarHideAnimation());
                mTopBar.setVisibility(View.GONE);
            }
            mUrlBarVisible = false;
        }
    }

    /**
     * 设置底部工具栏是否可见
     * 
     * @param visible
     */
    private void setButtomBarVisibility(boolean visible) {
        if (visible) {
            if (!mButtomBarVisible) {
                mBottomBar.startAnimation(AnimationManager.getInstance().getBottomBarShowAnimation());
                mBottomBar.setVisibility(View.VISIBLE);
            }
            mButtomBarVisible = true;

        } else {
            if (mButtomBarVisible) {
                mBottomBar.startAnimation(AnimationManager.getInstance().getBottomBarHideAnimation());
                mBottomBar.setVisibility(View.GONE);
            }
            mButtomBarVisible = false;
        }
    }

    /**
     * 设置地址栏以及底部工具栏的可见性
     * 
     * @param visible
     */
    private void setUrlBarAndButtomBarVisibility(boolean visible) {
        setUrlBarVisibility(visible);
        setButtomBarVisibility(visible);
    }

    /**
     * 页面两边的向前向后飜类似箭头的图标是否可见
     * 
     * @param visible
     */
    private void setBubbleVisibility(boolean visible) {

        boolean switchTabByButtons = isSwitchTabsByButtonsEnabled();
        boolean showPreviousTabView = mViewFlipper.getDisplayedChild() > 0;
        boolean showNextTabView = mViewFlipper.getDisplayedChild() < mViewFlipper.getChildCount() - 1;

        if (visible) {

            if (!mBubbleVisible) {

                if (switchTabByButtons) {
                    if (showPreviousTabView) {
                        mPreviousTabView.startAnimation(AnimationManager.getInstance().getPreviousTabViewShowAnimation());
                    }

                    if (showNextTabView) {
                        mNextTabView.startAnimation(AnimationManager.getInstance().getNextTabViewShowAnimation());
                    }
                }

                if (switchTabByButtons) {
                    if (showPreviousTabView) {
                        mPreviousTabView.setVisibility(View.VISIBLE);
                    }

                    if (showNextTabView) {
                        mNextTabView.setVisibility(View.VISIBLE);
                    }
                }

                mBubbleRightView.setVisibility(View.GONE);
                mBubbleLeftView.setVisibility(View.GONE);
            }

            mBubbleVisible = true;

        } else {

            if (mBubbleVisible) {

                if (switchTabByButtons) {
                    if (showPreviousTabView) {
                        mPreviousTabView.startAnimation(AnimationManager.getInstance().getPreviousTabViewHideAnimation());
                    }

                    if (showNextTabView) {
                        mNextTabView.startAnimation(AnimationManager.getInstance().getNextTabViewHideAnimation());
                    }
                }

                if (switchTabByButtons) {
                    if (showPreviousTabView) {
                        mPreviousTabView.setVisibility(View.GONE);
                    }

                    if (showNextTabView) {
                        mNextTabView.setVisibility(View.GONE);
                    }
                }

                String bubblePosition = Controller.getInstance().getPreferences().getString(Constants.PREFERENCES_GENERAL_BUBBLE_POSITION,
                                                                                            "right");

                if (bubblePosition.equals("right")) {
                    mBubbleRightView.setVisibility(View.VISIBLE);
                    mBubbleLeftView.setVisibility(View.GONE);
                } else if (bubblePosition.equals("left")) {
                    mBubbleRightView.setVisibility(View.GONE);
                    mBubbleLeftView.setVisibility(View.VISIBLE);
                } else if (bubblePosition.equals("both")) {
                    mBubbleRightView.setVisibility(View.VISIBLE);
                    mBubbleLeftView.setVisibility(View.VISIBLE);
                } else {
                    mBubbleRightView.setVisibility(View.VISIBLE);
                    mBubbleLeftView.setVisibility(View.GONE);
                }
            }

            mBubbleVisible = false;
        }
    }

    /**
     * 设置用于打于地址栏和底部工具栏的工具栏，是否可见
     * 
     * @param visible
     */
    private void setOpenToolBarVisibility(boolean visible) {
        if (visible) {
            if (!mOpenToolBarVisible) {
                mOpenToolBar.startAnimation(AnimationManager.getInstance().getBottomBarShowAnimation());
                mOpenToolBar.setVisibility(View.VISIBLE);
            }
            mOpenToolBarVisible = Boolean.TRUE;
        } else {
            if (mOpenToolBarVisible) {
                mOpenToolBar.startAnimation(AnimationManager.getInstance().getBottomBarHideAnimation());
                mOpenToolBar.setVisibility(View.GONE);
            }
            mOpenToolBarVisible = Boolean.FALSE;
        }
    }

    /**
     * 设置左下脚停止加载的按钮是否可见
     * 
     * @param visible
     */
    private void setStopLoadingBarVisibility(boolean visible) {
        if (visible) {
            if (!mStopLoadingBarVisible) {
                mStopLoadingBar.startAnimation(AnimationManager.getInstance().getBottomBarShowAnimation());
                mStopLoadingBar.setVisibility(View.VISIBLE);
            }
            mStopLoadingBarVisible = Boolean.TRUE;
        } else {
            if (mStopLoadingBarVisible) {
                mStopLoadingBar.startAnimation(AnimationManager.getInstance().getBottomBarHideAnimation());
                mStopLoadingBar.setVisibility(View.GONE);
            }
            mStopLoadingBarVisible = Boolean.FALSE;
        }
    }

    /**
     * 为查找对话框显示输入键盘
     */
    private void showKeyboardForFindDialog() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mFindText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 关闭查找对话框的输入键盘
     */
    private void hideKeyboardFromFindDialog() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mFindText.getWindowToken(), 0);
    }

    /**
     * 隐藏输入键盘，如果地址栏是显示的，则这里把地址栏隐藏起来
     */
    private void hideKeyboardFromUrlEditText() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUrlEditText.getWindowToken(), 0);
    }

    /**
     * Thread to delay the show of the find dialog. This seems to be necessary when shown from a QuickAction. If not,
     * the keyboard does not show. 50ms seems to be enough on a Nexus One and on the (rather) slow emulator. Dirty hack
     * :(
     */
    private void startShowFindDialogRunnable() {
        new Thread(new Runnable() {

            private Handler mHandler = new Handler() {

                                         public void handleMessage(Message msg) {
                                             showFindDialog();
                                         }
                                     };

            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                    mHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    mHandler.sendEmptyMessage(0);
                }

            }
        }).start();
    }

    /**
     * Hide the tool bars.
     */
    public void hideToolbars() {
        if (mUrlBarVisible) {
            if ((!mUrlEditText.hasFocus()) && (!mToolsActionGridVisible)) {

                if (!mCurrentWebView.isLoading()) {
                    setToolbarsVisibility(false);
                }
            }
        }
    }

    /**
     * Start a runnable to update history.
     * 
     * @param title The page title.
     * @param url The page url.
     */
    private void startHistoryUpdaterRunnable(String title, String url, String originalUrl) {
        if ((url != null) && (url.length() > 0)) {
            new Thread(new HistoryUpdater(this, title, url, originalUrl)).start();
        }
    }

    /**
     * Navigate to the given url.
     * 
     * @param url The url.
     */
    private void navigateToUrl(String url) {
        // Needed to hide toolbars properly.
        mUrlEditText.clearFocus();

        if ((url != null) && (url.length() > 0)) {

            if (UrlUtils.isUrl(url)) {
                url = UrlUtils.checkUrl(url);
            } else {
                url = UrlUtils.getSearchUrl(this, url);
            }

            hideKeyboardFromUrlEditText();
            if (mUrlBarVisible) {
                setUrlBarVisibility(false);
            }

            if (url.equals(Constants.URL_ABOUT_START) || url.equals(R.string.Main_AddressBarDefaultWords)) {

                mCurrentWebView.loadDataWithBaseURL(Constants.URL_BASE_URL, ApplicationUtils.getStartPage(this),
                                                    "text/html", "UTF-8", Constants.URL_ABOUT_START);

            } else {

                // If the url is not from GWT mobile view, and is in the mobile
                // view url list, then load it with GWT.
                if ((!url.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT))
                    && (UrlUtils.checkInMobileViewUrlList(this, url))) {

                    url = String.format(Constants.URL_GOOGLE_MOBILE_VIEW, url);
                }

                mCurrentWebView.loadUrl(url);
            }
        }
    }

    /**
     * Navigate to the url given in the url edit text.
     */
    private void navigateToUrl() {
        navigateToUrl(mUrlEditText.getText().toString());
    }

    /**
     * Navigate to the user home page.
     */
    private void navigateToHome() {
        navigateToUrl(Controller.getInstance().getPreferences().getString(Constants.PREFERENCES_GENERAL_HOME_PAGE,
                                                                          Constants.URL_ABOUT_START));
    }

    /**
     * Navigate to the previous page in history.
     */
    private void navigatePrevious() {
        // Needed to hide toolbars properly.
        mUrlEditText.clearFocus();

        hideKeyboardFromUrlEditText();
        if (mUrlBarVisible) {
            setUrlBarVisibility(false);
        }
        mCurrentWebView.goBack();
    }

    /**
     * Navigate to the next page in history.
     */
    private void navigateNext() {
        // Needed to hide toolbars properly.
        mUrlEditText.clearFocus();

        hideKeyboardFromUrlEditText();
        if (mUrlBarVisible) {
            setUrlBarVisibility(false);
        }
        mCurrentWebView.goForward();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.moveTaskToBack(true);
                return true;
            default:
                return super.onKeyLongPress(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mFindDialogVisible) {
                    closeFindDialog();
                } else {
                    if (mCurrentWebView.canGoBack()) {
                        mCurrentWebView.goBack();
                    } else {
                        this.moveTaskToBack(true);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_SEARCH:
                if (!mFindDialogVisible) {
                    showFindDialog();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        String volumeKeysBehaviour = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREFERENCES_UI_VOLUME_KEYS_BEHAVIOUR,
                                                                                                   "DEFAULT");

        if (!volumeKeysBehaviour.equals("DEFAULT")) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:

                    if (volumeKeysBehaviour.equals("SWITCH_TABS")) {
                        showPreviousTab();
                    } else if (volumeKeysBehaviour.equals("HISTORY")) {
                        mCurrentWebView.goForward();
                    } else {
                        mCurrentWebView.zoomIn();
                    }

                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:

                    if (volumeKeysBehaviour.equals("SWITCH_TABS")) {
                        showNextTab();
                    } else if (volumeKeysBehaviour.equals("HISTORY")) {
                        mCurrentWebView.goBack();
                    } else {
                        mCurrentWebView.zoomOut();
                    }

                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Set the application title to default.
     */
    private void clearTitle() {
        this.setTitle(getResources().getString(R.string.ApplicationName));
    }

    /**
     * Update the application title.
     */
    private void updateTitle() {
        String value = mCurrentWebView.getTitle();

        if ((value != null) && (value.length() > 0)) {
            this.setTitle(String.format(getResources().getString(R.string.ApplicationNameUrl), value));
        } else {
            clearTitle();
        }
    }

    /**
     * Get a Drawable of the current favicon, with its size normalized relative to current screen density.
     * 
     * @return The normalized favicon.
     */
    private BitmapDrawable getNormalizedFavicon() {

        BitmapDrawable favIcon = new BitmapDrawable(getResources(), mCurrentWebView.getFavicon());

        if (mCurrentWebView.getFavicon() != null) {
            int imageButtonSize = ApplicationUtils.getImageButtonSize(this);
            int favIconSize = ApplicationUtils.getFaviconSize(this);

            Bitmap bm = Bitmap.createBitmap(imageButtonSize, imageButtonSize, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bm);

            favIcon.setBounds((imageButtonSize / 2) - (favIconSize / 2), (imageButtonSize / 2) - (favIconSize / 2),
                              (imageButtonSize / 2) + (favIconSize / 2), (imageButtonSize / 2) + (favIconSize / 2));
            favIcon.draw(canvas);

            favIcon = new BitmapDrawable(getResources(), bm);
        }

        return favIcon;
    }

    /**
     * Update the "Go" button image.
     */
    private void updateGoButton() {
        if (mCurrentWebView.isLoading()) {
            mGoButton.setImageResource(R.drawable.ic_btn_stop);
            mUrlEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCircularProgress, null);
            ((AnimationDrawable) mCircularProgress).start();
        } else {
            if (!mCurrentWebView.isSameUrl(mUrlEditText.getText().toString())) {
                mGoButton.setImageResource(R.drawable.ic_btn_go);
            } else {
                mGoButton.setImageResource(R.drawable.ic_btn_reload);
            }

            mUrlEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            ((AnimationDrawable) mCircularProgress).stop();
        }
    }

    /**
     * Update the fav icon display.
     */
    private void updateFavIcon() {
        BitmapDrawable favicon = getNormalizedFavicon();

        if (mCurrentWebView.getFavicon() != null) {
            mToolsButton.setImageDrawable(favicon);
        } else {
            mToolsButton.setImageResource(R.drawable.fav_icn_default_toolbar);
        }
    }

    /**
     * Update the UI: Url edit text, previous/next button state,...
     */
    private void updateUI() {
        mUrlEditText.removeTextChangedListener(mUrlTextWatcher);
        mUrlEditText.setText(mCurrentWebView.getUrl());
        mUrlEditText.addTextChangedListener(mUrlTextWatcher);

        mPreviousButton.setEnabled(mCurrentWebView.canGoBack());
        mNextButton.setEnabled(mCurrentWebView.canGoForward());

        mRemoveTabButton.setEnabled(mViewFlipper.getChildCount() > 1);

        mProgressBar.setProgress(mCurrentWebView.getProgress());

        updateGoButton();

        updateTitle();

        updateFavIcon();
    }

    private boolean isSwitchTabsByFlingEnabled() {
        return (mSwitchTabsMethod == SwitchTabsMethod.FLING) || (mSwitchTabsMethod == SwitchTabsMethod.BOTH);
    }

    private boolean isSwitchTabsByButtonsEnabled() {
        return (mSwitchTabsMethod == SwitchTabsMethod.BUTTONS) || (mSwitchTabsMethod == SwitchTabsMethod.BOTH);
    }

    /**
     * Open the "Add bookmark" dialog.
     */
    private void openAddBookmarkDialog() {
        Intent i = new Intent(this, EditBookmarkActivity.class);

        i.putExtra(Constants.EXTRA_ID_BOOKMARK_ID, (long) -1);
        i.putExtra(Constants.EXTRA_ID_BOOKMARK_TITLE, mCurrentWebView.getTitle());
        i.putExtra(Constants.EXTRA_ID_BOOKMARK_URL, mCurrentWebView.getUrl());

        startActivity(i);
    }

    /**
     * Open the bookmark list.
     */
    private void openBookmarksHistoryActivity() {
        Intent i = new Intent(this, BookmarksHistoryActivity.class);
        startActivityForResult(i, OPEN_BOOKMARKS_HISTORY_ACTIVITY);
    }

    /**
     * Open the download list.
     */
    private void openDownloadsList() {
        Intent i = new Intent(this, DownloadsListActivity.class);
        startActivityForResult(i, OPEN_DOWNLOADS_ACTIVITY);
    }

    /**
     * Perform the user-defined action when clicking on the quick button.
     */
    private void onQuickButton() {
        openBookmarksHistoryActivity();
    }

    /**
     * Open preferences.
     */
    private void openPreferences() {
        Intent preferencesActivity = new Intent(this, PreferencesActivity.class);
        startActivity(preferencesActivity);
    }

    /**
     * 显示地址栏
     */
    private void showAddressBar() {
        setToolbarsVisibility(Boolean.TRUE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item;

        item = menu.add(0, MENU_SHOW_ADDRESS_BAR, 0, R.string.Main_ShowAddressBar);
        item.setIcon(R.drawable.ic_menu_add_bookmark);

        item = menu.add(0, MENU_ADD_BOOKMARK, 0, R.string.Main_MenuAddBookmark);
        item.setIcon(R.drawable.ic_menu_add_bookmark);

        item = menu.add(0, MENU_SHOW_BOOKMARKS, 0, R.string.Main_MenuShowBookmarks);
        item.setIcon(R.drawable.ic_menu_bookmarks);

        item = menu.add(0, MENU_SHOW_DOWNLOADS, 0, R.string.Main_MenuShowDownloads);
        item.setIcon(R.drawable.ic_menu_downloads);

        item = menu.add(0, MENU_PREFERENCES, 0, R.string.Main_MenuPreferences);
        item.setIcon(R.drawable.ic_menu_preferences);

        item = menu.add(0, MENU_EXIT, 0, R.string.Main_MenuExit);
        item.setIcon(R.drawable.ic_menu_exit);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ADD_BOOKMARK:
                openAddBookmarkDialog();
                return true;
            case MENU_SHOW_BOOKMARKS:
                openBookmarksHistoryActivity();
                return true;
            case MENU_SHOW_DOWNLOADS:
                openDownloadsList();
                return true;
            case MENU_PREFERENCES:
                openPreferences();
                return true;
            case MENU_EXIT:
                this.finish();
                return true;
            case MENU_SHOW_ADDRESS_BAR:
                showAddressBar();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == OPEN_BOOKMARKS_HISTORY_ACTIVITY) {
            if (intent != null) {
                Bundle b = intent.getExtras();
                if (b != null) {
                    if (b.getBoolean(Constants.EXTRA_ID_NEW_TAB)) {
                        addTab(false);
                    }
                    navigateToUrl(b.getString(Constants.EXTRA_ID_URL));
                }
            }
        } else if (requestCode == OPEN_FILE_CHOOSER_ACTIVITY) {
            if (mUploadMessage == null) {
                return;
            }

            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    protected void onPause() {
        mCurrentWebView.doOnPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mCurrentWebView.doOnResume();
        super.onResume();
    }

    /**
     * Show a toast alert on tab switch.
     */
    private void showToastOnTabSwitch() {
        if (Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_SHOW_TOAST_ON_TAB_SWITCH, true)) {
            String text;
            if (mCurrentWebView.getTitle() != null) {
                text = String.format(getString(R.string.Main_ToastTabSwitchFullMessage),
                                     mViewFlipper.getDisplayedChild() + 1, mCurrentWebView.getTitle());
            } else {
                text = String.format(getString(R.string.Main_ToastTabSwitchMessage),
                                     mViewFlipper.getDisplayedChild() + 1);
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePreviousNextTabViewsVisibility() {
        if ((mUrlBarVisible) && (isSwitchTabsByButtonsEnabled())) {
            if (mViewFlipper.getDisplayedChild() > 0) {
                mPreviousTabView.setVisibility(View.VISIBLE);
            } else {
                mPreviousTabView.setVisibility(View.GONE);
            }

            if (mViewFlipper.getDisplayedChild() < mViewFlipper.getChildCount() - 1) {
                mNextTabView.setVisibility(View.VISIBLE);
            } else {
                mNextTabView.setVisibility(View.GONE);
            }
        } else {
            mPreviousTabView.setVisibility(View.GONE);
            mNextTabView.setVisibility(View.GONE);
        }
    }

    /**
     * Show the previous tab, if any.
     */
    private void showPreviousTab() {

        if (mViewFlipper.getChildCount() > 1) {

            if (mFindDialogVisible) {
                closeFindDialog();
            }

            mCurrentWebView.doOnPause();

            mViewFlipper.setInAnimation(AnimationManager.getInstance().getInFromLeftAnimation());
            mViewFlipper.setOutAnimation(AnimationManager.getInstance().getOutToRightAnimation());

            mViewFlipper.showPrevious();

            mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());

            mCurrentWebView.doOnResume();

            showToastOnTabSwitch();

            updatePreviousNextTabViewsVisibility();

            updateUI();
        }
    }

    /**
     * Show the next tab, if any.
     */
    private void showNextTab() {

        if (mViewFlipper.getChildCount() > 1) {

            if (mFindDialogVisible) {
                closeFindDialog();
            }

            mCurrentWebView.doOnPause();

            mViewFlipper.setInAnimation(AnimationManager.getInstance().getInFromRightAnimation());
            mViewFlipper.setOutAnimation(AnimationManager.getInstance().getOutToLeftAnimation());

            mViewFlipper.showNext();

            mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());

            mCurrentWebView.doOnResume();

            showToastOnTabSwitch();

            updatePreviousNextTabViewsVisibility();

            updateUI();
        }
    }

    /* 屏幕触摸时所触发的事件 */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideKeyboardFromUrlEditText();
        if (!isHomePage()) {
            setUrlBarVisibility(false);
            setButtomBarVisibility(false);
            setOpenToolBarVisibility(true);
        }
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * Check if the url is in the AdBlock white list.
     * 
     * @param url The url to check
     * @return true if the url is in the white list
     */
    private boolean checkInAdBlockWhiteList(String url) {

        if (url != null) {
            boolean inList = false;
            Iterator<String> iter = Controller.getInstance().getAdBlockWhiteList(this).iterator();
            while ((iter.hasNext()) && (!inList)) {
                if (url.contains(iter.next())) {
                    inList = true;
                }
            }
            return inList;
        } else {
            return false;
        }
    }

    /**
     * 页面加载完成后执行的动作
     * 
     * @param url
     */
    public void onPageFinished(String url) {
        updateUI();
        if ((Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_ADBLOCKER_ENABLE, true))
            && (!checkInAdBlockWhiteList(mCurrentWebView.getUrl()))) {
            mCurrentWebView.loadAdSweep();
        }
        WebIconDatabase.getInstance().retainIconForPageUrl(mCurrentWebView.getUrl());
        /* 隐藏加载状态栏 */
        setProgressBarLinearVisible(false);
        setStopLoadingBarVisibility(false);
    }

    /**
     * 页面开始加载时执行的动作
     * 
     * @param url
     */
    public void onPageStarted(String url) {
        if (mFindDialogVisible) {
            closeFindDialog();
        }
        boolean visible = Boolean.FALSE;
        /* 打开主页时显示地址栏及底部工具栏，其它页面则隐藏 */
        if (Constants.URL_BASE_URL.equals(url) || Constants.URL_ABOUT_START.equals(url)
            || url.equals(R.string.Main_AddressBarDefaultWords)) {
            visible = Boolean.TRUE;
        }
        setUrlBarAndButtomBarVisibility(visible);
        setOpenToolBarVisibility(!visible);
        /* 显示加载状态栏 */
        setProgressBarLinearVisible(true);
        setStopLoadingBarVisibility(!visible);
    }

    /**
     * 检测当前页面是否主页
     * 
     * @return
     */
    private boolean isHomePage() {
        boolean is = Boolean.FALSE;
        String currentUrl = mCurrentWebView.getUrl();
        if (Constants.URL_BASE_URL.equals(currentUrl) || Constants.URL_ABOUT_START.equals(currentUrl)
            || currentUrl.equals(R.string.Main_AddressBarDefaultWords)) {
            is = Boolean.TRUE;
        }
        return is;
    }

    /**
     * 设置加载进度条所在行是否显示
     * 
     * @param visible
     */
    private void setProgressBarLinearVisible(boolean visible) {
        if (visible) {
            if (!mProgressBarVisible) {
                mProgressBarLinear.setVisibility(View.VISIBLE);
            }
            mProgressBarVisible = true;
        } else {
            mProgressBarLinear.setVisibility(View.GONE);
            mProgressBarVisible = false;
        }
    }

    /**
     * 页面加载时候执行的动作
     * 
     * @param url
     */
    public void onUrlLoading(String url) {
        /* 这里可以放置一个页面加载效果 */
    }

    public void onMailTo(String url) {
        Intent sendMail = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(sendMail);
    }

    public void onExternalApplicationUrl(String url) {
        try {

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);

        } catch (Exception e) {

            // Notify user that the vnd url cannot be viewed.
            new AlertDialog.Builder(this).setTitle(R.string.Main_VndErrorTitle).setMessage(String.format(getString(R.string.Main_VndErrorMessage),
                                                                                                         url)).setPositiveButton(android.R.string.ok,
                                                                                                                                 new AlertDialog.OnClickListener() {

                                                                                                                                     public void onClick(DialogInterface dialog,
                                                                                                                                                         int which) {
                                                                                                                                     }
                                                                                                                                 }).setCancelable(true).create().show();
        }
    }

    public void setHttpAuthUsernamePassword(String host, String realm, String username, String password) {
        mCurrentWebView.setHttpAuthUsernamePassword(host, realm, username, password);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if ((item != null) && (item.getIntent() != null)) {
            Bundle b = item.getIntent().getExtras();

            switch (item.getItemId()) {
                case CONTEXT_MENU_OPEN:
                    if (b != null) {
                        navigateToUrl(b.getString(Constants.EXTRA_ID_URL));
                    }
                    return true;

                case CONTEXT_MENU_OPEN_IN_NEW_TAB:
                    if (b != null) {
                        addTab(false, mViewFlipper.getDisplayedChild());
                        navigateToUrl(b.getString(Constants.EXTRA_ID_URL));
                    }
                    return true;

                case CONTEXT_MENU_DOWNLOAD:
                    if (b != null) {
                        doDownloadStart(b.getString(Constants.EXTRA_ID_URL), null, null, null, 0);
                    }
                    return true;
                case CONTEXT_MENU_COPY:
                    if (b != null) {
                        ApplicationUtils.copyTextToClipboard(this, b.getString(Constants.EXTRA_ID_URL),
                                                             getString(R.string.Commons_UrlCopyToastMessage));
                    }
                    return true;
                case CONTEXT_MENU_SHARE:
                    if (b != null) {
                        ApplicationUtils.sharePage(this, "", b.getString(Constants.EXTRA_ID_URL));
                    }
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onDownloadEvent(String event, Object data) {
        if (event.equals(EventConstants.EVT_DOWNLOAD_ON_FINISHED)) {

            DownloadItem item = (DownloadItem) data;

            if (item.getErrorMessage() == null) {
                Toast.makeText(this, getString(R.string.Main_DownloadFinishedMsg), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.Main_DownloadErrorMsg, item.getErrorMessage()),
                               Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Gesture listener implementation.
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /* 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mCurrentWebView.zoomIn();
            return super.onDoubleTap(e);
        }

        /* 对滑动操作的处理 */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (isSwitchTabsByFlingEnabled()) {
                if (e2.getEventTime() - e1.getEventTime() <= FLIP_TIME_THRESHOLD) {
                    if (e2.getX() > (e1.getX() + FLIP_PIXEL_THRESHOLD)) {

                        showPreviousTab();
                        return false;
                    }

                    // going forwards: pushing stuff to the left
                    if (e2.getX() < (e1.getX() - FLIP_PIXEL_THRESHOLD)) {

                        showNextTab();
                        return false;
                    }
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }

}
