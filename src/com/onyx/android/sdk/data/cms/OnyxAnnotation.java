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
public class OnyxAnnotation
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
        
        // need read at runtime
        private static boolean sColumnIndexesInitialized = false; 
        private static int sColumnID = -1;
        private static int sColumnMD5 = -1;
        private static int sColumnQuote = -1;
        private static int sColumnLocationBegin = -1;
        private static int sColumnLocationEnd = -1;
        private static int sColumnNote = -1;
        private static int sColumnUpdateTime = -1;
        
        public static ContentValues createColumnData(OnyxAnnotation annot)
        {
            ContentValues values = new ContentValues();
            values.put(MD5, annot.getMD5());
            values.put(QUOTE, annot.getQuote());
            values.put(LOCATION_BEGIN, annot.getLocationBegin());
            values.put(LOCATION_END, annot.getLocationEnd());
            values.put(NOTE, annot.getNote());
            values.put(UPDATE_TIME, SerializationUtil.dateToString(annot.getUpdateTime()));
            
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
                
                sColumnIndexesInitialized = true;
            }
            
            long id = c.getLong(sColumnID);
            String md5 = c.getString(sColumnMD5);
            String quote = c.getString(sColumnQuote);
            String loc_begin = c.getString(sColumnLocationBegin);
            String loc_end = c.getString(sColumnLocationEnd);
            String note = c.getString(sColumnNote);
            String update_time = c.getString(sColumnUpdateTime);
            
            annot.setId(id);
            annot.setMD5(md5);
            annot.setQuote(quote);
            annot.setLocationBegin(loc_begin);
            annot.setLocationEnd(loc_end);
            annot.setNote(note);
            annot.setUpdateTime(SerializationUtil.dateFromString(update_time));
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
    private String mLocationBegin = null;
    private String mLocationEnd = null;
    private String mNote = null;
    private Date mUpdateTime = null;
    
    public OnyxAnnotation()
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
    
}
