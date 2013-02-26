/**
 * 
 */
package com.onyx.android.sdk.device;

import android.content.Context;
import android.view.Surface;
import android.view.View;

/**
 * singleton class
 * 
 * @author joy
 *
 */
public abstract class EpdController
{
    @SuppressWarnings("unused")
    private static String TAG = "EpdController";
    
    public static enum EPDMode { FULL, AUTO, TEXT, AUTO_PART, AUTO_BLACK_WHITE, AUTO_A2 }
    public enum UpdatePolicy { Automatic, GUIntervally }
    public enum UpdateMode { None, DW, GU, GU_FAST, GC, } 
    
    private EpdController()
    {
    }
    
    public static int getWindowRotation(Context context)
    {
        return Surface.ROTATION_0;
    }
    
    public static boolean setWindowRotation(Context context, int rotation)
    {
        return false;
    }
    
    public static boolean setUpdatePolicy(View view, UpdatePolicy policy, int guInterval)
    {
        return false;
    }
    
    public static void invalidate(View view, UpdateMode mode)
    {
        DeviceInfo.singleton().getDeviceController().invalidate(view, mode);
    }
    
    /**
     * be careful when UpdateMode is GC because postInvalidate() does not take effect immediately
     *  
     * @param view
     * @param mode
     */
    public static void postInvalidate(View view, UpdateMode mode)
    {
        DeviceInfo.singleton().getDeviceController().postInvalidate(view, mode);
    }
    
    public static EPDMode getMode()
    {
        return DeviceInfo.singleton().getDeviceController().getEpdMode();
    }
    
    public static boolean setMode(Context context, EPDMode mode)
    {
        return DeviceInfo.singleton().getDeviceController().setEpdMode(context, mode);
    }
    
}
