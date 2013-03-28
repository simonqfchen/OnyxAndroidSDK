package com.onyx.android.sdk.ui;

import com.onyx.android.sdk.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class OnyxProgressBar extends ImageView
{
    private PaintFlagsDrawFilter mPaintFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    private boolean mIsInit = true;
    private ProgressBarTask mTask = new ProgressBarTask();
    private AnimationDrawable mAnimationDrawable = null;

    public OnyxProgressBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public OnyxProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public OnyxProgressBar(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setImageResource(R.anim.reader_loading_animation);
        mAnimationDrawable = (AnimationDrawable) this.getDrawable();  
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
    	if(mAnimationDrawable != null) {
    		mAnimationDrawable.start();
    	}
        canvas.setDrawFilter(mPaintFilter);
        super.onDraw(canvas);

        if (mIsInit) {
            mTask.execute();
            mIsInit = false;
        }
    }

    class ProgressBarTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            while (OnyxProgressBar.this.isShown()) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(params);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
        	if(mAnimationDrawable != null) {
        		mAnimationDrawable.stop();
        	}
            OnyxProgressBar.this.invalidate();
        }
    }
}
