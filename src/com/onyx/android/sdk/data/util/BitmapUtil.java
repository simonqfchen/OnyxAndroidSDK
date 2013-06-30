/**
 * 
 */
package com.onyx.android.sdk.data.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author joy
 *
 */
public class BitmapUtil
{
    private static final String TAG = "BitmapUtil";
    
    public static byte[] getByteArrayFromBitmap(Bitmap bmp)
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
            return os.toByteArray();
        }
        finally {
            try {
                os.close();
            }
            catch (IOException e) {
                Log.e(TAG, "exception", e);
            }
        }
    }
}
