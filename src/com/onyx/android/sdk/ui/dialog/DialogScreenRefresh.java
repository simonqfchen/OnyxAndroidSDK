/**
 * 
 */
package com.onyx.android.sdk.ui.dialog;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.onyx.android.sdk.R;
import com.onyx.android.sdk.data.sys.OnyxSysCenter;
import com.onyx.android.sdk.ui.data.SelectionAdapter;

/**
 * 
 * @author qingyue
 *
 */
public class DialogScreenRefresh extends DialogBaseSettings
{
    private final static String TAG = "DialogScreenRefresh";

    public interface onScreenRefreshListener
    {
        public void screenFefresh(int pageTurning);
    }
    private onScreenRefreshListener mOnScreenRefreshListener = new onScreenRefreshListener()
    {

        @Override
        public void screenFefresh(int pageTurning)
        {
            //do nothing
        }
    };
    public void setOnScreenRefreshListener(onScreenRefreshListener l)
    {
        mOnScreenRefreshListener = l;
    }
    
    /**
     * times to reset display to eliminate ghost pixels
     */
    public static int RENDER_RESET_MAX_TIME = 1;
    
    private SelectionAdapter mAdapter = null;
    final ArrayList<Pair<String, Object>> mItems = new ArrayList<Pair<String, Object>>();
    
    public DialogScreenRefresh(final Activity hostActivity, int pageTurning)
    {
        super(hostActivity);

        mItems.add(new Pair<String, Object>(this.getContext().getString(R.string.always), Integer.valueOf(1)));
        mItems.add(new Pair<String, Object>(this.getContext().getString(R.string.every_3_pages), Integer.valueOf(3)));
        mItems.add(new Pair<String, Object>(this.getContext().getString(R.string.every_5_pages), Integer.valueOf(5)));
        mItems.add(new Pair<String, Object>(this.getContext().getString(R.string.every_7_pages), Integer.valueOf(7)));
        mItems.add(new Pair<String, Object>(this.getContext().getString(R.string.every_9_pages), Integer.valueOf(9)));
        mItems.add(new Pair<String, Object>(this.getContext().getString(R.string.automatic), Integer.valueOf(Integer.MAX_VALUE)));
        RENDER_RESET_MAX_TIME = OnyxSysCenter.getScreenUpdateGCInterval();
        if (RENDER_RESET_MAX_TIME == -1) {
        	RENDER_RESET_MAX_TIME = 5;
        }
        
        mAdapter = new SelectionAdapter(hostActivity, this.getGridView(), mItems, 0);
        for (int i = 0; i < mItems.size(); i++) {
            if ((Integer)mItems.get(i).second == RENDER_RESET_MAX_TIME) {
                mAdapter.setSelection(i);
                break;
            }
        }
        this.getGridView().setAdapter(mAdapter);
        
        this.getButtonSet().setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                DialogScreenRefresh.this.dismiss();
                
                int sel = mAdapter.getSelection();
                if (sel >= 0) {
                    RENDER_RESET_MAX_TIME = (Integer)mItems.get(sel).second;
                }
                else {
                    RENDER_RESET_MAX_TIME = 1;
                }

                OnyxSysCenter.setScreenUpdateGCInterval(hostActivity , RENDER_RESET_MAX_TIME);
                mOnScreenRefreshListener.screenFefresh(RENDER_RESET_MAX_TIME);
                
                Log.d(TAG, "render reset time: " + RENDER_RESET_MAX_TIME);
            }
        });
        
        this.getButtonCancel().setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogScreenRefresh.this.cancel();
            }
        });

        this.getTextViewTitle().setText(R.string.screen_refresh);

        mAdapter.getPaginator().setPageSize(mItems.size());
    }
}
