/**
 * 
 */
package com.onyx.android.sdk.reader;

import java.util.List;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author joy
 *
 */
public class DocTextSelection implements Parcelable
{
    private final static String TAG = "DocTextSelection";

    private List<Rect> mBoxes = null;
    private String mText = null;
    private String mSelectionBegin = null;
    private String mSelectionEnd = null;

    public DocTextSelection(List<Rect> boxes, String text, String selectionBegin, String selectionEnd)
    {
        mBoxes = boxes;
        mText = text;
        mSelectionBegin = selectionBegin;
        mSelectionEnd = selectionEnd;
        Log.d(TAG, "selection text: " + text);
    }
    public DocTextSelection(Parcel p)
    {
        p.readList(mBoxes, null);
        mText = p.readString();
        mSelectionBegin = p.readString();
        mSelectionEnd = p.readString();
    }

    public List<Rect> getBoxes()
    {
        return mBoxes;
    }

    public String getText()
    {
        return mText;
    }

    public String getSelectionBegin()
    {
        return mSelectionBegin;
    }

    public String getSelectionEnd()
    {
        return mSelectionEnd;
    }

    public static final Parcelable.Creator<DocTextSelection> CREATOR = new Parcelable.Creator<DocTextSelection>() {

        @Override
        public DocTextSelection createFromParcel(Parcel source)
        {
            return new DocTextSelection(source);
        }

        @Override
        public DocTextSelection[] newArray(int size)
        {
            return new DocTextSelection[size];
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
        dest.writeList(mBoxes);
        dest.writeString(mText);
        dest.writeString(mSelectionBegin);
        dest.writeString(mSelectionEnd);
    }
}
