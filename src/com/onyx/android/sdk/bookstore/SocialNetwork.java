/**
 * 
 */
package com.onyx.android.sdk.bookstore;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author Simon
 *
 */
public class SocialNetwork implements Parcelable
{
	private static final String TAG = "SocialNetwork";

	private int mNetworkId;
	private String mNetworkName;
	
	public SocialNetwork(int networkId, String networkName)
	{
		mNetworkId = networkId;
		mNetworkName = networkName;
	}
	
	public SocialNetwork(Parcel source)
	{
		readFromParcel(source);
	}
	
	public int getNetworkId()
	{
		return mNetworkId; 
	}
	
	public String getNetworkName() {
		return mNetworkName;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void readFromParcel(Parcel source)
	{
		mNetworkId = source.readInt();
		mNetworkName = source.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mNetworkId);
		dest.writeString(mNetworkName);
	}
	
	public static final Parcelable.Creator<SocialNetwork> CREATOR 
							= new Parcelable.Creator<SocialNetwork>() 
	{
	
		@Override
		public SocialNetwork createFromParcel(Parcel source) 
		{
			return new SocialNetwork(source);
		}
		
		@Override
		public SocialNetwork[] newArray(int size) 
		{
			return new SocialNetwork[size];
		}
	
	};

	private static ISocialShareService mService = null;
	
    /**
     * Class for interacting with the main interface of the service.
     */
    private static ServiceConnection mConnection = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className,
                IBinder service)
        {
        	Log.i(TAG, "Connected");
            mService = ISocialShareService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className)
        {
        	Log.i(TAG, "Disconnected");
            mService = null;
        }
    };
    
	private static void initService(Context context)
	{
		if (mService == null) {
			context.startService(new Intent("com.onyx.android.sdk.bookstore.SocialShareService"));
			context.bindService(new Intent("com.onyx.android.sdk.bookstore.SocialShareService"), 
					mConnection, Context.BIND_AUTO_CREATE);
			
			int count = 30;
			
			while (count-- > 0) {
				if (mService != null) {
					break;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	public static boolean getNetworks(Context context, List<SocialNetwork> networks)
	{
		initService(context);

		try {
			try {
				return mService.getSocialNetworks(networks);
			} catch (DeadObjectException ex) {
				mService = null;
				initService(context);
				return mService.getSocialNetworks(networks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean socialShare(Context context, String isbn, int socialId, String selectText, String note)
	{
		initService(context);

		try {
			try {
				return mService.socialShare(isbn, socialId, selectText, note);
			} catch (DeadObjectException ex) {
				mService = null;
				initService(context);
				return mService.socialShare(isbn, socialId, selectText, note);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
