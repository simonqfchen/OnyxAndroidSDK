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
	
	private static OnyxHistoryEntry sHistoryEntry = new OnyxHistoryEntry();
	
	public static void recordStartReading(Context context , String md5)
	{
		sHistoryEntry.setStartTime(new Date());
		sHistoryEntry.setEndTime(new Date());
		sHistoryEntry.setMD5(md5);
		OnyxCmsCenter.insertHistory(context, sHistoryEntry);
		Log.d(Tag, "insert id is " + sHistoryEntry.getId());
		Log.d(Tag, "insert MD5 is " + sHistoryEntry.getMD5());
	}
	
	public static void recordFinishReading(Context context)
	{
		sHistoryEntry.setEndTime(new Date());
		OnyxCmsCenter.updateHistory(context, sHistoryEntry);
		Log.d(Tag, "update id is " + sHistoryEntry.getId());
		Log.d(Tag, "update MD5 is " + sHistoryEntry.getMD5());
	}
	
	public static List<OnyxHistoryEntry> getHistorysByMD5(Context context, String md5)
	{
		return OnyxCmsCenter.getHistorysByMD5(context, md5);
	}
}
