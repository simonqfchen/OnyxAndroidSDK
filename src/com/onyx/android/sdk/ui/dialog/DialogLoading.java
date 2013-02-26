/**
 * 
 */
package com.onyx.android.sdk.ui.dialog;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;

import com.onyx.android.sdk.R;

/**
 * 
 * @author qingyue
 *
 */
public class DialogLoading extends OnyxDialogBase
{
    public interface onFinishReaderListener
    {
        public void onFinishReader();
    }
    private onFinishReaderListener mOnFinishReaderListener = new onFinishReaderListener()
    {

        @Override
        public void onFinishReader()
        {
            //do nothing
        }
    };
    public void SetOnFinishReaderListener(onFinishReaderListener l)
    {
        mOnFinishReaderListener = l;
    }

    private TextView mTextViewMessage = null;

    public DialogLoading(Context context, String msg)
    {
        super(context);

        setContentView(R.layout.dialog_loading);
        mTextViewMessage = (TextView) findViewById(R.id.textview_message);
        mTextViewMessage.setText(msg);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String msg)
    {
        if (!mTextViewMessage.getText().equals(msg)) {
            mTextViewMessage.setText(msg);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.cancel();
            mOnFinishReaderListener.onFinishReader();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
