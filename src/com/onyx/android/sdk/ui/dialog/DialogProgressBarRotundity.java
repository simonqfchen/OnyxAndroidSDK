/**
 * 
 */
package com.onyx.android.sdk.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;

import com.onyx.android.sdk.R;

/**
 * @author qingyue
 *
 */
public class DialogProgressBarRotundity extends Dialog
{
    public interface onDismissDialogListener
    {
        public void dismissdialog();
    }
    private onDismissDialogListener mOnDismissDialogListener = new onDismissDialogListener()
    {
        
        @Override
        public void dismissdialog()
        {
            //do nothing
        }
    };
    public void setOnDismissdialogListener(onDismissDialogListener l)
    {
        mOnDismissDialogListener = l;
    }
    
    private boolean mIsDimissedByUser = false;

    public DialogProgressBarRotundity(Context context)
    {
        super(context, R.style.dialog_progress_style);

        setContentView(R.layout.dialog_progressbar_rotundity);
    }
    
    public boolean isDismissedByUser()
    {
        return mIsDimissedByUser;
    }

    @Override
        public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mIsDimissedByUser = true;
            this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void dismiss()
    {
        super.dismiss();
        mOnDismissDialogListener.dismissdialog();
    }
}
