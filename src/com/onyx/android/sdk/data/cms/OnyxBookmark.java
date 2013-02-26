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
import android.provider.BaseColumns;
import android.util.Log;

/**
 * @author joy
 *
 */
public class OnyxBookmark
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
        
        // need read at runtime
        private static boolean sColumnIndexesInitialized = false; 
        private static int sColumnID = -1;
        private static int sColumnMD5 = -1;
        private static int sColumnQuote = -1;
        private static int sColumnLocation = -1;
        private static int sColumnUpdateTime = -1;
        
        public static ContentValues createColumnData(OnyxBookmark bookmark)
        {
            ContentValues values = new ContentValues();
            values.put(MD5, bookmark.getMD5());
            values.put(QUOTE, bookmark.getQuote());
            values.put(LOCATION, bookmark.getLocation());
            values.put(UPDATE_TIME, SerializationUtil.dateToString(bookmark.getUpdateTime()));
            
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
                
                sColumnIndexesInitialized = true;
            }
            
            long id = c.getLong(sColumnID);
            String md5 = c.getString(sColumnMD5);
            String quote = c.getString(sColumnQuote);
            String location = c.getString(sColumnLocation);
            String update_time = c.getString(sColumnUpdateTime);
            
            bookmark.setId(id);
            bookmark.setMD5(md5);
            bookmark.setQuote(quote);
            bookmark.setLocation(location);
            bookmark.setUpdateTime(SerializationUtil.dateFromString(update_time));
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
            return SimpleDateFormat.getDateTimeInstance().format(d);
        }
        public static Date dateFromString(String str)
        {
            try {
                return SimpleDateFormat.getDateTimeInstance().parse(str);
            }
            catch (ParseException e) {
                Log.w(TAG, e);
            }
            return null;
        }
    }
    
    // -1 should never be valid DB value
    private static final int INVALID_ID = -1;
    
    private long mId = INVALID_ID;
    private String mMD5 = null;
    private String mQuote = null;
    private String mLocation = null;
    private Date mUpdateTime = null;
    
    public OnyxBookmark()
    {
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
}
