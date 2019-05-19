package cm.stx.calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class TestCalendar {

	public static void main(String[] args)  {
				
		System.out.println("请输入日期（格式：2019-05-20）：");
		Scanner input = new Scanner(System.in);
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date date = null;
		try {
			date = df.parse(input.nextLine());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		int dayInput = c.get(Calendar.DAY_OF_MONTH);
		int days = c.getActualMaximum(Calendar.DATE);
		
		System.out.println("一\t二\t三\t四\t五\t六\t日");
		
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		for(int i=1;i<c.get(Calendar.DAY_OF_WEEK);i++) {
			System.out.print("\t");
		}
	    
		
		for(int j=1;j<days;j++) {
			
			if(dayInput == c.get(Calendar.DAY_OF_MONTH)) {
				System.out.print(c.get(Calendar.DAY_OF_MONTH) +"*\t");
			}else {
				System.out.print(c.get(Calendar.DAY_OF_MONTH) +"\t");
			}
			
			
			if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
				System.out.println();
			}
			
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
}
