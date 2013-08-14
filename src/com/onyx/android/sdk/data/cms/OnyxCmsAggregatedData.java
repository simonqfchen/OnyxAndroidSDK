/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author Simon
 *
 */
public class OnyxCmsAggregatedData implements Parcelable 
{
	private static final String TAG = "OnyxCmsAggregatedData";
	
	private OnyxMetadata mBook = null;
	//private OnyxBookProgress mProgress = null;
	private OnyxPosition mPosition = null;
	private List<OnyxBookmark> mBookmarks = null;
	private List<OnyxAnnotation> mAnnotations = null;
	
	public OnyxCmsAggregatedData(OnyxMetadata book, OnyxPosition position, 
		List<OnyxBookmark> bookmarks, List<OnyxAnnotation> annotations)
	{
		mBook = book;
		mPosition = position;
		mBookmarks = bookmarks;
		mAnnotations = annotations;
	}
	
	public OnyxCmsAggregatedData(Parcel source)
	{
		mBook = source.readParcelable(OnyxMetadata.class.getClassLoader());
		mPosition = source.readParcelable(OnyxPosition.class.getClassLoader());
		mBookmarks = new LinkedList<OnyxBookmark>();
		source.readTypedList(mBookmarks, OnyxBookmark.CREATOR);
		mAnnotations = new LinkedList<OnyxAnnotation>();
		source.readTypedList(mAnnotations, OnyxAnnotation.CREATOR);
	}
	
	public OnyxCmsAggregatedData()
	{
		
	}
	
	/**
	 * 
	 * @return data of the book
	 */
	public OnyxMetadata getBook()
	{
		return mBook;
	}
	
	public void setBook(OnyxMetadata metadata)
	{
		mBook = metadata;
	}
	
	/**
	 * 
	 * @return null standing for nothing
	 */
/*	public OnyxBookProgress getProgress()
	{
		return mProgress;
	}*/
	
	public OnyxPosition getPosition()
	{
		return mPosition;
	}
	
	public void setPosition(OnyxPosition position)
	{
		mPosition = position;
	}
	
	/**
	 * 
	 * @return null standing for nothing
	 */
	public List<OnyxBookmark> getBookmarks()
	{
		return mBookmarks;
	}
	
	public void setBookmark(List<OnyxBookmark> bookmarks)
	{
		mBookmarks = bookmarks;
	}
	
	/**
	 * 
	 * @return null standing for nothing
	 */
	public List<OnyxAnnotation> getAnnotations()
	{
		return mAnnotations;
	}
	
	public void setAnnotations(List<OnyxAnnotation> annotations)
	{
		mAnnotations = annotations;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeParcelable(mBook, flags);
		dest.writeParcelable(mPosition, flags);
		dest.writeTypedList(mBookmarks);
		dest.writeTypedList(mAnnotations);
/*
		if (mBook == null) {
			dest.writeParcelable(new OnyxMetadata(), flags);
		} else {
			dest.writeParcelable(mBook, flags);
		}

		if (mPosition == null) {
			dest.writeParcelable(new OnyxPosition(), flags);
		} else {
			dest.writeParcelable(mPosition, flags);
		}
		
		if (mBookmarks == null) {
			dest.writeTypedList(new LinkedList<OnyxBookmark>());
		} else {
			dest.writeTypedList(mBookmarks);
		}
		
		if (mAnnotations == null) {
			dest.writeTypedList(new LinkedList<OnyxAnnotation>());
		} else {
			dest.writeTypedList(mAnnotations);
		}
		*/
	}
	
	public void readFromParcel(Parcel source)
	{
		mBook = source.readParcelable(OnyxMetadata.class.getClassLoader());
		mPosition = source.readParcelable(OnyxPosition.class.getClassLoader());
		mBookmarks = new LinkedList<OnyxBookmark>();
		source.readTypedList(mBookmarks, OnyxBookmark.CREATOR);
		mAnnotations = new LinkedList<OnyxAnnotation>();
		source.readTypedList(mAnnotations, OnyxAnnotation.CREATOR);
	}
	
	public static final Parcelable.Creator<OnyxCmsAggregatedData> CREATOR 
				= new Parcelable.Creator<OnyxCmsAggregatedData>() 
	{

		@Override
		public OnyxCmsAggregatedData createFromParcel(Parcel source) 
		{
			Log.i(TAG, "Create aggregated data from parcel!");
			return new OnyxCmsAggregatedData(source);
		}

		@Override
		public OnyxCmsAggregatedData[] newArray(int size) 
		{
			return new OnyxCmsAggregatedData[size];
		}
		
	};

	public boolean getAggregatedDataByISBN(Context context, String isbn)
	{
		OnyxMetadata metadata = new OnyxMetadata();
		metadata.setISBN(isbn);
		if (!OnyxCmsCenter.getMetadata(context, metadata)) {
			return false;
		}
		mBook = metadata;
		
		String md5 = mBook.getMD5();
		
		List<OnyxAnnotation> annotations = new LinkedList<OnyxAnnotation>();
		if (OnyxCmsCenter.getAnnotations(context, md5, annotations)) {
			mAnnotations = annotations;
		} else {
			mAnnotations = null;
		}
		
		List<OnyxBookmark> bookmarks = new LinkedList<OnyxBookmark>();
		if (OnyxCmsCenter.getBookmarks(context, md5, bookmarks)) {
			mBookmarks = bookmarks;
		} else {
			mBookmarks = null;
		}
		
		// TODO: get OnyxPosition here
/*
		OnyxPosition position = new OnyxPosition();
		mPosition = position;
*/		
		return true;
	}
	
}
