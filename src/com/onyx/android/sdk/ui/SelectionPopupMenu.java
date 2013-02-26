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
import android.widget.ImageButton;
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
        public void translation();
        public void addBookmark();
        public void dismiss();
    }
    private boolean mIsShow = false;
    private final Activity myActivity;

    public SelectionPopupMenu(Activity activity, final ISelectionHandler handler, RelativeLayout layout)
    {
        super(activity);
        myActivity = activity;

        setFocusable(false);

        final LayoutInflater inflater =
                (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_panel_floating, this, true);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.addView(this, p);

        ImageButton imagebuttonCopy = (ImageButton) findViewById(R.id.imagebutton_copy);
        imagebuttonCopy.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.copy();
            }
        });

        ImageButton imagebuttonShare = (ImageButton) findViewById(R.id.imagebutton_share);
        imagebuttonShare.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.share();
            }
        });

        ImageButton imagebuttonTranslation = (ImageButton) findViewById(R.id.imagebutton_translation);
        imagebuttonTranslation.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.translation();
            }
        });

        ImageButton imagebuttonBookmark = (ImageButton) findViewById(R.id.imagebutton_bookmark);
        imagebuttonBookmark.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.addBookmark();
            }
        });

        ImageButton imagebuttonDismiss = (ImageButton) findViewById(R.id.imagebutton_dismiss);
        imagebuttonDismiss.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                handler.dismiss();
                SelectionPopupMenu.this.hide();
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
                setVisibility(View.GONE);
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

        final int verticalPosition; 
        final int screenHeight = ((View)this.getParent()).getHeight();
        final int diffTop = screenHeight - selectionEndY;
        final int diffBottom = selectionStartY;
        if (diffTop > diffBottom) {
            verticalPosition = diffTop > this.getHeight() + 20
                    ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.CENTER_VERTICAL;
        } else {
            verticalPosition = diffBottom > this.getHeight() + 20
                    ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.CENTER_VERTICAL;
        }

        layoutParams.addRule(verticalPosition);
        setLayoutParams(layoutParams);
    }

    public boolean isShow()
    {
        return mIsShow;
    }
}
