/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

/**
 * @author peekaboo
 *
 */
public class OnyxHistoryEntryHelper 
{
	private static final String Tag = "OnyxHistoryEntryHelper";
    private static final boolean VERBOSE_LOG = false;
	
	private static OnyxHistoryEntry sHistoryEntry = null;
	
	public static void recordStartReading(Context context, String md5, OnyxBookProgress bookProgress)
	{
	    sHistoryEntry = new OnyxHistoryEntry();
	    
		sHistoryEntry.setStartTime(new Date());
		sHistoryEntry.setEndTime(new Date());
		sHistoryEntry.setProgress(bookProgress);
		sHistoryEntry.setMD5(md5);
		OnyxCmsCenter.insertHistory(context, sHistoryEntry);
		if (VERBOSE_LOG) Log.d(Tag, "insert: " + sHistoryEntry.getId() + ", " + sHistoryEntry.getMD5());
	}
	
	public static void recordFinishReading(Context context, OnyxBookProgress bookProgress)
	{
	    if (sHistoryEntry == null) {
	        return;
	    }

		Date old_end_time = sHistoryEntry.getEndTime();
		Date new_end_time = new Date();
		int durationTime = 10;  // minutes
		if ((new_end_time.getTime() - old_end_time.getTime()) >= durationTime * 60 * 1000) {
			Log.d(Tag, "Read idle for " + durationTime +" minutes timeout");
			recordStartReading(context, sHistoryEntry.getMD5(), bookProgress);
			return;
		}

		sHistoryEntry.setEndTime(new_end_time);
		sHistoryEntry.setProgress(bookProgress);
		OnyxCmsCenter.updateHistory(context, sHistoryEntry);
		if (VERBOSE_LOG) Log.d(Tag, "update: " + sHistoryEntry.getId() + ", " + sHistoryEntry.getMD5());
	}
	
	public static List<OnyxHistoryEntry> getHistorysByMD5(Context context, String md5)
	{
		return OnyxCmsCenter.getHistorysByMD5(context, md5);
	}

	public static boolean deleteHistoryByMD5(Context context, String md5) {
		return OnyxCmsCenter.deleteHistoryByMD5(context, md5);
	}
}
