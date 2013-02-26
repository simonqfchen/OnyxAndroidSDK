/**
 * 
 */
package com.onyx.android.sdk.ui.data;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onyx.android.sdk.R;

/**
 * @author dxwts
 *
 */
public class NumberButtonAdapter extends BaseAdapter
{

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    
    private String[] mButtonText = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "delete", "0", "OK"};
    
    private Context mContext;
    
    public NumberButtonAdapter(Context context)
    {
        mContext = context;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mButtonText.length;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public String getItemText(int position)
    {
        return mButtonText[position];
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(mButtonText[position] == "delete") {
            ImageView img = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.image_item, null);
            return img;
        }else {
            TextView textView = null;
            textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.button_item, null);
            textView.setText(mButtonText[position]);
            return textView;
        }
    }

}
