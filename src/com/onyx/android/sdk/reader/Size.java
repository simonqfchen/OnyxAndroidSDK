/**
 * 
 */
package com.onyx.android.sdk.reader;


/**
 * @author joy
 *
 */
public class Size
{
    private int mWidth = 0;
    private int mHeight = 0;
    
    public int getWidth()
    {
        return mWidth;
    }
    public int getHeight()
    {
        return mHeight;
    }
    
    public Size(int w, int h)
    {
        mWidth = w;
        mHeight = h;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Size)) {
            return false;
        }
        Size s = (Size) obj;
        return mWidth == s.mWidth && mHeight == s.mHeight;
    }
    @Override
    public int hashCode() {
        return mWidth * 32713 + mHeight;
    }
}
