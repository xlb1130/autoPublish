package org.xlb.publish.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 日期工具类
 * @author Lingbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class DateUtil {
	/**
	 * ȡ�õ�������
	 * */
	public static int getCurrentMonthLastDay()
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);//����������Ϊ���µ�һ��
		a.roll(Calendar.DATE, -1);//���ڻع�һ�죬Ҳ�������һ��
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * �õ�ָ���µ�����
	 * */
	public static int getMonthLastDay(int year, int month)
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);//����������Ϊ���µ�һ��
		a.roll(Calendar.DATE, -1);//���ڻع�һ�죬Ҳ�������һ��
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
	
	/**
	 * ��ȡʱ���������
	 * @param a yyyy-MM-dd ��ʼʱ��
	 * @param b yyyy-MM-dd  ����ʱ��
	 * @return
	 * @throws ParseException
	 */
	public static long getBetweenDayCount(String a, String b) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Long c = sf.parse(b).getTime()-sf.parse(a).getTime();
		return c/1000/60/60/24;
	}
	
	/**
	 * ��ȡʱ���������
	 * @param a String yyyy-MM-dd ��ʼʱ��
	 * @param b Date  ����ʱ��
	 * @return
	 * @throws ParseException
	 */
	public static long getBetweenDayCount(String a, Date b) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Long c = b.getTime()-sf.parse(a).getTime();
		return c/1000/60/60/24;
	}
	
	/**
	 * ��ȡ�ϸ��¿�ʼʱ��
	 * @param pattern
	 * @return
	 */
	public static String getBeginDate(String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(pattern);
		Calendar cal =Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH,1);
		String date = sdf.format(cal.getTime());
		return date;
	}
	
	/**
	 * ��ȡ�ϸ��½���ʱ��
	 * @param pattern
	 * @return
	 */
	public  static String getEndDate(String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(pattern);
		Calendar cal =Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,0);
		String date = sdf.format(cal.getTime());
		return date;
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 * @param pattern
	 * @return
	 */
	public  static String getCurrentDate(String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(pattern);
		Calendar cal =Calendar.getInstance();
		String date = sdf.format(cal.getTime());
		return date;
	}
}
