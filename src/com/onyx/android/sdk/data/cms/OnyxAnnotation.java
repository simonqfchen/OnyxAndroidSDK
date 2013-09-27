/**
 * 
 */
package com.onyx.android.sdk.data.cms;

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
public class OnyxAnnotation implements Parcelable
{

	private static final String TAG = "OnyxAnnotation";
    
    public static final String DB_TABLE_NAME = "library_annotation";
    public static final Uri CONTENT_URI = Uri.parse("content://" + OnyxCmsCenter.PROVIDER_AUTHORITY + "/" + DB_TABLE_NAME);
    
    public static class Columns implements BaseColumns
    {
        public static String MD5 = "MD5";
        public static String QUOTE = "Quote";
        public static String LOCATION_BEGIN = "LocationBegin";
        public static String LOCATION_END = "LocationEnd";
        public static String NOTE = "Note";
        public static String UPDATE_TIME = "UpdateTime";
        public static String APPLICATION = "Application";
        
        // need read at runtime
        private static boolean sColumnIndexesInitialized = false; 
        private static int sColumnID = -1;
        private static int sColumnMD5 = -1;
        private static int sColumnQuote = -1;
        private static int sColumnLocationBegin = -1;
        private static int sColumnLocationEnd = -1;
        private static int sColumnNote = -1;
        private static int sColumnUpdateTime = -1;
        private static int sColumnApplication = -1;
        
        public static ContentValues createColumnData(OnyxAnnotation annot)
        {
            ContentValues values = new ContentValues();
            values.put(MD5, annot.getMD5());
            values.put(QUOTE, annot.getQuote());
            values.put(LOCATION_BEGIN, annot.getLocationBegin());
            values.put(LOCATION_END, annot.getLocationEnd());
            values.put(NOTE, annot.getNote());
            values.put(UPDATE_TIME, SerializationUtil.dateToString(annot.getUpdateTime()));
            values.put(APPLICATION, annot.getApplication());
            
            return values;
        }
        
        public static void readColumnData(Cursor c, OnyxAnnotation annot)
        {
            if (!sColumnIndexesInitialized) {
                sColumnID = c.getColumnIndex(_ID);
                sColumnMD5 = c.getColumnIndex(MD5);
                sColumnQuote = c.getColumnIndex(QUOTE);
                sColumnLocationBegin = c.getColumnIndex(LOCATION_BEGIN);
                sColumnLocationEnd = c.getColumnIndex(LOCATION_END);
                sColumnNote = c.getColumnIndex(NOTE);
                sColumnUpdateTime = c.getColumnIndex(UPDATE_TIME);
                sColumnApplication = c.getColumnIndex(APPLICATION);
                
                sColumnIndexesInitialized = true;
            }
            
            long id = c.getLong(sColumnID);
            String md5 = c.getString(sColumnMD5);
            String quote = c.getString(sColumnQuote);
            String loc_begin = c.getString(sColumnLocationBegin);
            String loc_end = c.getString(sColumnLocationEnd);
            String note = c.getString(sColumnNote);
            String update_time = c.getString(sColumnUpdateTime);
            String application = c.getString(sColumnApplication);
            
            annot.setId(id);
            annot.setMD5(md5);
            annot.setQuote(quote);
            annot.setLocationBegin(loc_begin);
            annot.setLocationEnd(loc_end);
            annot.setNote(note);
            annot.setUpdateTime(SerializationUtil.dateFromString(update_time));
            annot.setApplication(application);
        }
        
        public static OnyxAnnotation readColumnData(Cursor c)
        {
            OnyxAnnotation a = new OnyxAnnotation();
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
        		return d.getTime() + "";
        	}
        }
        public static Date dateFromString(String str)
        {
        	if ("null".equals(str)) {
        		return null;
        	} else {
	            try {
	            	return new Date(Long.parseLong(str));
	            }
	            catch (NumberFormatException e) {
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
    private String mLocationBegin = null;
    private String mLocationEnd = null;
    private String mNote = null;
    private Date mUpdateTime = null;
    private String mApplication = null;
    
    public OnyxAnnotation()
    {
    }


    public OnyxAnnotation(Parcel source)
    {
    	readFromParcel(source);
    }

    public OnyxAnnotation(OnyxAnnotation annotation)
    {
    	mId = annotation.getId();
    	mMD5 = annotation.getMD5();
    	mQuote = annotation.getQuote();
    	mLocationBegin = annotation.getLocationBegin();
    	mLocationEnd = annotation.getLocationEnd();
    	mNote = annotation.getNote();
    	mUpdateTime = annotation.getUpdateTime();
    	mApplication = annotation.getApplication();
    }

    @Override
	public boolean equals(Object o) {
		if (!(o instanceof OnyxAnnotation)) {
			return false;
		}
		
		OnyxAnnotation annotation = (OnyxAnnotation) o;
				
		try {
			return ((mMD5 == annotation.getMD5() || mMD5.equals(annotation.getMD5()))
					&& (mQuote == annotation.getQuote() || mQuote.equals(annotation.getQuote()))
					&& (mNote == annotation.getNote() || mNote.equals(annotation.getNote()))
					&& (mLocationBegin == annotation.getLocationBegin() || mLocationBegin.equals(annotation.getLocationBegin()))
					&& (mLocationEnd == annotation.getLocationEnd() || mLocationEnd.equals(annotation.getLocationEnd()))
//					&& (mUpdateTime == annotation.getUpdateTime() || mUpdateTime.equals(annotation.getUpdateTime()))
//					&& (mApplication == annotation.getApplication() || mApplication.equals(annotation.getApplication()))
					);
		} catch (Exception e) {
			e.printStackTrace();
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

    public String getLocationBegin()
    {
        return mLocationBegin;
    }

    public void setLocationBegin(String locationBegin)
    {
        this.mLocationBegin = locationBegin;
    }
    
    public String getLocationEnd()
    {
        return mLocationEnd;
    }

    public void setLocationEnd(String locationEnd)
    {
        this.mLocationEnd = locationEnd;
    }

    public String getNote()
    {
        return mNote;
    }

    public void setNote(String note)
    {
        this.mNote = note;
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
        dest.writeString(mLocationBegin);
        dest.writeString(mLocationEnd);
        dest.writeString(mNote);
        dest.writeString(SerializationUtil.dateToString(mUpdateTime));
        dest.writeString(mApplication);
	}
	
	public void readFromParcel(Parcel source)
	{
    	mId = source.readLong();
    	mMD5 = source.readString();
    	mQuote = source.readString();
    	mLocationBegin = source.readString();
    	mLocationEnd = source.readString();
    	mNote = source.readString();
    	mUpdateTime = SerializationUtil.dateFromString(source.readString());
    	mApplication = source.readString();
	}

	public static final Parcelable.Creator<OnyxAnnotation> CREATOR 
								= new Parcelable.Creator<OnyxAnnotation>() 
	{
	
		@Override
		public OnyxAnnotation createFromParcel(Parcel source) 
		{
			Log.i(TAG, "Create annotation from parcel!");
			return new OnyxAnnotation(source);
		}
		
		@Override
		public OnyxAnnotation[] newArray(int size) 
		{
			return new OnyxAnnotation[size];
		}
	
	};

    public void copyFrom(OnyxAnnotation annotation)
    {
        mId = annotation.mId;
        mMD5 = annotation.mMD5;
        mQuote = annotation.mQuote;
        mLocationBegin = annotation.mLocationBegin;
        mLocationEnd = annotation.mLocationEnd;
        mNote = annotation.mNote;
        mUpdateTime = annotation.mUpdateTime;
    }
    
}
