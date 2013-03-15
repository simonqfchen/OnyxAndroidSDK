/**
 * 
 */
package com.onyx.android.sdk.reader;

import java.util.ArrayList;

import android.graphics.Rect;
import android.util.Log;

/**
 * @author joy
 *
 */
public class DocTextSelection
{
    private final static String TAG = "DocTextSelection";
    
    private ArrayList<Rect> mBoxes = null;
    private String mText = null;
    private String mSelectionBegin = null;
    private String mSelectionEnd = null;
    
    public DocTextSelection(ArrayList<Rect> boxes, String text, String selectionBegin, String selectionEnd)
    {
        mBoxes = boxes;
        mText = text;
        mSelectionBegin = selectionBegin;
        mSelectionEnd = selectionEnd;
        Log.d(TAG, "selection text: " + text);
    }
    
    public ArrayList<Rect> getBoxes()
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
}
