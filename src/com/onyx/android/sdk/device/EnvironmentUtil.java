/**
 * 
 */
package com.onyx.android.sdk.device;

import java.io.File;

/**
 * duplication of android.os.Environment to provide device specific function
 * 
 * @author joy
 *
 */
public class EnvironmentUtil
{
    public static final File EXTERNAL_STORAGE_ANDROID_DATA_DIRECTORY = new File(new File(getExternalStorageDirectory(),
            "Android"), "data");
    
    /**
     * wrapper of android.os.Environment.getExternalStorageDirectory
     * 
     * @return
     */
    public static File getExternalStorageDirectory()
    {
        return DeviceInfo.singleton().getDeviceController().getExternalStorageDirectory();
    }
    
    /**
     * Returns the path for android-specific data on the SD card.
     */
    public static File getExternalStorageAndroidDataDir()
    {
        return EXTERNAL_STORAGE_ANDROID_DATA_DIRECTORY;
    }
    
    /**
     * Generates the raw path to an application's data
     */
    public static File getExternalStorageAppDataDirectory(String packageName) {
        return new File(EXTERNAL_STORAGE_ANDROID_DATA_DIRECTORY, packageName);
    }
    
    /**
     * Generates the path to an application's files.
     */
    public static File getExternalStorageAppFilesDirectory(String packageName) {
        return new File(new File(EXTERNAL_STORAGE_ANDROID_DATA_DIRECTORY,
                packageName), "files");
    }
    
    /**
     * Generates the path to an application's cache.
     */
    public static File getExternalStorageAppCacheDirectory(String packageName) {
        return new File(new File(EXTERNAL_STORAGE_ANDROID_DATA_DIRECTORY,
                packageName), "cache");
    }
    
    /**
     * directory of removable SD card, can be different from getExternalStorageDirectory() according to devices
     * 
     * @return
     */
    public static File getRemovableSDCardDirectory()
    {
        return DeviceInfo.singleton().getDeviceController().getRemovableSDCardDirectory();
    }
    
    public static boolean isFileOnRemovableSDCard(File file)
    {
        return DeviceInfo.singleton().getDeviceController().isFileOnRemovableSDCard(file);
    }
    
    
}
