package com.onyx.android.sdk.ui.dialog;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.onyx.android.sdk.ui.DirectoryGridView;
import com.onyx.android.sdk.ui.data.DirectoryItem;
import com.onyx.android.sdk.ui.data.GridViewAnnotationAdapter;
import com.onyx.android.sdk.ui.data.GridViewDirectoryAdapter;
import com.onyx.android.sdk.ui.dialog.DialogAnnotation.AnnotationAction;
import com.onyx.android.sdk.ui.dialog.DialogAnnotation.onUpdateAnnotationListener;
import com.onyx.android.sdk.ui.dialog.DialogDirectory.IEditPageHandler;
import com.onyx.android.sdk.ui.dialog.DialogDirectory.IGotoPageHandler;
import com.onyx.android.sdk.ui.dialog.data.AnnotationItem;

/**
 * @author cap
 *
 */
public class BookmarksPopupWindow extends PopupWindow implements View.OnKeyListener
{
    private final static String TAG = "BookMarkPopupWindow";

    private final int GOTOBUTTON_ID = 0;
    private final int EDITBUTTON_ID = 1;
    private final int DELEBUTTON_ID = 2;
    private final int EXITBUTTON_ID = 3;

    /*
     *
     */
    private final int BOOKMARK_MODE = 0;
    private final int ANNOTATION_MODE = 1;

    private int mode = -1;

    private DialogDirectory mDialogDirectory = null;
    private DirectoryItem mDirectoryItem = null;
    private IGotoPageHandler mIGotoPageHandler = null;
    private IEditPageHandler mIEditPageHandler = null;

    private DirectoryGridView mGridView = null;
    private int position = -1;

    private ImageButton gotoButton = null;
    private ImageButton editButton = null;
    private ImageButton deleButton = null;
    private ImageButton exitButton = null;

    private final LinearLayout mLayout;
    private final Context mContext;

    public BookmarksPopupWindow (Context context, LinearLayout layout, DialogDirectory dialogDirectory,
            IGotoPageHandler iGoto, IEditPageHandler iEdit) {
        super(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);

        mContext = context;
        mLayout = layout;

        mDialogDirectory = dialogDirectory;
        mIGotoPageHandler = iGoto;
        mIEditPageHandler = iEdit;

        gotoButton = (ImageButton) layout.findViewById(GOTOBUTTON_ID);
        editButton = (ImageButton) layout.findViewById(EDITBUTTON_ID);
        deleButton = (ImageButton) layout.findViewById(DELEBUTTON_ID);
        exitButton = (ImageButton) layout.findViewById(EXITBUTTON_ID);

        mode = ANNOTATION_MODE;

        setClickListener();
    }

    public void setDirectoryItem(DirectoryItem item, DirectoryGridView onyxGridView, int position) {
        mDirectoryItem = item;
        mGridView = onyxGridView;
        this.position = position;

        gotoButton.requestFocus();

    }

    public boolean addButton(int id, int position) {
        ImageButton imageButton = choiceButton(id);
        if (imageButton != null) {
            mLayout.addView(imageButton, position);
            mode = ANNOTATION_MODE;
            return true;
        }
        return false;
    }

    public boolean removeButton(int id, int position) {
        ImageButton imageButton = choiceButton(id);
        if (imageButton != null) {
            mLayout.removeViewInLayout(imageButton);
            mode = BOOKMARK_MODE;
            return true;
        }
        return false;
    }

    private ImageButton choiceButton(int id) {
        ImageButton imageButton = null;
        switch (id) {
        case GOTOBUTTON_ID:
            imageButton = gotoButton;
            break;
        case EDITBUTTON_ID:
            imageButton = editButton;
            break;
        case DELEBUTTON_ID:
            imageButton = deleButton;
            break;
        case EXITBUTTON_ID:
            imageButton = exitButton;
            break;
        }
        return imageButton;
    }

    private void setClickListener(){

        gotoButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mDialogDirectory.dismiss();
                switch (mode) {
                case BOOKMARK_MODE:
                    mIGotoPageHandler.jumpBookmark(mDirectoryItem);
                    break;
                case ANNOTATION_MODE:
                    mIGotoPageHandler.jumpAnnotation(mDirectoryItem);
                    break;
                }
            }
        });
        gotoButton.setFocusableInTouchMode(true);
        gotoButton.setOnKeyListener(this);

        editButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BookmarksPopupWindow.this.dismiss();
                String note = mDirectoryItem.getTitle();
                DialogAnnotation dialogAnnotation = new DialogAnnotation(mContext, AnnotationAction.onlyUpdate, note);
                dialogAnnotation.setOnUpdateAnnotationListener(new onUpdateAnnotationListener()
                {

                    @Override
                    public void updateAnnotation(String note)
                    {
                        GridViewAnnotationAdapter annotationAdapter = (GridViewAnnotationAdapter) (mGridView.getGridView().getAdapter());
                        AnnotationItem annotation = new AnnotationItem(note, mDirectoryItem.getPage(), mDirectoryItem.getTag(), null);
                        annotationAdapter.update(annotation, position);
                        mIEditPageHandler.editAnnotation(annotation);
                    }
                });
                dialogAnnotation.show();
            }
        });
        editButton.setFocusableInTouchMode(true);
        editButton.setOnKeyListener(this);

        deleButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BookmarksPopupWindow.this.dismiss();
                switch (mode) {
                case BOOKMARK_MODE:
                    mIEditPageHandler.deleteBookmark(mDirectoryItem);
                    GridViewDirectoryAdapter bookMarkAdapter = (GridViewDirectoryAdapter) (mGridView.getGridView().getAdapter());
                    bookMarkAdapter.remove(position);
                    break;
                case ANNOTATION_MODE:
                    mIEditPageHandler.deleteAnnotation(mDirectoryItem);
                    GridViewAnnotationAdapter annotationAdapter = (GridViewAnnotationAdapter) (mGridView.getGridView().getAdapter());
                    annotationAdapter.remove(position);
                    break;
                }

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

    public int getMode() {
        return mode;
    }

}
