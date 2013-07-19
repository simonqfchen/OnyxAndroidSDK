/**
 * 
 */
package com.onyx.android.sdk.reader;

/**
 * @author joy
 *
 */
public class DocLinkInfo
{
    private boolean mIsInternalLink = true;
    private String mTarget = null;
    private DocTextSelection mSelection = null;
    
    public DocLinkInfo(boolean isInternal, String target, DocTextSelection selection)
    {
        mIsInternalLink = isInternal;
        mTarget = target;
        mSelection = selection;
    }
    
    public boolean isInternalLink()
    {
        return mIsInternalLink;
    }
    public String getTarget()
    {
        return mTarget;
    }
    public DocTextSelection getSelection()
    {
        return mSelection;
    }
}
