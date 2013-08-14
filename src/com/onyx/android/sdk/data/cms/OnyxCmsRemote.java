/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import com.onyx.android.sync.IOnyxSyncService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author joy
 *
 */
public class OnyxCmsRemote 
{
    private static IOnyxSyncService mService = null;
    
    /**
     * Class for interacting with the main interface of the service.
     */
    private static ServiceConnection mConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className,
                IBinder service)
        {
            mService = IOnyxSyncService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className)
        {
            mService = null;
        }
    };
    
	public static void initSyncService(Context context)
	{
		if (mService == null) {
			context.startService(new Intent("com.onyx.android.sync.OnyxSyncService"));
			context.bindService(new Intent("com.onyx.android.sync.OnyxSyncService"), 
					mConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	public static void unbindSyncService(Context context)
	{
		context.unbindService(mConnection);
	}
	
	public static boolean sync(Context context, OnyxCmsAggregatedData localData, 
					OnyxCmsAggregatedData updates, OnyxCmsAggregatedData removes)
	{
		if (mService == null) {
			return false;
		}

		try {
			mService.sync(localData, updates, removes);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean test_position(Context context, OnyxPosition inPosition, OnyxPosition outPosition)
	{
		if (mService == null) {
			return false;
		}

		try {
			mService.test_position(inPosition, outPosition);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean test_bookmark(Context context, OnyxBookmark inPosition, OnyxBookmark outPosition)
	{
		if (mService == null) {
			return false;
		}

		try {
			mService.test_bookmark(inPosition, outPosition);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean test_metadata(Context context, OnyxMetadata inMetadata, OnyxMetadata outMetadata)
	{
		if (mService == null) {
			return false;
		}

		try {
			mService.test_metadata(inMetadata, outMetadata);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean test_annotation(Context context, OnyxAnnotation inPosition, OnyxAnnotation outPosition)
	{
		if (mService == null) {
			return false;
		}

		try {
			mService.test_annotation(inPosition, outPosition);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return false;
	}
	
}
