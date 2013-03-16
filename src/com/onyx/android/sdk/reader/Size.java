/**
 * 
 */
package com.onyx.android.sdk.reader;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author joy
 *
 */
public class Size implements Parcelable
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
    
    public Size(Parcel p)
    {
        mWidth = p.readInt();
        mHeight = p.readInt();
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
    
    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>()
    {

        @Override
        public Size createFromParcel(Parcel source)
        {
            return new Size(source);
        }

        @Override
        public Size[] newArray(int size)
        {
            return new Size[size];
        }
    };
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
    }
}
