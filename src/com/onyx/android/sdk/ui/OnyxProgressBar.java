package com.onyx.android.sdk.ui;

import com.onyx.android.sdk.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class OnyxProgressBar extends ImageView
{
    private PaintFlagsDrawFilter mPaintFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    private int[] mRotate = new int[]{0, 90, 180, 270 };
    private int mProgress = 0;
    private boolean mIsInit = true;
    private ProgressBarTask mTask = new ProgressBarTask();

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
        setImageResource(R.drawable.menu_loading);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mProgress++;
        if (mProgress >= mRotate.length) {
            mProgress = 0;
        }

        canvas.rotate(mRotate[mProgress], getWidth() / 2, getHeight() / 2);
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
            OnyxProgressBar.this.invalidate();
        }
    }
}
