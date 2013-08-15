/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.onyx.android.sdk.data.util.ProfileUtil;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author Simon
 *
 */
public class OnyxSyncCenter {

	private static final String TAG = "OnyxSyncCenter";
	public static final String PROVIDER_AUTHORITY = "com.onyx.android.sdk.OnyxSyncProvider";
	
    public static final Uri METADATA_CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTHORITY + "/" + OnyxMetadata.DB_TABLE_NAME);
    public static final Uri POSITION_CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTHORITY + "/" + OnyxPosition.DB_TABLE_NAME);
    public static final Uri BOOKMARK_CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTHORITY + "/" + OnyxBookmark.DB_TABLE_NAME);
    public static final Uri ANNOTATION_CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTHORITY + "/" + OnyxAnnotation.DB_TABLE_NAME);
	
    
    
	public static boolean getBookmarks(Context context, String md5, List<OnyxBookmark> result)
	{
        Cursor c = null;
        try {
            ProfileUtil.start(TAG, "query bookmarks");
            c = context.getContentResolver().query(BOOKMARK_CONTENT_URI,
                    null,
                    OnyxBookmark.Columns.MD5 + "='" + md5 + "'", 
                    null, null);
            ProfileUtil.end(TAG, "query bookmarks");

            if (c == null) {
                Log.d(TAG, "query database failed");
                return false;
            }

            ProfileUtil.start(TAG, "read db result");
            readBookmarkCursor(c, result);
            ProfileUtil.end(TAG, "read db result");
            
            Log.d(TAG, "items loaded, count: " + result.size());
            
            return true;
        } finally {
            if (c != null) {
                c.close();
            }
        }
	}
	
    public static boolean insertBookmark(Context context, OnyxBookmark bookmark)
    {
        Uri result = context.getContentResolver().insert(
                BOOKMARK_CONTENT_URI,
                OnyxBookmark.Columns.createColumnData(bookmark));
        if (result == null) {
            return false;
        }

        String id = result.getLastPathSegment();
        if (id == null) {
            return false;
        }

        bookmark.setId(Long.parseLong(id));

        return true;
    }
    
    private static void readBookmarkCursor(Cursor c,
            Collection<OnyxBookmark> result)
    {
        if (c.moveToFirst()) {
            result.add(OnyxBookmark.Columns.readColumnData(c));

            while (c.moveToNext()) {
                if (Thread.interrupted()) {
                    return;
                }

                result.add(OnyxBookmark.Columns.readColumnData(c));
            }
        }
    }
    
	public static boolean getAnnotations(Context context, String md5, List<OnyxAnnotation> result)
	{
        Cursor c = null;
        try {
            ProfileUtil.start(TAG, "query annotations");
            c = context.getContentResolver().query(ANNOTATION_CONTENT_URI,
                    null, 
                    OnyxAnnotation.Columns.MD5 + "='" + md5 + "'", 
                    null, null);
            ProfileUtil.end(TAG, "query annotations");

            if (c == null) {
                Log.d(TAG, "query database failed");
                return false;
            }

            ProfileUtil.start(TAG, "read db result");
            readAnnotationCursor(c, result);
            ProfileUtil.end(TAG, "read db result");
            
            Log.d(TAG, "items loaded, count: " + result.size());
            
            return true;
        } finally {
            if (c != null) {
                c.close();
            }
        }
	}
	
    public static boolean insertAnnotation(Context context, OnyxAnnotation annotation)
    {
        Uri result = context.getContentResolver().insert(
                ANNOTATION_CONTENT_URI,
                OnyxAnnotation.Columns.createColumnData(annotation));
        if (result == null) {
            return false;
        }

        String id = result.getLastPathSegment();
        if (id == null) {
            return false;
        }

        annotation.setId(Long.parseLong(id));

        return true;
    }

    private static void readAnnotationCursor(Cursor c,
            Collection<OnyxAnnotation> result)
    {
        if (c.moveToFirst()) {
            result.add(OnyxAnnotation.Columns.readColumnData(c));

            while (c.moveToNext()) {
                if (Thread.interrupted()) {
                    return;
                }

                result.add(OnyxAnnotation.Columns.readColumnData(c));
            }
        }
    }
    
	public static boolean getPosition(Context context, String md5, OnyxPosition result)
	{
        Cursor c = null;
        try {
            ProfileUtil.start(TAG, "query annotations");
            c = context.getContentResolver().query(POSITION_CONTENT_URI, 
                    null, 
                    OnyxPosition.Columns.MD5 + "='" + md5 + "'", 
                    null, null);
            ProfileUtil.end(TAG, "query annotations");

            if (c == null) {
                Log.d(TAG, "query database failed");
                return false;
            }

            if (c.moveToFirst()) {
                OnyxPosition.Columns.readColumnData(c, result);
                return true;
            } else {
            	return false;
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
	}

    private static void readPositionCursor(Cursor c,
            Collection<OnyxPosition> result)
    {
        if (c.moveToFirst()) {
            result.add(OnyxPosition.Columns.readColumnData(c));

            while (c.moveToNext()) {
                if (Thread.interrupted()) {
                    return;
                }

                result.add(OnyxPosition.Columns.readColumnData(c));
            }
        }
    }
	
	public static boolean insertPosition(Context context, OnyxPosition location)
	{
        Uri result = context.getContentResolver().insert(
                POSITION_CONTENT_URI,
                OnyxPosition.Columns.createColumnData(location));
        if (result == null) {
            return false;
        }

        String id = result.getLastPathSegment();
        if (id == null) {
            return false;
        }

        location.setId(Long.parseLong(id));

        return true;
	}

	/**
     * reading data from DB to metadata, old data in metadata will be overwritten 
     * 
     * @param context
     * @param data
     * @return
     */
    public static boolean getMetadata(Context context, OnyxMetadata data)
    {
        Cursor c = null;
        try {
        	if (data.getISBN() != null) {
                c = context.getContentResolver().query(METADATA_CONTENT_URI,
                        null,
                        OnyxMetadata.Columns.ISBN + "=?",
                        new String[] { data.getISBN() }, null);
        	} else {
                c = context.getContentResolver().query(METADATA_CONTENT_URI,
                        null,
                        OnyxMetadata.Columns.NATIVE_ABSOLUTE_PATH + "=?" + " AND " +
                                OnyxMetadata.Columns.SIZE + "=" + data.getSize() + " AND " +
                                OnyxMetadata.Columns.LAST_MODIFIED + "=" + data.getLastModified().getTime(),
                        new String[] { data.getNativeAbsolutePath() }, null);
        	}
            if (c == null) {
                Log.d(TAG, "query database failed");
                return false;
            }
            if (c.moveToFirst()) {
                OnyxMetadata.Columns.readColumnData(c, data);
                return true;
            }

            return false;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    
    public static boolean insertMetadata(Context context, OnyxMetadata data)
    {
        Log.d(TAG, "insert metadata: " + data.getNativeAbsolutePath());
        
        int n = context.getContentResolver().delete(METADATA_CONTENT_URI,
                OnyxMetadata.Columns.NATIVE_ABSOLUTE_PATH + "=?",
                new String[] { data.getNativeAbsolutePath() });
        if (n > 0) {
            Log.w(TAG, "delete obsolete metadata: " + n);
        }
        
        Uri result = context.getContentResolver().insert(
                METADATA_CONTENT_URI,
                OnyxMetadata.Columns.createColumnData(data));
        if (result == null) {
            return false;
        }

        String id = result.getLastPathSegment();
        if (id == null) {
            return false;
        }

        data.setId(Long.parseLong(id));

        return true;
    }
	
	public static boolean getAggregatedData(Context context, String isbn, OnyxCmsAggregatedData data)
	{
		if (isbn == null || "".equals(isbn)) {
			return false;
		}
		
		OnyxMetadata metadata = new OnyxMetadata();
		metadata.setISBN(isbn);
		if (!OnyxSyncCenter.getMetadata(context, metadata))
		{
			return false;
		}
		
		data.setBook(metadata);
		
		String md5 = metadata.getMD5();
		List<OnyxAnnotation> annotations = new LinkedList<OnyxAnnotation>();
		if (OnyxSyncCenter.getAnnotations(context, md5, annotations)) {
			data.setAnnotations(annotations);
		} else {
			data.setAnnotations(null);
		}
		
		List<OnyxBookmark> bookmarks = new LinkedList<OnyxBookmark>();
		if (OnyxSyncCenter.getBookmarks(context, md5, bookmarks)) {
			data.setBookmark(bookmarks);
		} else {
			data.setBookmark(null);
		}
		
		OnyxPosition position = new OnyxPosition();
		if (getPosition(context, md5, position)) {
			data.setPosition(position);
		}

		return true;
	}
	
	public static boolean clear(Context context)
	{
        int count = -1;
        
        count = context.getContentResolver().delete(METADATA_CONTENT_URI, null, null);
        if (count < 0) {
            return false;
        }
        
        count = context.getContentResolver().delete(BOOKMARK_CONTENT_URI, null, null);
        if (count < 0) {
            return false;
        }
        
        count = context.getContentResolver().delete(POSITION_CONTENT_URI, null, null);
        if (count < 0) {
            return false;
        }
        
        count = context.getContentResolver().delete(ANNOTATION_CONTENT_URI, null, null);
        if (count < 0) {
            return false;
        }
        
        return true;
	}
	
	public static class CmsSync
	{
		private static boolean mergePosition(Context context, OnyxPosition position)
		{
			// TODO: NOT implement
			Log.i(TAG, "Merge position");
			return true;
		}
		
		private static boolean mergeBookmarks(Context context, List<OnyxBookmark> bookmarks)
		{
			for (OnyxBookmark bookmark : bookmarks) {
				if (bookmark.getId() == -1) {
					Log.i(TAG, "Insert bookmark");
					OnyxCmsCenter.insertBookmark(context, bookmark);
				} else {
					Log.i(TAG, "Update bookmark");
					OnyxCmsCenter.updateBookmark(context, bookmark);
				}
			}
			
			return true;
		}
		
		private static boolean mergeAnnotations(Context context, List<OnyxAnnotation> annotations)
		{
			for (OnyxAnnotation annotation : annotations) {
				Log.i(TAG, annotation.getQuote());
				
				if (annotation.getId() == -1) {
					Log.i(TAG, "Insert annotation");
					OnyxCmsCenter.insertAnnotation(context, annotation);
				} else {
					Log.i(TAG, "Update annotation");
					OnyxCmsCenter.updateAnnotation(context, annotation);
				}
			}
			
			return true;
		}
		
		private static boolean deleteBookmarks(Context context, List<OnyxBookmark> bookmarks)
		{
			for (OnyxBookmark bookmark : bookmarks) {
				Log.i(TAG, "Delete bookmark");
				OnyxCmsCenter.deleteBookmark(context, bookmark);
			}
			
			return true;
		}
		
		private static boolean deleteAnnotations(Context context, List<OnyxAnnotation> annotations)
		{
			for (OnyxAnnotation annotation : annotations) {
				Log.i(TAG, "Delete annotation");
				OnyxCmsCenter.deleteAnnotation(context, annotation);
			}
			
			return true;
		}
		
		private static boolean updateTime(Context context, OnyxMetadata metadata)
		{
			String updateTime = metadata.getExtraAttributes();
			
			if (OnyxCmsCenter.getMetadata(context, metadata)) {
				metadata.setExtraAttributes(updateTime);
				return OnyxCmsCenter.updateMetadata(context, metadata);
			}
			
			return false;
		}
		
		public static boolean mergeDiff(Context context, OnyxCmsAggregatedData updates, OnyxCmsAggregatedData removes)
		{
			boolean updated = false;
			boolean result = true;
			
			if (updates.getPosition() != null) {
				if (!mergePosition(context, updates.getPosition())) {
					result = false;
				}
				updated = true;
			}
			
			if (updates.getBookmarks() != null && updates.getBookmarks().size() > 0) {
				if (!mergeBookmarks(context, updates.getBookmarks())) {
					result = false;
				}
				updated = true;
			}
			
			if (updates.getAnnotations() != null && updates.getAnnotations().size() > 0) {
				if (!mergeAnnotations(context, updates.getAnnotations())) {
					result = false;
				}
				updated = true;
			}
			
			if (removes.getBookmarks() != null && removes.getBookmarks().size() > 0) {
				if (!deleteBookmarks(context, removes.getBookmarks())) {
					result = false;
				}
				updated = true;
			}
			
			if (removes.getAnnotations() != null && removes.getAnnotations().size() > 0) {
				if (!deleteAnnotations(context, removes.getAnnotations())) {
					result = false;
				}
				updated = true;
			}
			
			if (updated) {
				updateTime(context, updates.getBook());
			}
			
			return result;
		}
		
	}
}
