/**
 * 
 */
package com.onyx.android.sdk.ui.dialog;

import com.onyx.android.sdk.R;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author peekaboo
 *
 */
public class DialogReaderMenuMore extends DialogBaseOnyx
{

    public DialogReaderMenuMore(Activity activity , View view)
    {
        super(activity);
        DialogReaderMenuMore.this.setContentView(view);
        Window window = DialogReaderMenuMore.this.getWindow();
    	WindowManager.LayoutParams layout_params = window.getAttributes();
    	layout_params.y = (int) activity.getResources().getDimension(R.dimen.reader_menu_dialog_y);
    	window.setAttributes(layout_params);

    }
}
