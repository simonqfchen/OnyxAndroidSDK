/**
 * 
 */
package com.onyx.android.sdk.device;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.onyx.android.sdk.device.EpdController.EPDMode;
import com.onyx.android.sdk.device.EpdController.UpdateMode;

/**
 * @author joy
 *
 */
public class RK2906Factory implements IDeviceFactory
{
    private final static String TAG = "RK2906Factory";
    
    private static class RK2906Controller implements IDeviceController 
    {
        private final static int DEFAULT_VIEW_MODE = 0;
        private final static String DEFAULT_EPD_MODE = "3"; // which standing for A2 according to RK2906 specification
        
        private static RK2906Controller sInstance = null;
        
        private static Method sMethodViewRequestEpdMode = null;
        private static int sViewNull = DEFAULT_VIEW_MODE;
        private static int sViewFull = DEFAULT_VIEW_MODE;
        private static int sViewA2 = DEFAULT_VIEW_MODE;
        private static int sViewAuto = DEFAULT_VIEW_MODE;
        private static int sViewPart = DEFAULT_VIEW_MODE;
        
        @SuppressWarnings("rawtypes")
        private static Constructor sEpdManagerConstructor = null;
        
        private static Method sMethodEpdSetMode = null;
        private static String sEpdModeFull = DEFAULT_EPD_MODE;
        private static String sEpdModeA2 = DEFAULT_EPD_MODE;
        private static String sEpdModePart = DEFAULT_EPD_MODE;
        
        private Context mContext = null;
        private Object mEpdManagerInstance = null;
        
        private EPDMode mCurrentMode = EPDMode.AUTO;
        
        @SuppressWarnings("rawtypes")
        private static Constructor sDeviceControllerConstructor = null;
        private static Method sMethodIsTouchable;
        @SuppressWarnings("unused")
        private static Method sMethodGetTouchType;
        private static Method sMethodHasWifi;
        private static Method sMethodHasAudio;
        private static Method sMethodHasFrontLight;
        
        @SuppressWarnings("unused")
        private static int sTouchTypeUnknown = 0;
        @SuppressWarnings("unused")
        private static int sTouchTypeIR = 0;
        @SuppressWarnings("unused")
        private static int sTouchTypeCapacitive = 0;
        
        private RK2906Controller()
        {
        }
        
        @SuppressWarnings("unchecked")
        public static RK2906Controller createController()
        {
            if (sInstance == null) {
                try {
                    Class<View> class_view = View.class;
                    sMethodViewRequestEpdMode = class_view.getMethod("requestEpdMode", int.class);
                    sViewNull = class_view.getField("EPD_NULL").getInt(null);
                    sViewFull = class_view.getField("EPD_FULL").getInt(null);
                    sViewA2 = class_view.getField("EPD_A2").getInt(null);
                    sViewAuto = class_view.getField("EPD_AUTO").getInt(null);
                    sViewPart = class_view.getField("EPD_PART").getInt(null);
                    
                    @SuppressWarnings("rawtypes")
                    Class class_epd_manager = Class.forName("android.hardware.EpdManager");
                    sEpdManagerConstructor = class_epd_manager.getConstructor(Context.class);
                    sMethodEpdSetMode = class_epd_manager.getMethod("setMode", String.class);
                    sEpdModeFull = (String)class_epd_manager.getField("FULL").get(null);
                    sEpdModeA2 = (String)class_epd_manager.getField("A2").get(null);
                    sEpdModePart = (String)class_epd_manager.getField("PART").get(null);
                    
                    @SuppressWarnings("rawtypes")
                    Class class_device_controller = Class.forName("android.hardware.DeviceController");
                    sDeviceControllerConstructor = class_device_controller.getConstructor(Context.class);
                    sMethodIsTouchable = class_device_controller.getMethod("isTouchable");
                    sMethodGetTouchType = class_device_controller.getMethod("getTouchType");
                    sMethodHasWifi = class_device_controller.getMethod("hasWifi");
                    sMethodHasAudio = class_device_controller.getMethod("hasAudio");
                    sMethodHasFrontLight = class_device_controller.getMethod("hasFrontLight");
                    
//                    sTouchTypeUnknown = class_device_controller.getField("TOUCH_TYPE_UNKNOWN").getInt(null);
//                    sTouchTypeIR = class_device_controller.getField("TOUCH_TYPE_IR").getInt(null);
//                    sTouchTypeCapacitive = class_device_controller.getField("TOUCH_TYPE_CAPACITIVE").getInt(null);
                    
                    sInstance = new RK2906Controller();
                    return sInstance;
                }
                catch (ClassNotFoundException e) {
                    Log.w(TAG, e);
                }
                catch (SecurityException e) {
                    Log.w(TAG, e);
                }
                catch (NoSuchMethodException e) {
                    Log.w(TAG, e);
                }
                catch (IllegalArgumentException e) {
                    Log.w(TAG, e);
                }
                catch (IllegalAccessException e) {
                    Log.w(TAG, e);
                }
                catch (NoSuchFieldException e) {
                    Log.w(TAG, e);
                }
                
                return null;
            }
            
            return sInstance;
        }
        
        @Override
        public File getExternalStorageDirectory()
        {
            return new File("/mnt/storage");
        }

        @Override
        public File getRemovableSDCardDirectory()
        {
            File storage_root = this.getExternalStorageDirectory();

            // if system has an emulated SD card(/mnt/sdcard) provided by device's NAND flash, 
            // then real SD card will be mounted as a child directory(/mnt/sdcard/extsd) in it, which names "extsd" here
            final String SDCARD_MOUNTED_FOLDER = "sdcard";
            File extsd = new File(storage_root, SDCARD_MOUNTED_FOLDER);
            return extsd;
        }

        @Override
        public boolean isFileOnRemovableSDCard(File file)
        {
            return file.getAbsolutePath().startsWith(getRemovableSDCardDirectory().getAbsolutePath());
        }
        
        @Override
        public TouchType getTouchType(Context context)
        {
            try {
                Object instance = sDeviceControllerConstructor.newInstance(context);
                Boolean succ = (Boolean) sMethodIsTouchable.invoke(instance);
                if (succ == null || !succ.booleanValue()) {
                    return TouchType.None;
                }
                
                return TouchType.IR;
                
//                Integer n = (Integer)sMethodGetTouchType.invoke(instance);
//                if (n.intValue() == sTouchTypeUnknown) {
//                    return TouchType.Unknown;
//                }
//                else if (n.intValue() == sTouchTypeIR) {
//                    return TouchType.IR;
//                }
//                else if (n.intValue() == sTouchTypeCapacitive) {
//                    return TouchType.Capacitive;
//                }
//                else {
//                    assert(false);
//                    return TouchType.Unknown;
//                }
            }
            catch (IllegalArgumentException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InstantiationException e) {
                Log.e(TAG, "exception", e);
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InvocationTargetException e) {
                Log.e(TAG, "exception", e);
            }
            
            return TouchType.None;
        }
        
        @Override
        public boolean hasWifi(Context context)
        {
            try {
                Object instance = sDeviceControllerConstructor.newInstance(context);
                Boolean succ = (Boolean) sMethodHasWifi.invoke(instance);
                if (succ != null) {
                    return succ.booleanValue();
                }
            }
            catch (IllegalArgumentException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InstantiationException e) {
                Log.e(TAG, "exception", e);
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InvocationTargetException e) {
                Log.e(TAG, "exception", e);
            }
            
            return false;
        }
        
        @Override
        public boolean hasAudio(Context context)
        {
            try {
                Object instance = sDeviceControllerConstructor.newInstance(context);
                Boolean succ = (Boolean) sMethodHasAudio.invoke(instance);
                if (succ != null) {
                    return succ.booleanValue();
                }
            }
            catch (IllegalArgumentException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InstantiationException e) {
                Log.e(TAG, "exception", e);
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InvocationTargetException e) {
                Log.e(TAG, "exception", e);
            }
            
            return false;
        }
        
        @Override
        public boolean hasFrontLight(Context context)
        {
            try {
                Object instance = sDeviceControllerConstructor.newInstance(context);
                Boolean succ = (Boolean) sMethodHasFrontLight.invoke(instance);
                if (succ != null) {
                    return succ.booleanValue();
                }
            }
            catch (IllegalArgumentException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InstantiationException e) {
                Log.e(TAG, "exception", e);
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InvocationTargetException e) {
                Log.e(TAG, "exception", e);
            }
            
            return false;
        }
        
        @Override
        public boolean isEInkScreen()
        {
            return true;
        }

        @Override
        public EpdController.EPDMode getEpdMode()
        {
            return mCurrentMode;
        }
        
        @Override
        public boolean setEpdMode(Context context, EpdController.EPDMode mode)
        {
            String m = sEpdModePart;
            if (mode == EPDMode.FULL) {
                m = sEpdModeFull;
            }
            else if (mode == EPDMode.AUTO_A2 || mode == EPDMode.AUTO_BLACK_WHITE) {
                m = sEpdModeA2;
            }
            else if (mode == EPDMode.AUTO || mode == EPDMode.AUTO_PART || mode == EPDMode.TEXT) {
                m = sEpdModePart;
            }
            
            try {
                if (mContext != context) {
                    mContext = context;
                    mEpdManagerInstance = sEpdManagerConstructor.newInstance(context);
                }
                
                Boolean res = (Boolean)sMethodEpdSetMode.invoke(mEpdManagerInstance, m);
                if (res != null && res.booleanValue()) {
                    if (mode != EPDMode.FULL) {
                        // FULL mode is temporarily effective
                        mCurrentMode = mode;
                    }
                }
                
                return false;
            }
            catch (InstantiationException e) {
                Log.w(TAG, e);
            }
            catch (IllegalArgumentException e) {
                Log.w(TAG, e);
            }
            catch (IllegalAccessException e) {
                Log.w(TAG, e);
            }
            catch (InvocationTargetException e) {
                Log.w(TAG, e);
            }
            
            return false;
        }
        
        @Override
        public void invalidate(View view, UpdateMode mode)
        {
            int m = sViewNull;
            switch (mode) {
            case GU:
                m = sViewPart;
                break;
            case GU_FAST:
                m = sViewAuto;
                break;
            case GC:
                m = sViewFull;
                break;
            case DW:
                m = sViewA2;
                break;
            default:
                assert(false);
                break;
            }

            try {
                sMethodViewRequestEpdMode.invoke(view, (Integer)m);
            }
            catch (IllegalArgumentException e) {
                Log.e(TAG, "exception", e);
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InvocationTargetException e) {
                Log.e(TAG, "exception", e);
            }
                
            view.invalidate();
        }

        @Override
        public void postInvalidate(View view, UpdateMode mode)
        {
            int m = sViewNull;
            switch (mode) {
            case GU:
                m = sViewPart;
                break;
            case GU_FAST:
                m = sViewAuto;
                break;
            case GC:
                m = sViewFull;
                break;
            case DW:
                m = sViewA2;
                break;
            default:
                assert(false);
                break;
            }

            try {
                sMethodViewRequestEpdMode.invoke(view, (Integer)m);
            }
            catch (IllegalArgumentException e) {
                Log.e(TAG, "exception", e);
            }
            catch (IllegalAccessException e) {
                Log.e(TAG, "exception", e);
            }
            catch (InvocationTargetException e) {
                Log.e(TAG, "exception", e);
            }

            view.postInvalidate();
        }
    }

    @Override
    public String name()
    {
        return "RK2906";
    }

    @Override
    public boolean isPresent()
    {
        return (Build.MANUFACTURER.contentEquals("unknown") &&
                Build.MODEL.contentEquals("rk29sdk") &&
                Build.DEVICE.contentEquals("rk29sdk")) ||
                (Build.MANUFACTURER.contentEquals("unknown") && 
                        Build.MODEL.contentEquals("R65_HD") &&
                        Build.DEVICE.contentEquals("R65_HD"));
    }

    @Override
    public IDeviceController createController()
    {
        return RK2906Controller.createController();
    }
}
