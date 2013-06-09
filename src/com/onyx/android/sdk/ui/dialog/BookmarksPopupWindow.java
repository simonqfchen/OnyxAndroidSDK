package com.onyx.android.sdk.ui.dialog;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.onyx.android.sdk.R;
import com.onyx.android.sdk.ui.DirectoryGridView;
import com.onyx.android.sdk.ui.data.DirectoryItem;
import com.onyx.android.sdk.ui.data.GridViewDirectoryAdapter;
import com.onyx.android.sdk.ui.dialog.DialogDirectory.IEditPageHandler;
import com.onyx.android.sdk.ui.dialog.DialogDirectory.IGotoPageHandler;

/**
 * @author cap
 *
 */
public class BookmarksPopupWindow extends PopupWindow implements View.OnKeyListener
{
    private DialogDirectory mDialogDirectory = null;
    private DirectoryItem mDirectoryItem = null;
    private IGotoPageHandler mIGotoPageHandler = null;
    private IEditPageHandler mIEditPageHandler = null;

    private DirectoryGridView mGridView = null;
    private int position = -1;

    private ImageButton gotoButton = null;
    private ImageButton deleButton = null;
    private ImageButton exitButton = null;

    public BookmarksPopupWindow (View layout, DialogDirectory dialogDirectory,
            DirectoryItem item, IGotoPageHandler iGoto, IEditPageHandler iEdit) {
        this(layout, dialogDirectory, item, iGoto, iEdit, null, -1);
    }

    public BookmarksPopupWindow (View layout, DialogDirectory dialogDirectory,
            DirectoryItem item, IGotoPageHandler iGoto, IEditPageHandler iEdit,
            DirectoryGridView onyxGridView, int position) {
        super(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);

        mDialogDirectory = dialogDirectory;
        mDirectoryItem = item;
        mIGotoPageHandler = iGoto;
        mIEditPageHandler = iEdit;

        mGridView = onyxGridView;
        this.position = position;

        gotoButton = (ImageButton) layout.findViewById(R.id.goto_button);
        deleButton = (ImageButton) layout.findViewById(R.id.dele_button);
        exitButton = (ImageButton) layout.findViewById(R.id.exit_button);

        gotoButton.requestFocus();

        setClickListener();
    }

    private void setClickListener(){

        gotoButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mDialogDirectory.dismiss();
                mIGotoPageHandler.jumpBookmark(mDirectoryItem);
            }
        });
        gotoButton.setFocusableInTouchMode(true);
        gotoButton.setOnKeyListener(this);

        deleButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BookmarksPopupWindow.this.dismiss();
                mIEditPageHandler.deleteBookmark(mDirectoryItem);
                GridViewDirectoryAdapter adapter = ((GridViewDirectoryAdapter) (mGridView.getGridView().getAdapter()));
                adapter.remove(position);
            }
        });
        deleButton.setFocusableInTouchMode(true);
        deleButton.setOnKeyListener(this);

        exitButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BookmarksPopupWindow.this.dismiss();
            }
        });
        exitButton.setFocusableInTouchMode(true);
        exitButton.setOnKeyListener(this);

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (BookmarksPopupWindow.this.isShowing()) {
                BookmarksPopupWindow.this.dismiss();
                return true;
            }
        }
        return false;
    }

}
