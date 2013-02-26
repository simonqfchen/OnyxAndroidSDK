package com.onyx.android.sdk.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.onyx.android.sdk.R;
import com.onyx.android.sdk.ui.data.SelectionAdapter;

public class DialogPageMargins extends DialogBaseSettings
{
    public static final String[] sPageMarginsArray = new String[]{"0", "10", "20", "30"};

    public interface onPageMarginsListener
    {
        public void onPageMargins(int pageMargins);
    }
    private onPageMarginsListener mOnPageMarginsListener = new onPageMarginsListener()
    {

        @Override
        public void onPageMargins(int pageMargins)
        {
            //do nothing
        }
    };

    public void setOnPageMargingsListener(onPageMarginsListener l)
    {
        mOnPageMarginsListener = l;
    }

    private SelectionAdapter mAdapter = null;
    private int mSelectItem = -1;

    public DialogPageMargins(Context context, int margins)
    {
        super(context);

        mAdapter = new SelectionAdapter(context, getGridView(), sPageMarginsArray, margins);
        for (int i = 0; i < sPageMarginsArray.length; i++) {
            if (String.valueOf(margins).equals(sPageMarginsArray[i])) {
                mAdapter.setSelection(i);
                break;
            }
        }
        getGridView().setAdapter(mAdapter);

        this.getTextViewTitle().setText(R.string.page_margins);

        mAdapter.getPaginator().setPageSize(sPageMarginsArray.length);
        setIsCanChooseZeroItem(false);

        this.getGridView().setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mAdapter.setSelection(position);
                mAdapter.notifyDataSetChanged();
                mSelectItem = Integer.parseInt((String) view.getTag());
            }
        });

        this.getButtonSet().setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mOnPageMarginsListener.onPageMargins(mSelectItem);
                DialogPageMargins.this.dismiss();
            }
        });

        this.getButtonCancel().setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogPageMargins.this.dismiss();
            }
        });
    }
}
