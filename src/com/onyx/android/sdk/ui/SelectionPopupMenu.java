/**
 * 
 */

package com.onyx.android.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.onyx.android.sdk.R;

/**
 * 
 * @author qingyue
 *
 */
public class SelectionPopupMenu extends LinearLayout {

    public static interface ISelectionHandler
    {
        public void copy();
        public void share();
        public void showDictionary();
        public void addAnnotation();
        public void dismiss();
        public void highLight();
        public void refresh();
    }
    private boolean mIsShow = false;
    private final Activity myActivity;
    private ISelectionHandler mHandler;
    private ImageView mButtonCancel;

    public SelectionPopupMenu(Activity activity, final ISelectionHandler handler, RelativeLayout layout)
    {
        super(activity);
        myActivity = activity;
        mHandler = handler;

        setFocusable(false);

        final LayoutInflater inflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_panel_floating, this, true);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.addView(this, p);


        mButtonCancel = new ImageView(activity);
        mButtonCancel.setImageDrawable(activity.getResources().getDrawable(R.drawable.selection_close_default));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mButtonCancel.setLayoutParams(params);
        layout.addView(mButtonCancel);
        mButtonCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                handler.dismiss();
                SelectionPopupMenu.this.hide();
            }
        });

        LinearLayout imagebuttonHightLight = (LinearLayout) findViewById(R.id.imagebutton_highlight);
        imagebuttonHightLight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handler.highLight();
                SelectionPopupMenu.this.hide();
            }
        });

        LinearLayout imagebuttonCopy = (LinearLayout) findViewById(R.id.imagebutton_share);
        imagebuttonCopy.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.share();
            }
        });

        LinearLayout imagebuttonTranslation = (LinearLayout) findViewById(R.id.imagebutton_dictionary);
        imagebuttonTranslation.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.showDictionary();
                handler.dismiss();
                SelectionPopupMenu.this.hide();
            }
        });

        LinearLayout imagebuttonBookmark = (LinearLayout) findViewById(R.id.imagebutton_annotaion);
        imagebuttonBookmark.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.addAnnotation();
            }
        });

        setVisibility(View.GONE);
    }

    Activity getActivity()
    {
        return myActivity;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return true;
    }

    public void show()
    {
        myActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                setVisibility(View.VISIBLE);
                mButtonCancel.setVisibility(View.VISIBLE);
                mIsShow = true;
            }
        });
    }

    public void hide()
    {
        myActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                mHandler.refresh();
                setVisibility(View.GONE);
                mButtonCancel.setVisibility(View.GONE);
                mIsShow = false;
            }
        });
    }

    public void move(int selectionStartY, int selectionEndY) {
        if (this == null) {
            return;
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.bottomMargin = 80;
        params.rightMargin = 25;
        mButtonCancel.setLayoutParams(params);
    }

    public boolean isShow()
    {
        return mIsShow;
    }
}
