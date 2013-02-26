/**
 * 
 */
package com.onyx.android.sdk.data.util;

import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author joy
 *
 */
public class IntentFilterFactory
{
    public static final String ACTION_MEDIA_SCANNED = "com.onyx.android.intent.action.ACTION_MEDIA_SCANNED";
    
    private static final IntentFilter SDCARD_UNMOUNTED_FILTER;
    private static final IntentFilter MEDIA_MOUNTED_FILTER;
    private static final IntentFilter MEDIA_SCANNED_FILTER;
    
    private static IntentFilter mIntentFilter = null;
    
    static {
        SDCARD_UNMOUNTED_FILTER = new IntentFilter(); 
        SDCARD_UNMOUNTED_FILTER.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        SDCARD_UNMOUNTED_FILTER.addAction(Intent.ACTION_MEDIA_REMOVED);
        SDCARD_UNMOUNTED_FILTER.addAction(Intent.ACTION_MEDIA_SHARED);
        SDCARD_UNMOUNTED_FILTER.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
        SDCARD_UNMOUNTED_FILTER.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        SDCARD_UNMOUNTED_FILTER.addDataScheme("file"); 
        
        MEDIA_MOUNTED_FILTER = new IntentFilter();
        MEDIA_MOUNTED_FILTER.addAction(Intent.ACTION_MEDIA_MOUNTED);
        MEDIA_MOUNTED_FILTER.addDataScheme("file"); 
        
        MEDIA_SCANNED_FILTER = new IntentFilter();
        MEDIA_SCANNED_FILTER.addAction(ACTION_MEDIA_SCANNED);
        
    }
    
    public static IntentFilter getSDCardUnmountedFilter()
    {
        return SDCARD_UNMOUNTED_FILTER;
    }
    
    public static IntentFilter getMediaMountedFilter()
    {
        return MEDIA_MOUNTED_FILTER;
    }
    
    public static IntentFilter getMediaScannedFilter()
    {
        return MEDIA_SCANNED_FILTER;
    }
    
    public static IntentFilter getIntentFilterFrontPreferredApplications()
    {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ActionNameFactory.FRONT_PREFERRED_APPLICATIONS);

        return mIntentFilter;
    }

    
}
