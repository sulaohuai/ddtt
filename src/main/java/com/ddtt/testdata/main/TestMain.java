package com.ddtt.testdata.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Date cutOffDt =  null;
		List pubHolidayList = new ArrayList();//dao.getPubHoliday(conn);
		
		pubHolidayList.add("2014-12-10");
		pubHolidayList.add("2014-11-30");
		
		//Date today =  minusDay(new Date(),4);;
		//PublicHolidayTO pubHolidayTO = new PublicHolidayTO();
		
		Date currentDay = new Date();
		int count = 1;
		for(int a =0;a<10;a++){
			boolean isSunday = false;
			if(7==dayForWeek(currentDay)){
				System.out.println(dft.format(currentDay) + " is Sunday");
				currentDay = minusDay(currentDay,1);
				isSunday = true;
			}else{
				System.out.println(dft.format(currentDay) + " is not Sunday");
			}
			
			if(isSunday == true){
				continue;
			}
			
			if (count == 5){
				cutOffDt = currentDay;
				break;
			}
			if(pubHolidayList.size()==0){
				System.out.println(dft.format(currentDay) + " is not Sunday or Holiday");
				currentDay = minusDay(currentDay,1);
				count ++;	
			}else{
				boolean isHoliday = false;
				for(int y=0;y<pubHolidayList.size();y++){
					if(dft.format(currentDay).equals(pubHolidayList.get(y))){
						isHoliday = true;
						break;
					}
				}
				if(isHoliday == true){
					System.out.println(dft.format(currentDay) + " is Holiday");
				}else{
					System.out.println(dft.format(currentDay) + " is not Sunday or Holiday");
					count++;
				}
				
				currentDay = minusDay(currentDay,1);
			}
//			System.out.println(currentDay +"---------"+count);
		}
		
		System.out.println("Result is " + dft.format(cutOffDt));

	}
	
	
	public static Date minusDay (Date originalDate, int days){
		Date targetDate = null;
		Calendar date = Calendar.getInstance();
		date.setTime(originalDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - days);
		targetDate = date.getTime();
		
		return targetDate;
	}
	
	public static int dayForWeek(Date date) throws Exception {
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();
		  c.setTime(date);
		  int dayForWeek = 0;
		  if(c.get(Calendar.DAY_OF_WEEK) == 1){
		   dayForWeek = 7;
		  }else{
		   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  }
		  return dayForWeek;
	}

}

