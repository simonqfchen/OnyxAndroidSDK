/**
 *
 */
package com.onyx.android.sdk.ui.dialog;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.onyx.android.sdk.R;
import com.onyx.android.sdk.device.DeviceInfo;
import com.onyx.android.sdk.device.IDeviceFactory.TouchType;
import com.onyx.android.sdk.ui.DirectoryGridView;
import com.onyx.android.sdk.ui.data.DirectoryItem;
import com.onyx.android.sdk.ui.data.GridViewAnnotationAdapter;
import com.onyx.android.sdk.ui.data.GridViewDirectoryAdapter;
import com.onyx.android.sdk.ui.dialog.data.AnnotationItem;

/**
 * @author qingyue
 */
public class DialogDirectory extends DialogBaseOnyx
{
    public static enum DirectoryTab {toc, bookmark, annotation};
    private BookmarksPopupWindow mPopupWindow = null;

    public static interface IGotoPageHandler
    {
        public void jumpTOC( DirectoryItem item);
        public void jumpBookmark(DirectoryItem item);
        public void jumpAnnotation(DirectoryItem item);
    }

    public static interface IEditPageHandler {
        public void deleteBookmark(DirectoryItem item);
        public void deleteAnnotation(DirectoryItem item);
        public void editAnnotation(DirectoryItem item);
    }

    private IGotoPageHandler mGotoPageHandler = null;
    private IEditPageHandler mEditPageHandler = null;
    private TextView mTextViewTitle = null;
    private Context mContext = null;

    public DialogDirectory(Context context, ArrayList<DirectoryItem> tocItems,
            ArrayList<DirectoryItem> bookmarkItems, ArrayList<AnnotationItem> annotationItems,
            final IGotoPageHandler gotoPageHandler,final IEditPageHandler editPageHandler, DirectoryTab tab)
    {
        super(context, R.style.full_screen_dialog);
        setContentView(R.layout.dialog_directory);
        mContext = context;
        mGotoPageHandler = gotoPageHandler;
        mEditPageHandler = editPageHandler;
        TabHost tab_host = (TabHost) findViewById(R.id.tabhost);
        tab_host.setup();

        TextView toc = (TextView) LayoutInflater.from(context).inflate(R.layout.onyx_tabwidget, null);
        toc.setText(R.string.tabwidget_toc);
        TextView bookmark = (TextView) LayoutInflater.from(context).inflate(R.layout.onyx_tabwidget, null);
        bookmark.setText(R.string.tabwidget_bookmark);
        TextView annotation = (TextView) LayoutInflater.from(context).inflate(R.layout.onyx_tabwidget, null);
        annotation.setText(R.string.tabwidget_annotation);

        Resources resources = context.getResources();

        tab_host.addTab(tab_host.newTabSpec(resources.getString(R.string.tabwidget_toc)).setIndicator(toc).setContent(R.id.layout_toc));
        tab_host.addTab(tab_host.newTabSpec(resources.getString(R.string.tabwidget_bookmark)).setIndicator(bookmark).setContent(R.id.layout_bookmark));

        if (DeviceInfo.singleton().getDeviceController().getTouchType(context) != TouchType.None) {
        	tab_host.addTab(tab_host.newTabSpec(resources.getString(R.string.tabwidget_annotation)).setIndicator(annotation).setContent(R.id.layout_annotation));
		} else {
        	View v = this.findViewById(R.id.layout_annotation);
        	v.setVisibility(View.GONE);
        }

        tab_host.setOnTabChangedListener(new OnTabChangeListener()
        {

            @Override
            public void onTabChanged(String tabId)
            {
                mTextViewTitle.setText(tabId);
            }
        });

        mTextViewTitle = (TextView) findViewById(R.id.textview_title);

        DirectoryGridView gridViewTOC = (DirectoryGridView) findViewById(R.id.gridview_toc);
        final DirectoryGridView gridViewBookmark = (DirectoryGridView) findViewById(R.id.gridview_bookmark);
        final DirectoryGridView gridViewAnnotation = (DirectoryGridView) findViewById(R.id.gridview_annotation);

        if (tocItems != null) {
            GridViewDirectoryAdapter tocAdapter = new GridViewDirectoryAdapter(context, gridViewTOC.getGridView(), tocItems);
            gridViewTOC.getGridView().setAdapter(tocAdapter);
        }
        if (bookmarkItems != null) {
            GridViewDirectoryAdapter bookmarkAdapter = new GridViewDirectoryAdapter(context, gridViewBookmark.getGridView(), bookmarkItems);
            gridViewBookmark.getGridView().setAdapter(bookmarkAdapter);
        }
        if (annotationItems != null) {
            GridViewAnnotationAdapter annotationAdapter = new GridViewAnnotationAdapter(context, gridViewAnnotation.getGridView(), annotationItems);
            gridViewAnnotation.getGridView().setAdapter(annotationAdapter);
        }

        gridViewTOC.getGridView().setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                DialogDirectory.this.dismiss();
                DirectoryItem item = (DirectoryItem) view.getTag();
                mGotoPageHandler.jumpTOC(item);
            }
        });

        gridViewBookmark.getGridView().setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                DirectoryItem item = (DirectoryItem) view.getTag();
                LinearLayout layout = initPopupWindow();
                if (mPopupWindow == null) {
                    mPopupWindow = new BookmarksPopupWindow(mContext, layout, DialogDirectory.this,
                            mGotoPageHandler, mEditPageHandler);
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.setTouchable(true);
                }
                if (mPopupWindow.getMode() != 0) {
                    mPopupWindow.removeButton(1, 1);
                }

                mPopupWindow.setDirectoryItem(item, gridViewBookmark, position);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                int width = params.width - mPopupWindow.getWidth();
                mPopupWindow.showAsDropDown(view, width, 0);
            }
        });

        gridViewAnnotation.getGridView().setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                DirectoryItem item = (DirectoryItem) view.getTag();
                LinearLayout layout = initPopupWindow();
                //mGotoPageHandler.jumpAnnotation(item);
                if (mPopupWindow == null) {
                    mPopupWindow = new BookmarksPopupWindow(mContext, layout, DialogDirectory.this,
                            mGotoPageHandler, mEditPageHandler);
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.setTouchable(true);
                }
                if (mPopupWindow.getMode() != 1) {
                    mPopupWindow.addButton(1, 1);
                }

                mPopupWindow.setDirectoryItem(item, gridViewAnnotation, position);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                int width = params.width - mPopupWindow.getWidth();
                mPopupWindow.showAsDropDown(view, width, 0);

            }
        });

        Button button_exit = (Button) findViewById(R.id.button_exit);
        button_exit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogDirectory.this.dismiss();
            }
        });

        switch (tab) {
        case toc:
            tab_host.setCurrentTab(0);
            mTextViewTitle.setText(R.string.tabwidget_toc);
            break;
        case bookmark:
            tab_host.setCurrentTab(1);
            mTextViewTitle.setText(R.string.tabwidget_bookmark);
            break;
        case annotation:
            tab_host.setCurrentTab(2);
            mTextViewTitle.setText(R.string.tabwidget_annotation);
            break;
        default:
            tab_host.setCurrentTab(0);
            mTextViewTitle.setText(R.string.tabwidget_toc);
            break;
        }
    }

    private LinearLayout initPopupWindow(){
        LinearLayout layout = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 5, 0);

        ImageButton gotoButton = new ImageButton(mContext);
        gotoButton.setId(0);
        gotoButton.setLayoutParams(params);
        gotoButton.setImageResource(R.drawable.toc_menu_go);
        gotoButton.setBackgroundResource(R.drawable.gridview_selector);
        layout.addView(gotoButton, 0);

        ImageButton editButton = new ImageButton(mContext);
        editButton.setLayoutParams(params);
        editButton.setId(1);
        editButton.setImageResource(R.drawable.toc_menu_edit);
        editButton.setBackgroundResource(R.drawable.gridview_selector);
        layout.addView(editButton, 1);

        ImageButton deleButton = new ImageButton(mContext);
        deleButton.setLayoutParams(params);
        deleButton.setId(2);
        deleButton.setImageResource(R.drawable.toc_menu_dele);
        deleButton.setBackgroundResource(R.drawable.gridview_selector);
        layout.addView(deleButton, 2);

        ImageButton exitButton = new ImageButton(mContext);
        exitButton.setId(3);
        exitButton.setLayoutParams(params);
        exitButton.setImageResource(R.drawable.toc_menu_exit);
        exitButton.setBackgroundResource(R.drawable.gridview_selector);
        layout.addView(exitButton, 3);

        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(params);
        return layout;
    }

}
