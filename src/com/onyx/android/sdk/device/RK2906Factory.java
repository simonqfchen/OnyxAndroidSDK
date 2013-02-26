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
        private static Class sClassEpdManager = null;
        @SuppressWarnings("rawtypes")
        private static Constructor sConstructor = null;
        
        private static Method sMethodEpdSetMode = null;
        private static String sEpdModeFull = DEFAULT_EPD_MODE;
        private static String sEpdModeA2 = DEFAULT_EPD_MODE;
        private static String sEpdModePart = DEFAULT_EPD_MODE;
        
        private Context mContext = null;
        private Object mEpdManagerInstance = null;
        
        private EPDMode mCurrentMode = EPDMode.AUTO;
        
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
                    
                    sClassEpdManager = Class.forName("android.hardware.EpdManager");
                    sConstructor = sClassEpdManager.getConstructor(Context.class);
                    sMethodEpdSetMode = sClassEpdManager.getMethod("setMode", String.class);
                    sEpdModeFull = (String)sClassEpdManager.getField("FULL").get(null);
                    sEpdModeA2 = (String)sClassEpdManager.getField("A2").get(null);
                    sEpdModePart = (String)sClassEpdManager.getField("PART").get(null);
                    
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
        public boolean hasWifi()
        {
            return true;
        }
        
        @Override
        public TouchType getTouchType()
        {
            return TouchType.IR;
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
                    mEpdManagerInstance = sConstructor.newInstance(context);
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
