/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import java.util.Collection;

import android.content.Context;

/**
 * @author joy
 *
 */
public class OnyxCmsRemote 
{
	public static String ACTION_REMOTE_CMS_DATA_DOWNLOADING = "com.onyx.android.intent.action.ACTION_LOCAL_CMS_DATA_UPLOADING";
	public static String ACTION_LOCAL_CMS_DATA_UPLOADING = "com.onyx.android.intent.action.ACTION_LOCAL_CMS_DATA_UPLOADING";
	
	public static String CMS_DATA_DIFF_CONTENT_URI = "";
	public static String CMS_DATA_COMPLETE_CONTENT_URI = "";
	
	public static String ACTION_REMOTE_CMS_DATA_DOWNLOADED = "com.onyx.android.intent.action.ACTION_LOCAL_CMS_DATA_UPLOADED";
	public static String ACTION_LOCAL_CMS_DATA_UPLOADED = "com.onyx.android.intent.action.ACTION_LOCAL_CMS_DATA_UPLOADED";
	
	public static class CmsAggregatedData 
	{
		private OnyxMetadata mBook = null;
		private OnyxBookProgress mProgress = null;
		private Collection<OnyxBookmark> mBookmarks = null;
		private Collection<OnyxAnnotation> mAnnotations = null;
		
		public CmsAggregatedData(OnyxMetadata book, OnyxBookProgress progress, 
			Collection<OnyxBookmark> bookmarks, Collection<OnyxAnnotation> annotations)
		{
			mBook = book;
			mProgress = progress;
			mBookmarks = bookmarks;
			mAnnotations = annotations;
		}
		
		/**
		 * 
		 * @return data of the book
		 */
		public OnyxMetadata getBook()
		{
			return mBook;
		}
		
		/**
		 * 
		 * @return null standing for nothing
		 */
		public OnyxBookProgress getProgress()
		{
			return mProgress;
		}
		
		/**
		 * 
		 * @return null standing for nothing
		 */
		public Collection<OnyxBookmark> getBookmarks()
		{
			return mBookmarks;
		}
		
		/**
		 * 
		 * @return null standing for nothing
		 */
		public Collection<OnyxAnnotation> getAnnotations()
		{
			return mAnnotations;
		}
	}
	
	public static boolean getDownloadedData(Context context, Collection<CmsAggregatedData> data)
	{
		return false;
	}
	
	public static boolean getUploadingData(Context context, Collection<CmsAggregatedData> data)
	{
		return false;
	}
	
	public static boolean downloadRemoteData(Context context)
	{
		return false;
	}
	
	public static boolean notifyRemoteDataDownloaded(Context context, boolean succ, Collection<CmsAggregatedData> data, int errCode, String errMsg)
	{
		return false;
	}
	
	public static boolean uploadLocalData(Context context, Collection<CmsAggregatedData> data)
	{
		return false;
	}
	
	public static boolean notifyLocalDataUploaded(Context context, boolean succ, int errCode, String errMsg)
	{
		return false;
	}
}
