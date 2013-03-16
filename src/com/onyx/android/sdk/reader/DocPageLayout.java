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
public enum DocPageLayout implements Parcelable
{
    FixedPage, // hard paged document, like PDF, DJVU
    Reflow,;  // reflowable document, like EPUB, TXT

    public static final Parcelable.Creator<DocPageLayout> CREATOR = new Parcelable.Creator<DocPageLayout>() {

        @Override
        public DocPageLayout createFromParcel(Parcel source)
        {
            return DocPageLayout.values()[source.readInt()];
        }

        @Override
        public DocPageLayout[] newArray(int size)
        {
            return new DocPageLayout[size];
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
        dest.writeInt(ordinal());
    }
}
