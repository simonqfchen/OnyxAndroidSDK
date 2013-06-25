package com.onyx.android.sdk.data.cms;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class OnyxReadingHistoryEntry
{
    public static final String DB_TABLE_NAME = "library_history";
    public static final Uri CONTENT_URI = Uri.parse("content://" + OnyxCmsCenter.PROVIDER_AUTHORITY + "/" + DB_TABLE_NAME);
    
    /**
     * only store reading history longer than the threshold 5 * 60s.
     * considering being configured by user if necessary 
     */
    public static int HISTORY_THRESHOLD = 300;
    
    public static class Columns implements BaseColumns 
    {
        public static String MD5 = "MD5";
        public static final String START_TIME = "StartTime";
        public static final String END_TIME = "EndTime";
        public static String EXTRA_ATTRIBUTES = "ExtraAttributes";

        // need read at runtime
        private static boolean sColumnIndexesInitialized = false; 
        private static int sColumnID = -1;
        private static int sColumnMD5 = -1;
        private static int sColumnStartTime = -1;
        private static int sColumnEndTime = -1;
        private static int sColumnExtraAttributes = -1;
        
        public static ContentValues createColumnData(OnyxReadingHistoryEntry entry)
        {
            ContentValues values = new ContentValues();
            values.put(MD5, entry.getMD5());
            values.put(START_TIME, entry.getStartTime() == null ? 0 : entry.getStartTime().getTime());
            values.put(END_TIME, entry.getEndTime() == null ? 0 : entry.getEndTime().getTime());

            return values;
        }
        
        public static OnyxReadingHistoryEntry readColumnsData(Cursor c)
        {
            if (!sColumnIndexesInitialized) {
                sColumnID = c.getColumnIndex(_ID);
                sColumnMD5 = c.getColumnIndex(MD5);
                sColumnStartTime = c.getColumnIndex(START_TIME);
                sColumnEndTime = c.getColumnIndex(END_TIME);
                sColumnExtraAttributes = c.getColumnIndex(EXTRA_ATTRIBUTES);
                
                sColumnIndexesInitialized = true;
            }

            long id = c.getLong(sColumnID);
            String md5 = c.getString(sColumnMD5);
            long start_time = c.getLong(sColumnStartTime);
            long end_time = c.getLong(sColumnEndTime);
            String extra_attributes = c.getString(sColumnExtraAttributes);
            
            OnyxReadingHistoryEntry entry = new OnyxReadingHistoryEntry();
            entry.setId(id);
            entry.setMD5(md5);
            assert(start_time > 0);
            entry.setStartTime(new Date(start_time));
            assert(end_time > 0);
            entry.setEndTime(new Date(end_time));
            entry.setExtraAttributes(extra_attributes);

            return entry;
        }
    }
    
    private long mId = -1;
    private String mMD5 = null;
    private Date mStartTime = null;
    private Date mEndTime = null;
    /**
     * Additional attributes for flexibility
     */
    private String mExtraAttributes = null; 
    
    public long getId()
    {
        return mId;
    }
    public void setId(long id)
    {
        mId = id;
    }
    public String getMD5()
    {
        return mMD5;
    }
    public void setMD5(String md5)
    {
        this.mMD5 = md5;
    }
    public Date getStartTime()
    {
        return mStartTime;
    }
    public void setStartTime(Date time)
    {
        this.mStartTime = time;
    }
    public Date getEndTime()
    {
        return mEndTime;
    }
    public void setEndTime(Date time)
    {
        this.mEndTime = time;
    }
    /**
     * may return null
     * 
     * @return
     */
    public String getExtraAttributes()
    {
        return mExtraAttributes;
    }
    public void setExtraAttributes(String extraAttributes)
    {
        this.mExtraAttributes = extraAttributes;
    }

}
