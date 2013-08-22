/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author joy
 *
 */
public class OnyxBookmark implements Parcelable
{
    private static final String TAG = "OnyxBookmark";
    
    public static final String DB_TABLE_NAME = "library_bookmark";
    public static final Uri CONTENT_URI = Uri.parse("content://" + OnyxCmsCenter.PROVIDER_AUTHORITY + "/" + DB_TABLE_NAME);
    
    /**
     * TODO. It's better to use key-value based table instead of column based, 
     * since column based table is not sustainable to change
     * 
     * @author joy
     *
     */
    public static class Columns implements BaseColumns
    {
        public static String MD5 = "MD5";
        public static String QUOTE = "Quote";
        public static String LOCATION = "Location";
        public static String UPDATE_TIME = "UpdateTime";
        public static String APPLICATION = "Application";
        
        // need read at runtime
        private static boolean sColumnIndexesInitialized = false; 
        private static int sColumnID = -1;
        private static int sColumnMD5 = -1;
        private static int sColumnQuote = -1;
        private static int sColumnLocation = -1;
        private static int sColumnUpdateTime = -1;
        private static int sColumnApplication = -1;
        
        public static ContentValues createColumnData(OnyxBookmark bookmark)
        {
            ContentValues values = new ContentValues();
            values.put(MD5, bookmark.getMD5());
            values.put(QUOTE, bookmark.getQuote());
            values.put(LOCATION, bookmark.getLocation());
            values.put(UPDATE_TIME, SerializationUtil.dateToString(bookmark.getUpdateTime()));
            values.put(APPLICATION, bookmark.getApplication());
            
            return values;
        }
        
        public static void readColumnData(Cursor c, OnyxBookmark bookmark)
        {
            if (!sColumnIndexesInitialized) {
                sColumnID = c.getColumnIndex(_ID);
                sColumnMD5 = c.getColumnIndex(MD5);
                sColumnQuote = c.getColumnIndex(QUOTE);
                sColumnLocation = c.getColumnIndex(LOCATION);
                sColumnUpdateTime = c.getColumnIndex(UPDATE_TIME);
                sColumnApplication = c.getColumnIndex(APPLICATION);
                
                sColumnIndexesInitialized = true;
            }
            
            long id = c.getLong(sColumnID);
            String md5 = c.getString(sColumnMD5);
            String quote = c.getString(sColumnQuote);
            String location = c.getString(sColumnLocation);
            String update_time = c.getString(sColumnUpdateTime);
            String application = c.getString(sColumnApplication);
            
            bookmark.setId(id);
            bookmark.setMD5(md5);
            bookmark.setQuote(quote);
            bookmark.setLocation(location);
            bookmark.setUpdateTime(SerializationUtil.dateFromString(update_time));
            bookmark.setApplication(application);
        }
        
        public static OnyxBookmark readColumnData(Cursor c)
        {
            OnyxBookmark a = new OnyxBookmark();
            readColumnData(c, a);
            return a;
        }
        
    }
    
    public static class SerializationUtil {
        public static String dateToString(Date d)
        {
        	if (d == null) {
        		return "null";
        	} else {
        		return SimpleDateFormat.getDateTimeInstance().format(d);
        	}
        }
        public static Date dateFromString(String str)
        {
        	if ("null".equals(str)) {
        		return null;
        	} else {
	            try {
	                return SimpleDateFormat.getDateTimeInstance().parse(str);
	            }
	            catch (ParseException e) {
	                Log.w(TAG, e);
	            }
	            return null;
        	}
        }
    }
    
    // -1 should never be valid DB value
    private static final int INVALID_ID = -1;
    
    private long mId = INVALID_ID;
    private String mMD5 = null;
    private String mQuote = null;
    private String mLocation = null;
    private Date mUpdateTime = null;
    private String mApplication = null;
    
    public OnyxBookmark()
    {

    }
    
    public OnyxBookmark(Parcel source)
    {
    	readFromParcel(source);
    }
    
    public OnyxBookmark(OnyxBookmark bookmark)
    {
    	mId = bookmark.getId();
    	mMD5 = bookmark.getMD5();
    	mQuote = bookmark.getQuote();
    	mLocation = bookmark.getLocation();
    	mUpdateTime = bookmark.getUpdateTime();
    	mApplication = bookmark.getApplication();
    }

    @Override
	public boolean equals(Object o) {
		if (!(o instanceof OnyxBookmark)) {
			return false;
		}
		
		OnyxBookmark bookmark = (OnyxBookmark) o;
		
		try {
			return ((mMD5 == bookmark.getMD5() || mMD5.equals(bookmark.getMD5()))
					&& (mQuote == bookmark.getQuote() || mQuote.equals(bookmark.getQuote()))
					&& (mLocation == bookmark.getLocation() || mLocation.equals(bookmark.getLocation()))
					&& (mUpdateTime == bookmark.getUpdateTime() || mUpdateTime.equals(bookmark.getUpdateTime()))
					&& (mApplication == bookmark.getApplication() || mApplication.equals(bookmark.getApplication()))
					);
		} catch (Exception e) {
			return false;
		}
	}

    public long getId()
    {
        return mId;
    }

    public void setId(long id)
    {
        this.mId = id;
    }

    public String getMD5()
    {
        return mMD5;
    }

    public void setMD5(String md5)
    {
        this.mMD5 = md5;
    }

    public String getQuote()
    {
        return mQuote;
    }

    public void setQuote(String quote)
    {
        this.mQuote = quote;
    }
    
    public String getLocation()
    {
        return mLocation;
    }
    
    public void setLocation(String location)
    {
        mLocation = location;
    }

    public Date getUpdateTime()
    {
        return mUpdateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.mUpdateTime = updateTime;
    }

    public String getApplication()
    {
    	return mApplication;
    }
    
    public void setApplication(String application)
    {
    	mApplication = application;
    }
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	    dest.writeLong(mId);
	    dest.writeString(mMD5);
	    dest.writeString(mQuote);
	    dest.writeString(mLocation);
    	dest.writeString(SerializationUtil.dateToString(mUpdateTime));
    	dest.writeString(mApplication);
	}
	
	public void readFromParcel(Parcel source)
	{
    	mId = source.readLong();
    	mMD5 = source.readString();
    	mQuote = source.readString();
    	mLocation = source.readString();
    	mUpdateTime = SerializationUtil.dateFromString(source.readString());
    	mApplication = source.readString();
	}
	
	public static final Parcelable.Creator<OnyxBookmark> CREATOR 
		= new Parcelable.Creator<OnyxBookmark>() 
	{
		
		@Override
		public OnyxBookmark createFromParcel(Parcel source)
		{
			Log.i(TAG, "Create bookmark from parcel!");
			return new OnyxBookmark(source);
		}
		
		@Override
		public OnyxBookmark[] newArray(int size) 
		{
			return new OnyxBookmark[size];
		}

	};

}
