/**
 * 
 */
package com.onyx.android.sdk.ui.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @author Simon
 * 
 */
public class HTMLReaderWebView extends WebView
{

    private static final String TAG = "HTMLReaderWebView";

    private int mCurrentPage;
    private int mTotalPage;

    public static final int PAGE_TURN_TYPE_VERTICAL = 0;
    public static final int PAGE_TURN_TYPE_HORIZOTAL = 1;
    private int pageTurnType = PAGE_TURN_TYPE_HORIZOTAL;

    private int heightForSaveView = 50;
    private int pageTurnThreshold = 300;

    public void setPageTurnType(int pageTurnType)
    {
        this.pageTurnType = pageTurnType;
    }

    public void setHeightForSaveView(int heightForSaveView)
    {
        this.heightForSaveView = heightForSaveView;
    }

    public void setPageTurnThreshold(int pageTurnThreshold)
    {
        this.pageTurnThreshold = pageTurnThreshold;
    }

    @Override
    public void scrollTo(int x, int y)
    {
        super.scrollTo(mInternalScrollX, mInternalScrollY);
    }

    @Override
    public void draw(Canvas canvas)
    {
        scrollTo(mInternalScrollX, mInternalScrollY);
        super.draw(canvas);
    }

    @Override
    public void computeScroll()
    {
        scrollTo(mInternalScrollX, mInternalScrollY);
    }

    private int mInternalScrollX = 0;
    private int mInternalScrollY = 0;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        scrollTo(mInternalScrollX, mInternalScrollY);
        super.onScrollChanged(mInternalScrollX, mInternalScrollY, oldl, oldt);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl)
    {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public void setScroll(int l, int t)
    {
        mInternalScrollX = l;
        mInternalScrollY = t;
    }

    /**
     * @param context
     */
    public HTMLReaderWebView(Context context)
    {
        super(context);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public HTMLReaderWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public HTMLReaderWebView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
                Log.d(TAG, "newProgress = " + newProgress);
                if (newProgress == 100) {
                    setScroll(0, 0);
                    mCurrentPage = 0;
                }
            }
        });

        setOnTouchListener(new View.OnTouchListener() {
            float mX = 0;
            float mY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (pageTurnType == PAGE_TURN_TYPE_HORIZOTAL) {
                    switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mX = event.getX();
                        mY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        float x = event.getX();
                        float y = event.getY();

                        if (x - mX > pageTurnThreshold) {
                            prevPage();
                        }

                        if (mX - x > pageTurnThreshold) {
                            nextPage();
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        return true;

                    case MotionEvent.ACTION_CANCEL:
                        break;
                    }

                } else if (pageTurnType == PAGE_TURN_TYPE_VERTICAL) {
                    switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mX = event.getX();
                        mY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        float x = event.getX();
                        float y = event.getY();

                        if (y - mY > pageTurnThreshold) {
                            prevPage();
                        }

                        if (mY - y > pageTurnThreshold) {
                            nextPage();
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        return true;

                    case MotionEvent.ACTION_CANCEL:
                        break;
                    }
                }
                return false;
            }
        });

    }

    public interface OnPageChangedListener
    {
        public void onPageChanged(int totalPage, int curPage);
    }

    private OnPageChangedListener mOnPageChangedListener;

    public void registerOnOnPageChangedListener(OnPageChangedListener l)
    {
        mOnPageChangedListener = l;
    }

    public void unRegisterOnOnPageChangedListener()
    {
        mOnPageChangedListener = null;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        scrollTo(mInternalScrollX, mInternalScrollY);
        super.onDraw(canvas);
        refreshWebViewSize();
    }

    private void refreshWebViewSize()
    {
        int height = getHeight() - heightForSaveView;
        int scrollHeight = (int) (getContentHeight() - heightForSaveView);

        if (height == 0) {
            return;
        }

        mTotalPage = (scrollHeight + height - 1) / height;
        if (mCurrentPage > mTotalPage) {
            mCurrentPage = mTotalPage;
        }

        if (mCurrentPage <= 0) {
            mCurrentPage = 1;
        }

        if (mOnPageChangedListener != null)
            mOnPageChangedListener.onPageChanged(mTotalPage, mCurrentPage);
    }

    private void nextPage()
    {
        if (mCurrentPage < mTotalPage) {
            // EpdController.invalidate(webView, UpdateMode.GC);
            mCurrentPage++;
            setScroll(0, getScrollY() + getHeight() - heightForSaveView);
            scrollBy(0, getHeight() - heightForSaveView);
            if (mOnPageChangedListener != null)
                mOnPageChangedListener.onPageChanged(mTotalPage, mCurrentPage);
        }
    }

    private void prevPage()
    {
        if (mCurrentPage > 1) {
            // EpdController.invalidate(webView, UpdateMode.GC);
            mCurrentPage--;
            setScroll(0, getScrollY() - getHeight() + heightForSaveView);
            scrollBy(0, -(getHeight() - heightForSaveView));
            if (mOnPageChangedListener != null)
                mOnPageChangedListener.onPageChanged(mTotalPage, mCurrentPage);
        }
    }

}
