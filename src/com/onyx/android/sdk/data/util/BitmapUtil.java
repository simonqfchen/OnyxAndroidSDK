/**
 * 
 */
package com.onyx.android.sdk.data.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.util.Log;

/**
 * @author joy
 *
 */
public class BitmapUtil
{
    private static final String TAG = "BitmapUtil";
    private static final boolean VERBOSE_PROFILE = false;
    
    public static byte[] getByteArray(Bitmap bmp)
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            if (VERBOSE_PROFILE) ProfileUtil.start(TAG, "compress bitmap");
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
            if (VERBOSE_PROFILE) ProfileUtil.end(TAG, "compress bitmap");
        }
    }
    
    /**
     * never return null
     * 
     * @param bmp
     * @return
     */
    public static int[] getPixels(Bitmap bmp)
    {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();

        int buf[] = new int[width * height];
        bmp.getPixels(buf, 0, width, 0, 0, width, height);  
        
        return buf;
    }
    
    /**
     * never return null
     * 
     * @param bmp
     * @return
     */
    public static byte[] getPixelsInByte(Bitmap bmp)
    {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        ByteBuffer byte_buf = ByteBuffer.allocate(width * height * 4);
        bmp.copyPixelsToBuffer(byte_buf);
        
        return byte_buf.array();
    }
    
    public static ParcelFileDescriptor createMemoryFile(Bitmap bmp)
    {
        MemoryFile mf = null;
        try {
            byte[] pixels = getByteArray(bmp);

            mf = new MemoryFile(null, pixels.length);
            mf.allowPurging(false);

            if (VERBOSE_PROFILE) ProfileUtil.start(TAG, "write MemoryFile");
            OutputStream os = null;
            try {
                os = mf.getOutputStream();
                os.write(pixels);
            }
            finally {
                if (VERBOSE_PROFILE) ProfileUtil.end(TAG, "write MemoryFile");
                if (os != null) {
                    os.close();
                }
            }
        } catch (IOException e) {
            Log.w(TAG, e);
            if (mf != null) {
                mf.close();
            }
            return null;
        }
        
        return MemoryFileUtil.getParcelFileDescriptor(mf);
    }
    
    public static Bitmap readMemoryFile(ParcelFileDescriptor pfd)
    {
        if (VERBOSE_PROFILE) ProfileUtil.start(TAG, "readMemoryFile");
        try {
            return BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
        }
        finally {
            if (VERBOSE_PROFILE) ProfileUtil.end(TAG, "readMemoryFile");
        }
    }
}
