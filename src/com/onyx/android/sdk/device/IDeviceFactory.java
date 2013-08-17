/**
 * 
 */
package com.onyx.android.sdk.device;

import java.io.File;

import android.content.Context;
import android.view.View;

/**
 * @author joy
 *
 */
public interface IDeviceFactory
{
    public enum TouchType { None, IR, Capacitive, Unknown }
    
    public static interface IDeviceController
    {
        File getExternalStorageDirectory();
        File getRemovableSDCardDirectory();
        boolean isFileOnRemovableSDCard(File file);
        
        TouchType getTouchType(Context context);
        boolean hasWifi(Context context);
        boolean hasAudio(Context context);
        boolean hasFrontLight(Context context);
        
        boolean isEInkScreen();
        
        EpdController.EPDMode getEpdMode();
        boolean setEpdMode(Context context, EpdController.EPDMode mode);
        
        EpdController.UpdateMode getViewDefaultUpdateMode(View view);
        boolean setViewDefaultUpdateMode(View view, EpdController.UpdateMode mode);

        EpdController.UpdateMode getSystemDefaultUpdateMode();
        boolean setSystemDefaultUpdateMode(EpdController.UpdateMode mode);
        
        void invalidate(View view, EpdController.UpdateMode mode);
        void postInvalidate(View view, EpdController.UpdateMode mode);
    }
    
    String name();
    boolean isPresent();
    
    IDeviceController createController();
}
