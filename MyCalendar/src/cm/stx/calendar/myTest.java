package cm.stx.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class myTest {

	public static void main(String[] args) {
		String str = "2019-05-20 00:52:30.345" ;  
        // 准备第一个模板，从字符串中提取出日期数字  
        String str1 = "yyyy-MM-dd HH:mm:ss.SSS" ;  
        // 准备第二个模板，将提取后的日期数字变为指定的格式  
        String str2 = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒" ;  
        SimpleDateFormat df1 = new SimpleDateFormat(str1) ;         
        SimpleDateFormat df2 = new SimpleDateFormat(str2) ;        
        Date d = null ;  
        try{  
        	//从字符串中提取日期格式
            d = df1.parse(str) ;   
        }catch(Exception e){            
            e.printStackTrace() ;       
        }  
        System.out.println(d);
        System.out.println(df1.format(d));
        System.out.println(df2.format(d)) ;    
	}
}
