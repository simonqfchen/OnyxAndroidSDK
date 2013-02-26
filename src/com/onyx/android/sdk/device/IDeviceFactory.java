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
    public enum TouchType { None, IR, Capacitive }
    
    public static interface IDeviceController
    {
        File getExternalStorageDirectory();
        File getRemovableSDCardDirectory();
        boolean isFileOnRemovableSDCard(File file);
        
        boolean hasWifi();
        TouchType getTouchType();
        
        boolean isEInkScreen();
        
        EpdController.EPDMode getEpdMode();
        boolean setEpdMode(Context context, EpdController.EPDMode mode);
        
        void invalidate(View view, EpdController.UpdateMode mode);
        void postInvalidate(View view, EpdController.UpdateMode mode);
    }
    
    String name();
    boolean isPresent();
    
    IDeviceController createController();
}
