package net.mooctest;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class SuffixArrayTest {

	@Test
	public void test() throws ClassNotFoundException,
	NoSuchMethodException, SecurityException, 
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String str = "missississi";
		int sa[]={0,1,2,3,4,5,6,7,8,9,10},LCP[]=new int[11];
		
//		SuffixArray suf = new SuffixArray();
		SuffixArray.createSuffixArray(str, sa, LCP);
		SuffixArray.createSuffixArraySlow(str, sa, LCP);
		
		str = "TestData";
		int sb[]={0,1,2,3,4,5,6,7},LCP1[]=new int[8];
		
//		SuffixArray suf = new SuffixArray();
		SuffixArray.createSuffixArray(str, sb, LCP1);
		SuffixArray.createSuffixArraySlow(str, sb, LCP1);
		
		
		SuffixArray.isSorted(new int[]{1,0,2}, new int[]{1,2,3}, 3);
		SuffixArray.isSorted(new int[]{0,1,2}, new int[]{1,2,3}, 3);
		SuffixArray.sleq(new int[]{0,1,2}, 0, new int[]{0,2,1}, 0);
		
		SuffixArray.computeLCP("123", "12");
		
		SuffixArray.isPermutation(new int[]{0,1,2}, 3);
		SuffixArray.isPermutation(new int[]{0,1,1}, 3);
		
		SuffixArray.printV(new int[]{0,1,1}, "123");
		
		Class cls=Class.forName(new SuffixArray().getClass().getName());
		SuffixArray suf=(SuffixArray) cls.newInstance();
		Method leq1 = cls.getDeclaredMethod("leq",
				new Class[]{int.class,int.class,int.class,int.class});
		leq1.setAccessible(true);
		leq1.invoke(suf, new Object[]{1,3,2,2});
		leq1.invoke(suf, new Object[]{1,2,1,2});
		leq1.invoke(suf, new Object[]{1,3,1,2});
		
		Method leq2 = cls.getDeclaredMethod("leq",
				new Class[]{int.class,int.class,
				int.class,int.class,int.class,int.class});
		leq2.setAccessible(true);
		leq2.invoke(suf, new Object[]{1,2,3,2,2,3});
		leq2.invoke(suf, new Object[]{1,2,3,1,3,3});
		leq2.invoke(suf, new Object[]{1,3,3,1,2,2});
		leq2.invoke(suf, new Object[]{2,2,3,1,2,3});
		
		Method merge = cls.getDeclaredMethod("merge",
				new Class[]{int[].class,int[].class,
				int[].class,int[].class,int[].class,
				int.class,int.class,int.class,int.class});
		merge.setAccessible(true);
		merge.invoke(suf, new Object[]{new int[]{0,1,2},new int[]{0,1,2},
				new int[]{0,1,2},new int[]{0,1,2},new int[]{0,1,2},
				3,0,0,3});
		merge.invoke(suf, new Object[]{new int[]{0,1,2},new int[]{0,1,2},
				new int[]{0,1,2},new int[]{0,1,2},new int[]{0,1,2},
				3,0,1,0});
		
		Method computeS12 = cls.getDeclaredMethod("computeS12",
				new Class[]{int[].class,int[].class,
				int.class,int.class});
		computeS12.setAccessible(true);
		computeS12.invoke(suf, new Object[]{new int[]{0,1,2,3,4,5},
				new int[]{0,1,2,3,4,5},3,256});
		
		Method assignNames = cls.getDeclaredMethod("assignNames",
				new Class[]{int[].class,int[].class,int[].class,
				int.class,int.class,int.class});
		assignNames.setAccessible(true);
		assignNames.invoke(suf, new Object[]{new int[]{1,1,0,2,1,1},
				new int[]{0,0,3,0,0,0},new int[]{4,3,3,0,0,0},2,2,4});
		
		
		
//		SuffixArray
		
		try{
			SuffixArray.createSuffixArraySlow(str, new int[7], LCP);
		}catch(IllegalArgumentException ex){
			
		}
		try{
			SuffixArray.createSuffixArraySlow(str, sb, new int[7]);
		}catch(IllegalArgumentException ex){
			
		}
	}

}
