/**
 * 
 */
package com.onyx.android.sdk.data.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateFormat;

import com.onyx.android.sdk.R;

/**
 * @author joy
 *
 */
public class DateTimeUtil
{
    public static final SimpleDateFormat DATE_FORMAT_YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_YYYYMMDD_HHMM = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    
    /**
     * format the time according to the current locale and the user's 12-/24-hour clock preference
     * 
     * @param context
     * @return
     */
    public static String getCurrentTimeString(Context context) {
        return DateFormat.getTimeFormat(context).format(new Date());
    }
    
    public static String formatDate(Date endTime) {
		return DATE_FORMAT_YYYYMMDD_HHMMSS.format(endTime);
	}
    
    public static String formatTime(Context context, int all_second) {
		int hour_value = all_second / 3600;
		int minute_value = all_second % 3600 / 60;
		int second_value = all_second % 3600 % 60;
		
		String hour_symbol = context.getResources().getString(R.string.hour_symbol);
		String minute_symbol = context.getResources().getString(R.string.minute_symbol);
		String second_symbol = context.getResources().getString(R.string.second_symbol);
		
		if (hour_value > 0) {
			return hour_value + hour_symbol + minute_value + minute_symbol;
		} else if (minute_value > 0) {
			return minute_value + minute_symbol + second_value + second_symbol;
		} else {
			return second_value + second_symbol;
		}
	}
}
