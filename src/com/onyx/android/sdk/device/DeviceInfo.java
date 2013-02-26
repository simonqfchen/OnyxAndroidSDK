/**
 * 
 */
package com.onyx.android.sdk.device;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.onyx.android.sdk.device.EpdController.EPDMode;
import com.onyx.android.sdk.device.EpdController.UpdateMode;
import com.onyx.android.sdk.device.IDeviceFactory.IDeviceController;

/**
 * EPD work differently according to devices 
 * 
 * @author joy
 *
 */
public class DeviceInfo
{
    private final static String TAG = "DeviceInfo";
    
    private static class DefaultFactory implements IDeviceFactory
    {
        private static class DefaultController implements IDeviceFactory.IDeviceController
        {

            @Override
            public File getExternalStorageDirectory()
            {
                return android.os.Environment.getExternalStorageDirectory();
            }

            @Override
            public File getRemovableSDCardDirectory()
            {
                File storage_root = getExternalStorageDirectory();

                // if system has an emulated SD card(/mnt/sdcard) provided by device's NAND flash, 
                // then real SD card will be mounted as a child directory(/mnt/sdcard/extsd) in it, which names "extsd" here
                final String SDCARD_MOUNTED_FOLDER = "extsd";
                
                File extsd = new File(storage_root, SDCARD_MOUNTED_FOLDER);
                if (extsd.exists()) {
                    return extsd;
                }
                else {
                    return storage_root;
                }
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
                return false;
            }
            
            @Override
            public EPDMode getEpdMode()
            {
                return EPDMode.AUTO;
            }

            @Override
            public boolean setEpdMode(Context context, EPDMode mode)
            {
                return true;
            }

            @Override
            public void invalidate(View view, UpdateMode mode)
            {
                view.invalidate();
            }

            @Override
            public void postInvalidate(View view, UpdateMode mode)
            {
                view.postInvalidate();
            }
            
        }

        @Override
        public String name()
        {
            return "Default Device";
        }

        @Override
        public boolean isPresent()
        {
            return true;
        }

        @Override
        public IDeviceController createController()
        {
            return new DefaultController();
        }
        
    }
    
    private static DeviceInfo sInstance = new DeviceInfo();
    
    private IDeviceFactory mDefaultFactory = new DefaultFactory();
    private ArrayList<IDeviceFactory> mDeviceFactories = new ArrayList<IDeviceFactory>();
    
    private IDeviceFactory mPresentDeviceFactory = new DefaultFactory();
    private IDeviceController mPresentDeviceController = null;
    
    private DeviceInfo()
    {
        this.registerDevice(new RK2818Factory());
        this.registerDevice(new RK2906Factory());
        this.registerDevice(new IMX508Factory());
    }
    
    public static DeviceInfo singleton()
    {
        return sInstance;
    }
    
    public synchronized boolean registerDevice(IDeviceFactory factory)
    {
        if (mPresentDeviceController != null) {
            Log.w(TAG, "Device controller already activated: " + mPresentDeviceFactory.name());
        }
        for (IDeviceFactory f : mDeviceFactories) {
            if (f.name().equals(factory.name())) {
                Log.w(TAG, "Device already registered: " + factory.name());
                return false;
            }
        }
        
        mDeviceFactories.add(factory);
        return true;
    }
    
    public synchronized IDeviceController getDeviceController()
    {
        if (mPresentDeviceController != null) {
            return mPresentDeviceController;
        }
        
        Log.v(TAG, "device info: " + Build.MANUFACTURER + ", " + Build.MODEL + ", " + Build.DEVICE);
        
        for (IDeviceFactory f : mDeviceFactories) {
            if (f.isPresent()) {
                mPresentDeviceFactory = f;
                break;
            }
        }
        if (mPresentDeviceFactory == null) {
            mPresentDeviceFactory = mDefaultFactory;
        }
        
        mPresentDeviceController = mPresentDeviceFactory.createController();
        if (mPresentDeviceController == null) {
            Log.w(TAG, "create device controller failed: " + mPresentDeviceFactory.name());
            
            mPresentDeviceFactory = mDefaultFactory;
            mPresentDeviceController = mDefaultFactory.createController();
        }

        Log.v(TAG, "present device: " + mPresentDeviceFactory.name());

        assert(mPresentDeviceController != null);
        return mPresentDeviceController;
    }
}
