package com.onyx.android.sdk.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import com.onyx.android.sdk.R;

public class OnyxDialogBase extends Dialog
{
    public OnyxDialogBase(Context context)
    {
        super(context, R.style.dialog_no_title);

        setCanceledOnTouchOutside(true);
    }

    public OnyxDialogBase(Context context, int style)
    {
        super(context, style);

        setCanceledOnTouchOutside(true);
    }
}