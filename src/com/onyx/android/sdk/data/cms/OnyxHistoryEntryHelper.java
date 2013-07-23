/**
 * 
 */
package com.onyx.android.sdk.data.cms;

import java.util.Date;

import android.content.Context;
import android.util.Log;

/**
 * @author peekaboo
 *
 */
public class OnyxHistoryEntryHelper 
{
	private static final String Tag = "OnyxHistoryEntryHelper";
	
	private static OnyxHistoryEntry sHistoryEntry = new OnyxHistoryEntry();
	
	public static void recordStartReading(Context context)
	{
		sHistoryEntry.setStartTime(new Date());
		OnyxCmsCenter.insertHistory(context, sHistoryEntry);
		Log.d(Tag, "insert id is " + sHistoryEntry.getId());
	}
	
	public static void recordFinishReading(Context context)
	{
		sHistoryEntry.setEndTime(new Date());
		OnyxCmsCenter.updateHistory(context, sHistoryEntry);
		Log.d(Tag, "update id is " + sHistoryEntry.getId());
	}

}
