package com.onyx.android.sdk.ui.dialog;

import android.content.Context;
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
    private static final int GOTOBUTTON_ID = 0;
    private static final int EDITBUTTON_ID = 1;
    private static final int DELEBUTTON_ID = 2;
    private static final int EXITBUTTON_ID = 3;

    private static final int BOOKMARK_MODE = 0;
    private static final int ANNOTATION_MODE = 1;

    private int mMode = -1;

    private DialogDirectory mDialogDirectory = null;
    private DirectoryItem mDirectoryItem = null;
    private IGotoPageHandler mIGotoPageHandler = null;
    private IEditPageHandler mIEditPageHandler = null;

    private DirectoryGridView mGridView = null;
    private int mPosition = -1;

    private ImageButton mGotoButton = null;
    private ImageButton mEditButton = null;
    private ImageButton mDeleButton = null;
    private ImageButton mExitButton = null;

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

        mGotoButton = (ImageButton) layout.findViewById(GOTOBUTTON_ID);
        mEditButton = (ImageButton) layout.findViewById(EDITBUTTON_ID);
        mDeleButton = (ImageButton) layout.findViewById(DELEBUTTON_ID);
        mExitButton = (ImageButton) layout.findViewById(EXITBUTTON_ID);

        mMode = ANNOTATION_MODE;

        setClickListener();
    }

    public void setDirectoryItem(DirectoryItem item, DirectoryGridView onyxGridView, int position) {
        mDirectoryItem = item;
        mGridView = onyxGridView;
        this.mPosition = position;

        mGotoButton.requestFocus();

    }

    public boolean addButton(int id, int position) {
        ImageButton imageButton = choiceButton(id);
        if (imageButton != null) {
            mLayout.addView(imageButton, position);
            mMode = ANNOTATION_MODE;
            return true;
        }
        return false;
    }

    public boolean removeButton(int id, int position) {
        ImageButton imageButton = choiceButton(id);
        if (imageButton != null) {
            mLayout.removeViewInLayout(imageButton);
            mMode = BOOKMARK_MODE;
            return true;
        }
        return false;
    }

    private ImageButton choiceButton(int id) {
        ImageButton imageButton = null;
        switch (id) {
        case GOTOBUTTON_ID:
            imageButton = mGotoButton;
            break;
        case EDITBUTTON_ID:
            imageButton = mEditButton;
            break;
        case DELEBUTTON_ID:
            imageButton = mDeleButton;
            break;
        case EXITBUTTON_ID:
            imageButton = mExitButton;
            break;
        }
        return imageButton;
    }

    private void setClickListener(){

        mGotoButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mDialogDirectory.dismiss();
                switch (mMode) {
                case BOOKMARK_MODE:
                    mIGotoPageHandler.jumpBookmark(mDirectoryItem);
                    break;
                case ANNOTATION_MODE:
                    mIGotoPageHandler.jumpAnnotation(mDirectoryItem);
                    break;
                }
            }
        });
        mGotoButton.setFocusableInTouchMode(true);
        mGotoButton.setOnKeyListener(this);

        mEditButton.setOnClickListener(new OnClickListener()
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
                        annotationAdapter.update(annotation, mPosition);
                        mIEditPageHandler.editAnnotation(annotation);
                    }
                });
                dialogAnnotation.show();
            }
        });
        mEditButton.setFocusableInTouchMode(true);
        mEditButton.setOnKeyListener(this);

        mDeleButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BookmarksPopupWindow.this.dismiss();
                switch (mMode) {
                case BOOKMARK_MODE:
                    mIEditPageHandler.deleteBookmark(mDirectoryItem);
                    GridViewDirectoryAdapter bookMarkAdapter = (GridViewDirectoryAdapter) (mGridView.getGridView().getAdapter());
                    bookMarkAdapter.remove(mPosition);
                    break;
                case ANNOTATION_MODE:
                    mIEditPageHandler.deleteAnnotation(mDirectoryItem);
                    GridViewAnnotationAdapter annotationAdapter = (GridViewAnnotationAdapter) (mGridView.getGridView().getAdapter());
                    annotationAdapter.remove(mPosition);
                    break;
                }

            }
        });
        mDeleButton.setFocusableInTouchMode(true);
        mDeleButton.setOnKeyListener(this);

        mExitButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BookmarksPopupWindow.this.dismiss();
            }
        });
        mExitButton.setFocusableInTouchMode(true);
        mExitButton.setOnKeyListener(this);

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
        return mMode;
    }

}
