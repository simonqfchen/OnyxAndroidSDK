/**
 * 
 */
package com.onyx.android.sdk.ui.data;

/**
 * @author joy
 *
 */
public class TOCItem
{
    private String mTitle = null;
    private String mPage = null;
    private Object mTag = null;
    
    public TOCItem(String title, int page, Object tag)
    {
        mTitle = title;
        mPage = String.valueOf(page);
        mTag = tag;
    }
    public TOCItem(String title, String page, Object tag)
    {
        mTitle = title;
        mPage = page;
        mTag = tag;
    }
    
    public String getTitle()
    {
        return mTitle;
    }
    
    public String getPage()
    {
        return mPage;
    }
    
    public Object getTag()
    {
        return mTag;
    }
}
