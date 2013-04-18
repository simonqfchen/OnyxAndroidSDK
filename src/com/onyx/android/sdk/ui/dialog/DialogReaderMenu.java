package com.onyx.android.sdk.ui.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.onyx.android.sdk.R;
import com.onyx.android.sdk.device.DeviceInfo;
import com.onyx.android.sdk.device.EpdController;
import com.onyx.android.sdk.device.EpdController.EPDMode;
import com.onyx.android.sdk.device.IDeviceFactory.TouchType;
import com.onyx.android.sdk.ui.util.WindowUtil;


public class DialogReaderMenu extends OnyxDialogBase
{
    private final static String TAG = "DialogReaderMenu";

    public static enum LineSpacingProperty {normal, big, small, decreases, enlarge};
    public static enum RotationScreenProperty {rotation_0, rotation_90, rotation_180, rotation_270};
    public static enum FontSizeProperty {increase, decrease};

    public static interface IMenuHandler
    {
        public int getPageIndex();
        public int getPageCount();

        public void nextPage();
        public void previousPage();
        public void gotoPage(int i);

        public void updateCurrentPage(LinearLayout l);

        public void increaseFontSize();
        public void decreaseFontSize();
        public void changeFontsize(FontSizeProperty property);

        public void toggleFontEmbolden();

        public void showSetFontView();
        public String getFontFace();
        public void setFontFace();

        public void rotationScreen(int i);
        public void changeRotationScreen(int orientation);

        public void showLineSpacingView();
        public void setLineSpacing(LineSpacingProperty property);

        public void showTOC();
        public void showBookMarks();
        public void showTTsView();
        public void showAnnotation();

        public void searchContent();
        public void startDictionary();
        public void showGoToPageDialog();

        public void zoomToPage();
        public void zoomToWidth();
        public void zoomToHeight();
        public void zoomBySelection();
        public void zoomByTwoPoints();
        public void zoomByValue(double z);
        public void zoomIn();
        public void zoomOut();

        public void toggleFullscreen();
        public boolean showZoomSettings();
        public boolean canChangeFontFace();
        public boolean isFullscreen();
        public void setScreenRefresh();
        public void showReaderSettings();

        public boolean ttsIsSpeaking();
        public void ttsInit();
        public void ttsSpeak();
        public void ttsPause();
        public void ttsStop();
    }

    private long mThreadId = -1;
    private Handler mHandler = new Handler();

    private TextView mTextViewChildLines = null;
    private TextView mTextViewLines = null;
    private RelativeLayout mLayoutMainMenu = null;
    private LinearLayout mLayoutSecondaryMenu = null;
    private LinearLayout mLayoutChild = null;
    private LayoutInflater mInflater = null;
    private View mMoreView = null;
    private View mFontSettings = null;
    private View mLineSpacingSettings = null;
    private View mTTsView = null;
    private View mRotationView = null;
    private View mZoomSettings = null;
    private View mShowDirectory = null;

    private RelativeLayout mLayoutLineSpacingSmall = null;
    private RelativeLayout mLayoutLineSpacingBig = null;
    private RelativeLayout mLayoutLineSpacingNormal = null;

    private LinearLayout mLayoutRotation_90 = null;
    private LinearLayout mLayoutRotation_180 = null;
    private LinearLayout mLayoutRotation_270 = null;
    private LinearLayout mLayoutRotation_0 = null;

    private RelativeLayout mLayoutFontIncrease = null;
    private RelativeLayout mLayoutFontDecrease = null;
    private RelativeLayout mLayoutFontEmbolden = null;
    private Button mButtonFontFace = null;

    private LinearLayout mPageInfo = null;
    private TextView mCurrentPageTextView  = null;
    private TextView mTotalPageTextView = null;
    private Activity mActivity = null;

    WindowManager.LayoutParams mParams = null;
    Window mWindow = null;

    private IMenuHandler mMenuHandler = null;

    private boolean mIsShowChildMenu = false;
    private boolean mIsInitReaderMenu = true;

    private int mTextViewChildLineResoruce = -1;

    private GestureDetector mGestureDetector = null;
    private final int mChildLines = 26;

    private static int sRotationScreen = -1;

    private ImageButton mToggleStartStop = null;

    private EpdController.EPDMode mEpdModeBackup = EpdController.EPDMode.AUTO;

    private SeekBar mVolumeSeekBar = null;
    private AudioManager mAudioManager;
    private int mMaxVolume;

    public DialogReaderMenu(Activity activity, final IMenuHandler menuHandler)
    {
        super(activity, R.style.dialog_menu);

        setContentView(R.layout.dialog_menu);
        mActivity = activity;
        mThreadId = Thread.currentThread().getId();

        mLayoutMainMenu = (RelativeLayout) findViewById(R.id.layout_main_menu);
        mLayoutSecondaryMenu = (LinearLayout) findViewById(R.id.layout_secondary_menu);
        mLayoutChild = (LinearLayout) mLayoutSecondaryMenu.findViewById(R.id.layout_child);
        mTextViewChildLines = (TextView) mLayoutSecondaryMenu.findViewById(R.id.textview_child_lines);
        mTextViewLines = (TextView) mLayoutMainMenu.findViewById(R.id.textview_line);
        mTextViewLines.setVisibility(View.GONE);

        mInflater = LayoutInflater.from(mActivity);
        mMoreView = mInflater.inflate(R.layout.menu_more_view, null);
        mFontSettings = mInflater.inflate(R.layout.menu_font_settings, null);
        mLineSpacingSettings = mInflater.inflate(R.layout.menu_line_spacing_settings, null);
        mTTsView = mInflater.inflate(R.layout.menu_tts_view, null);
        mRotationView = mInflater.inflate(R.layout.menu_rotation_settings, null);
        mZoomSettings = mInflater.inflate(R.layout.menu_zoom_settings, null);
        mCurrentPageTextView = (TextView)findViewById(R.id.textview_current_page);
        mTotalPageTextView = (TextView)findViewById(R.id.textview_total_page);
        mShowDirectory = mInflater.inflate(R.layout.menu_directory_view, null);

        mLayoutRotation_0 = (LinearLayout) mRotationView.findViewById(R.id.linearlayout_rotation_0);
        mLayoutRotation_90 = (LinearLayout) mRotationView.findViewById(R.id.linearlayout_rotation_90);
        mLayoutRotation_180 = (LinearLayout) mRotationView.findViewById(R.id.linearlayout_rotation_180);
        mLayoutRotation_270 = (LinearLayout) mRotationView.findViewById(R.id.linearlayout_rotation_270);

        mMenuHandler = menuHandler;

        mLayoutRotation_0.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();

                int orientation = getOrientation(RotationScreenProperty.rotation_0);
                if (orientation != -1) {
                    mMenuHandler.changeRotationScreen(orientation);
                }
            }
        });
        mLayoutRotation_90.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();

                int orientation = getOrientation(RotationScreenProperty.rotation_90);
                if (orientation != -1) {
                    mMenuHandler.changeRotationScreen(orientation);
                }
            }
        });
        mLayoutRotation_180.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();

                int orientation = getOrientation(RotationScreenProperty.rotation_180);
                if (orientation != -1) {
                    mMenuHandler.changeRotationScreen(orientation);
                }
            }
        });
        mLayoutRotation_270.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();

                int orientation = getOrientation(RotationScreenProperty.rotation_270);
                if (orientation != -1) {
                    mMenuHandler.changeRotationScreen(orientation);
                }
            }
        });

        mLayoutFontDecrease = (RelativeLayout) mFontSettings.findViewById(R.id.layout_font_decrease);
        mLayoutFontIncrease = (RelativeLayout) mFontSettings.findViewById(R.id.layout_font_increase);
        mLayoutFontEmbolden = (RelativeLayout) mFontSettings.findViewById(R.id.layout_font_embolden);
        mButtonFontFace = (Button) mFontSettings.findViewById(R.id.button_font_face);
        if (!mMenuHandler.canChangeFontFace()) {
        	mFontSettings.findViewById(R.id.relativelayout_font_face).setVisibility(View.GONE);
        	mFontSettings.findViewById(R.id.relativelayout_font_type).setVisibility(View.GONE);
        }

        mLayoutFontDecrease.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.decreaseFontSize();
            }
        });
        mLayoutFontIncrease.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.increaseFontSize();
            }
        });
        mLayoutFontEmbolden.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.toggleFontEmbolden();
            }
        });
        mFontSettings.findViewById(R.id.relativelayout_font_face).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.setFontFace();
            }
        });

        mLayoutLineSpacingBig = (RelativeLayout) mLineSpacingSettings.findViewById(R.id.layout_spacing_big);
        mLayoutLineSpacingSmall = (RelativeLayout) mLineSpacingSettings.findViewById(R.id.layout_spacing_small);
        mLayoutLineSpacingNormal = (RelativeLayout) mLineSpacingSettings.findViewById(R.id.layout_spacing_normal);

        mLayoutLineSpacingNormal.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.setLineSpacing(LineSpacingProperty.normal);
            }
        });
        mLayoutLineSpacingBig.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.setLineSpacing(LineSpacingProperty.big);
            }
        });
        mLayoutLineSpacingSmall.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.setLineSpacing(LineSpacingProperty.small);
            }
        });
        RelativeLayout lineSpacingEnlarge = (RelativeLayout) mLineSpacingSettings.findViewById(R.id.layout_spacing_enlarge);
        lineSpacingEnlarge.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.setLineSpacing(LineSpacingProperty.enlarge);
            }
        });
        RelativeLayout lineSpacingDecreases = (RelativeLayout) mLineSpacingSettings.findViewById(R.id.layout_spacing_decreases);
        lineSpacingDecreases.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.setLineSpacing(LineSpacingProperty.decreases);
            }
        });

        RelativeLayout layout_more = (RelativeLayout) mLayoutSecondaryMenu.findViewById(R.id.layout_more);
        layout_more.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showChildMenu(R.drawable.item_selected_6, mMoreView);
            }
        });

        RelativeLayout layout_enlarge = (RelativeLayout) mZoomSettings.findViewById(R.id.layout_enlarge);
        layout_enlarge.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.zoomIn();
            }
        });

        RelativeLayout layout_narrow = (RelativeLayout) mZoomSettings.findViewById(R.id.layout_narrow);
        layout_narrow.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.zoomOut();
            }
        });

        RelativeLayout layout_fitWidth = (RelativeLayout) mZoomSettings.findViewById(R.id.layout_fit_width);
        layout_fitWidth.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.zoomToWidth();
            }
        });

        RelativeLayout layout_fitPage = (RelativeLayout) mZoomSettings.findViewById(R.id.layout_fit_page);
        layout_fitPage.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.zoomToPage();
            }
        });

        ImageButton imageButtonNavigationBar = (ImageButton) mZoomSettings.findViewById(R.id.imagebutton_navigation_bar);
        imageButtonNavigationBar.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.zoomByValue(1.0);
            }
        });

        RelativeLayout layout_twoPointsEnlarge = (RelativeLayout) mZoomSettings.findViewById(R.id.layout_two_points_enlarge);
        layout_twoPointsEnlarge.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.zoomByTwoPoints();
            }
        });

        if (DeviceInfo.singleton().getDeviceController().getTouchType(activity) == TouchType.None) {
        	mZoomSettings.findViewById(R.id.layout_cutting_edge).setVisibility(View.GONE);
        } else {
			RelativeLayout layout_cuttingEdge = (RelativeLayout) mZoomSettings.findViewById(R.id.layout_cutting_edge);
			layout_cuttingEdge.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mMenuHandler.zoomBySelection();
				}
			});
        }
        
        Button increaseFontButton = (Button)findViewById(R.id.button_font_size_increase);
        increaseFontButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.increaseFontSize();
            }
        });

        Button decreaseFontButton = (Button)findViewById(R.id.button_font_size_decrease);
        decreaseFontButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.decreaseFontSize();
            }
        });

        ImageButton prevPageButton = (ImageButton)findViewById(R.id.button_previous);
        prevPageButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.previousPage();
            }
        });
        ImageButton nextPageButton = (ImageButton)findViewById(R.id.button_next);
        nextPageButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.nextPage();
            }
        });
        Button rotationScreenButton = (Button)findViewById(R.id.button_back);
        rotationScreenButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.rotationScreen(sRotationScreen);
                sRotationScreen = -sRotationScreen;
                DialogReaderMenu.this.dismiss();
            }
        });

        RelativeLayout layout_rotation = (RelativeLayout) mLayoutSecondaryMenu.findViewById(R.id.layout_rotation);
        layout_rotation.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showChildMenu(R.drawable.item_selected_5, mRotationView);
            }
        });

        RelativeLayout layout_directory = (RelativeLayout) mLayoutSecondaryMenu.findViewById(R.id.layout_toc);
        layout_directory.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showChildMenu(R.drawable.item_selected_3, mShowDirectory);
            }
        });

        RelativeLayout layout_toc = (RelativeLayout) mShowDirectory.findViewById(R.id.layout_toc);
        layout_toc.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();
                menuHandler.showTOC();
            }
        });

        RelativeLayout layout_bookmark = (RelativeLayout) mShowDirectory.findViewById(R.id.layout_bookmark);
        layout_bookmark.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();
                menuHandler.showBookMarks();
            }
        });

        RelativeLayout layout_annotation = (RelativeLayout) mShowDirectory.findViewById(R.id.layout_annotation);
        if(DeviceInfo.singleton().getDeviceController().getTouchType(activity) == TouchType.None){
        	layout_annotation.setVisibility(View.GONE);
        } else {
        	layout_annotation.setOnClickListener(new View.OnClickListener()
        	{

        		@Override
        		public void onClick(View v)
        		{
        			DialogReaderMenu.this.dismiss();
        			menuHandler.showAnnotation();
        		}
        	});
        }
        
        RelativeLayout layout_dictionary = (RelativeLayout) mMoreView.findViewById(R.id.layout_dictionary);
        layout_dictionary.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();
                mMenuHandler.startDictionary();
            }
        });
        
      if (!DeviceInfo.singleton().getDeviceController().hasAudio(activity)) {
    	  layout_dictionary.setVisibility(View.GONE);
    	  findViewById(R.id.layout_tts).setVisibility(View.GONE);
    	  RelativeLayout foot_layout_dictionary = (RelativeLayout)findViewById(R.id.layout_dictionary);
    	  foot_layout_dictionary.setVisibility(View.VISIBLE);
    	  foot_layout_dictionary.setOnClickListener(new View.OnClickListener()
    	  {

    		  @Override
    		  public void onClick(View v)
    		  {
    			  mMenuHandler.startDictionary();
    		  }
    	  });
      }

        RelativeLayout layout_search = (RelativeLayout) mMoreView.findViewById(R.id.layout_search);
        layout_search.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();
                mMenuHandler.searchContent();
            }
        });

        RelativeLayout layout_screen = (RelativeLayout) mMoreView.findViewById(R.id.layout_screen);
        ImageView imageView_screen = (ImageView) mMoreView.findViewById(R.id.imageview_screen);
        TextView textView_screen = (TextView) mMoreView.findViewById(R.id.textview_screen);
        if (mMenuHandler.isFullscreen()) {
            imageView_screen.setImageResource(R.drawable.cancel_full_screen);
            textView_screen.setText(R.string.menu_item_screen_exit_full);
        }
        else {
            imageView_screen.setImageResource(R.drawable.full_screen);
            textView_screen.setText(R.string.menu_item_screen_full);
        }
        layout_screen.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.toggleFullscreen();
            }
        });

        RelativeLayout layout_settings = (RelativeLayout) mMoreView.findViewById(R.id.layout_settings);
        layout_settings.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogReaderMenu.this.dismiss();

                mMenuHandler.showReaderSettings();
            }
        });

        RelativeLayout layout_refresh = (RelativeLayout) mMoreView.findViewById(R.id.layout_refresh);
        layout_refresh.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mMenuHandler.setScreenRefresh();
            }
        });

        RelativeLayout layout_tts = (RelativeLayout) mLayoutSecondaryMenu.findViewById(R.id.layout_tts);
        layout_tts.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                showChildMenu(R.drawable.item_selected_1, mTTsView);
                menuHandler.ttsInit();
                setTtsState(menuHandler.ttsIsSpeaking());
            }
        });

        mToggleStartStop = (ImageButton) mTTsView.findViewById(R.id.imagebutton_tts_start);
        mToggleStartStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (menuHandler.ttsIsSpeaking()) {
                    menuHandler.ttsPause();
                }
                else {
                    menuHandler.ttsSpeak();
                }
                setTtsState(menuHandler.ttsIsSpeaking());
            }
        });

        ImageButton tts_stop = (ImageButton) mTTsView.findViewById(R.id.imagebutton_tts_stop);
        tts_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menuHandler.ttsStop();
                setTtsState(menuHandler.ttsIsSpeaking());
            }
        });

        ImageButton volume_increase = (ImageButton) mTTsView.findViewById(R.id.imagebutton_tts_volume_increase);
        volume_increase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                adjustVolume(true);
            }
        });

        ImageButton volume_down = (ImageButton) mTTsView.findViewById(R.id.imagebutton_tts_volume_down);
        volume_down.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adjustVolume(false);

            }
        });

        mVolumeSeekBar = (SeekBar) mTTsView.findViewById(R.id.progressbar_tts_volume);
        mVolumeSeekBar.setMax(100);
        mVolumeSeekBar.setProgress(getVolume());
        mVolumeSeekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        showLineSpacingOrZoomSettings();

        RelativeLayout layout_font = (RelativeLayout) mLayoutSecondaryMenu.findViewById(R.id.layout_font);
        layout_font.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mButtonFontFace.setText(mMenuHandler.getFontFace());
                showChildMenu(R.drawable.item_selected_4, mFontSettings);
            }
        });

        mPageInfo = (LinearLayout)findViewById(R.id.page_info_panel);
        mPageInfo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                menuHandler.showGoToPageDialog();
            }
        });

        mCurrentPageTextView.setText(String.valueOf(mMenuHandler.getPageIndex()));
        mTotalPageTextView.setText(String.valueOf(mMenuHandler.getPageCount()));

        mWindow = getWindow();
        mParams = mWindow.getAttributes();
        mParams.width = mWindow.getWindowManager().getDefaultDisplay().getWidth();
        mParams.y = mWindow.getWindowManager().getDefaultDisplay().getHeight();
        mWindow.setAttributes(mParams);
    }

    private void showLineSpacingOrZoomSettings()
    {
        RelativeLayout layout_spacing = (RelativeLayout) mLayoutSecondaryMenu.findViewById(R.id.layout_spacing);
        ImageView imageView = (ImageView) mLayoutSecondaryMenu.findViewById(R.id.imageview_line_spacing);
        TextView textView = (TextView) mLayoutSecondaryMenu.findViewById(R.id.textview_line_spacing);
        if (mMenuHandler.showZoomSettings()) {
            imageView.setImageResource(R.drawable.zoom);
            textView.setText(R.string.menu_item_zoom);
        }
        else {
            imageView.setImageResource(R.drawable.line_spacing);
            textView.setText(R.string.menu_item_line_spacing);
        }
        layout_spacing.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mMenuHandler.showZoomSettings()) {
                    showChildMenu(R.drawable.item_selected_2, mZoomSettings);
                }
                else {
                    showChildMenu(R.drawable.item_selected_2, mLineSpacingSettings);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(this.getContext(), new SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e)
                {
                    // if single tap happens outside of dialog, then auto close dialog for convenience
                    // assume dialog at bottom of the screen
                    int out_range = -10;
                    if (e.getY() < out_range) {
                        DialogReaderMenu.this.dismiss();
                        return true;
                    }
                    else {
                        return super.onSingleTapConfirmed(e);
                    }
                }
            });
        }

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void show()
    {
        mEpdModeBackup = EpdController.getMode();
        Log.d(TAG, "backup original EPD mode: " + mEpdModeBackup);
        EpdController.setMode(this.getContext(), EPDMode.AUTO);

        if (WindowUtil.isFullScreen(mActivity.getWindow())) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        this.setTtsState(mMenuHandler.ttsIsSpeaking());
        
        super.show();
    }

    @Override
    public void dismiss()
    {
        EpdController.setMode(this.getContext(), mEpdModeBackup);
        Log.d(TAG, "restore EPD mode: " + mEpdModeBackup);

        if (WindowUtil.isFullScreen(mActivity.getWindow())) {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        super.dismiss();
    }

    private void showChildMenu(int backgroundresoruce, View childView)
    {
        android.view.ViewGroup.LayoutParams params = mLayoutSecondaryMenu.getLayoutParams();

        if (mIsShowChildMenu && (backgroundresoruce != mTextViewChildLineResoruce)) {
            mTextViewChildLineResoruce = backgroundresoruce;
            mTextViewChildLines.setBackgroundResource(backgroundresoruce);

            mLayoutChild.removeAllViews();
            childView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            mLayoutChild.addView(childView);
        }
        else if (!mIsShowChildMenu) {
            mTextViewChildLineResoruce = backgroundresoruce;

            params.height = mLayoutSecondaryMenu.getHeight() * 2 + mChildLines;
            mLayoutSecondaryMenu.setLayoutParams(params);
            mLayoutChild.setVisibility(View.VISIBLE);
            mTextViewChildLines.setVisibility(View.VISIBLE);
            mTextViewChildLines.setBackgroundResource(backgroundresoruce);

            mLayoutChild.removeAllViews();
            childView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            mLayoutChild.addView(childView);

            mIsShowChildMenu = true;
        }
    }

    public void setButtonFontFaceText(String text)
    {
        mButtonFontFace.setText(text);
    }

    public interface onShowTTsViewLinsener
    {
        public void showTTsView();
    }
    public onShowTTsViewLinsener mOnShowTTsViewLinsener = new onShowTTsViewLinsener()
    {

        @Override
        public void showTTsView()
        {
            //do nothing
        }
    };
    public void setOnShowTTsViewLinsener(onShowTTsViewLinsener l)
    {
        mOnShowTTsViewLinsener = l;
    }

    public interface onShowLineSpacingViewLinsener
    {
        public void showLineSpacingView();
    }
    public onShowLineSpacingViewLinsener mOnShowLineSpacingViewLinsener = new onShowLineSpacingViewLinsener()
    {

        @Override
        public void showLineSpacingView()
        {
            //do nothing
        }
    };
    public void setOnShowLineSpacingViewLinsener(onShowLineSpacingViewLinsener l)
    {
        mOnShowLineSpacingViewLinsener = l;
    }

    public LinearLayout getPageInfoPanel()
    {
        return mPageInfo;
    }

    public void setPageIndex(final int current) {
        if (Thread.currentThread().getId() == mThreadId) {
            mCurrentPageTextView.setText(String.valueOf(current));
        }
        else {
            mHandler.post(new Runnable()
            {

                @Override
                public void run()
                {
                    mCurrentPageTextView.setText(String.valueOf(current));
                }
            });
        }
    }
    public void setPageCount(final int total) {
        if (Thread.currentThread().getId() == mThreadId) {
            mTotalPageTextView.setText(String.valueOf(total));
        }
        else {
            mHandler.post(new Runnable()
            {

                @Override
                public void run()
                {
                    mTotalPageTextView.setText(String.valueOf(total));
                }
            });
        }
    }

    public void setTtsState(final boolean state) {
        if (Thread.currentThread().getId() == mThreadId) {
            if(!state) {
                mToggleStartStop.setImageResource(R.drawable.tts_start);
            }
            else {
                mToggleStartStop.setImageResource(R.drawable.tts_pause);
            }
        }
        else {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if(!state) {
                        mToggleStartStop.setImageResource(R.drawable.tts_start);
                    }
                    else {
                        mToggleStartStop.setImageResource(R.drawable.tts_pause);
                    }

                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if (mIsInitReaderMenu) {
            showChildMenu(R.drawable.item_selected_6, mMoreView);
            mIsInitReaderMenu = false;
        }
    }

    private AudioManager getAudioManager() {
        if ( mAudioManager==null ) {
            mAudioManager = (AudioManager)this.getContext().getSystemService(Context.AUDIO_SERVICE);
            if(mAudioManager != null) {
                mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            }
        }
        return mAudioManager;
    }

    private int getVolume() {
        AudioManager am = getAudioManager();
        if (am!=null) {
            return am.getStreamVolume(AudioManager.STREAM_MUSIC) * 100 / mMaxVolume;
        }
        return 0;
    }

    private void setVolume( int volume ) {
        AudioManager am = getAudioManager();
        if (am!=null) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, volume * mMaxVolume / 100, 0);
        }
    }

    private void adjustVolume(boolean opition) {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(
                Context.AUDIO_SERVICE);
        if (audioManager != null) {
            if (opition) {
                audioManager.adjustSuggestedStreamVolume(AudioManager.ADJUST_RAISE,
                        AudioManager.USE_DEFAULT_STREAM_TYPE,
                                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            } else {
                audioManager.adjustSuggestedStreamVolume(AudioManager.ADJUST_LOWER,
                        AudioManager.USE_DEFAULT_STREAM_TYPE,
                                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
        updateVolumeSeekBar();
    }

    private void updateVolumeSeekBar() {
        mVolumeSeekBar.setProgress(getVolume());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private int getOrientation(RotationScreenProperty property)
    {
        int orientation = -1;
        int current_orientation = mActivity.getRequestedOrientation();
        if (property == RotationScreenProperty.rotation_90) {
            if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
            else {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        }
        else if (property == RotationScreenProperty.rotation_180) {
            if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            else {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
        else if (property == RotationScreenProperty.rotation_270) {
            if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
            else if (current_orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            else {
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
        }

        return orientation;
    }
}
