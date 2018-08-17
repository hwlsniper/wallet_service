package it.etoken.base.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static String forSecond(long time){
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		return df.format(new Date(time));
	}
	
	public static String forDay(long time){
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		return df.format(new Date(time));
	}
	
	//获取utc时间戳
	public static long getUtcTimes() {
		Calendar cal = Calendar.getInstance();
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        long mills = cal.getTimeInMillis();
		return mills;
	}
	
	//获取utc时间戳
	public static Date getUtcDate() {
		Calendar cal = Calendar.getInstance();
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        long mills = cal.getTimeInMillis();
        return new Date(mills);
	}
}
