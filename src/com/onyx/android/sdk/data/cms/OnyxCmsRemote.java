/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import com.onyx.android.sync.IOnyxSyncService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
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
    
	private static void initSyncService(Context context)
	{
		if (mService == null) {
			context.startService(new Intent("com.onyx.android.sync.OnyxSyncService"));
			context.bindService(new Intent("com.onyx.android.sync.OnyxSyncService"), 
					mConnection, Context.BIND_AUTO_CREATE);
		}
	}
	
	public static boolean sync(Context context)
	{
		return sync(context, context.getPackageName());
	}
	
	public static boolean sync(Context context, String application)
	{
		initSyncService(context);

		try {
			try {
				return mService.sync(application);
			} catch (DeadObjectException ex) {
				mService = null;
				initSyncService(context);
				return mService.sync(application);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
}
