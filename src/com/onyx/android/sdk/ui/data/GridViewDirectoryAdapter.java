/**
 *
 */
package com.onyx.android.sdk.ui.data;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onyx.android.sdk.R;
import com.onyx.android.sdk.ui.OnyxGridView;
import com.onyx.android.sdk.ui.data.GridViewPageLayout.GridViewMode;

/**
 * @author qingyue
 *
 */
public class GridViewDirectoryAdapter extends OnyxPagedAdapter
{
    private LayoutInflater mInflater = null;
    private final ArrayList<DirectoryItem> mDirectoryItems = new ArrayList<DirectoryItem>();

    private static final int sItemMinWidth = 145;
    private static final int sItemMinHeight = 60;
    private static final int sHorizontalSpacing = 0;
    private static final int sVerticalSpacing = 0;
    private static final int sItemDetailMinHeight = 60;

    public GridViewDirectoryAdapter(Context context, OnyxGridView gridView, ArrayList<DirectoryItem> DirectoryItems)
    {
        super(gridView);

        mInflater = LayoutInflater.from(context);
        mDirectoryItems.addAll(DirectoryItems);

        this.getPageLayout().setItemMinWidth(sItemMinWidth);
        this.getPageLayout().setItemMinHeight(sItemMinHeight);
        this.getPageLayout().setItemThumbnailMinHeight(sItemMinHeight);
        this.getPageLayout().setItemDetailMinHeight(sItemDetailMinHeight);
        this.getPageLayout().setHorizontalSpacing(sHorizontalSpacing);
        this.getPageLayout().setVerticalSpacing(sVerticalSpacing);
        this.getPageLayout().setViewMode(GridViewMode.Detail);

        this.getPaginator().initializePageData(mDirectoryItems.size(), this.getPaginator().getPageSize());
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void remove(int position) {
        mDirectoryItems.remove(position);
        super.setItemCount(mDirectoryItems.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View ret_view = null;
        if (convertView != null) {
            ret_view = convertView;
        } else {
            ret_view = mInflater.inflate(R.layout.gridview_directory_item, null);
        }

        int idx = this.getPaginator().getAbsoluteIndex(position);
        DirectoryItem directory_item = mDirectoryItems.get(idx);

        TextView title = (TextView) ret_view.findViewById(R.id.textview_title);
        TextView page = (TextView) ret_view.findViewById(R.id.textview_page);

        if(directory_item.getTitle() != null) {
            title.setText(directory_item.getTitle());
        } else {
            title.setText("");
        }
        page.setText(directory_item.getPage());

        ret_view.setTag(directory_item);

        OnyxGridView.LayoutParams ret_view_params = new OnyxGridView.LayoutParams(this.getPageLayout().getItemCurrentWidth(),
                this.getPageLayout().getItemCurrentHeight());
        ret_view.setLayoutParams(ret_view_params);

        return ret_view;
    }
}
