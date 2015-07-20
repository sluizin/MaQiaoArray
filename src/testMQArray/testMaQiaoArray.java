package testMQArray;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import MaQiao.MaQiaoArray.Consts.MathOperation;
import MaQiao.MaQiaoArray.Consts.NumberType;
import MaQiao.MaQiaoArray.MQArrayChar;
import MaQiao.MaQiaoArray.UtilSuffix;
@SuppressWarnings({ "unused" })
public class testMaQiaoArray {
	@Test
	public void test1() {
//		SLinked SLinked=new SLinked(JsonString.toCharArray());
//		SLinked.Split();
//		SLinked.Result();
		char[] Arraynull = new char[0];
		char[] ArraySecond = {'e','f','f'};
		char[] Arraythree = {'a','g','a','i','i','a','i','a'};
		char[] Arrayfour = {'f','f','f'};
		char[] Arraythree22 = {'a'};
		String[] ArrayStringInt = {"10","5","51","2","0","4"};
		int[] ArrayInt={1,5,0,1,2,9,5,2,5,15,2,1,4,2};
		//              0 1 2 3 4 5 6 7 8  9 10 11
		char c='o';
		char[] Array1 = {'f','o','x','s'};
		String ArrString = "a中bozoabppa中babxoa3abbcza中bakltal0nnqaba中bz国acolaala中bzoaoboa中bdef";
		String ArrString12 = "abczabcaabaabczabcabc";
		//System.out.println("len:"+ArrString12.length());
		//System.out.println(MQArrayChar.toString(MQArrayChar.folio("ab".toCharArray(),true)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.folio("ABC$123".toCharArray(),true)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.folio("ABC$123".toCharArray(),false)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.folio("ab".toCharArray(),false)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.folio("abcgdef".toCharArray(),false)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.exChange("abcdefghijlmnopq".toCharArray(),2,3,7,3)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.exChange("abXYZfg123lmnopq".toCharArray(),2,7,3)));
		//System.out.println();
		String oldArrString ="bzzabaccbddabbzb";
		String newArrString ="XXXXXX";
		//System.out.println(MQArrayChar.toString(MQArrayChar.fillCycle("abc123def".toCharArray(), 2,9,"XYZT".toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.fillCycle("abc123def".toCharArray(), 2,3,false,"XYZT".toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.copyOf("abc123def".toCharArray(), 2)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.copyOf("abc123d".toCharArray(),2,4, 3)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.disOrganize("abcd0123".toCharArray())));
		String abc="abcde0123456";
		//          01  
		/*
		 * a b i a c d e f g i  h  i  a  j  k
		 * 0 1 2 3 4 5 6 7 8 9  10 11 12 13 14
		 */
		//System.out.println(MQArrayChar.indexOfLast("abiacdefgihiajk".toCharArray(),"ia".toCharArray(),13,2));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArray$2_1(abc.toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArray$2_2(abc.toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArray$Part("abiacdefgihiajkac".toCharArray(),5,1)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArray$Part("abiacdefgihiajkac".toCharArray(),5,2)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArray$Part("abiacdefgihiajkac".toCharArray(),5,3)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArray$Part("abiacdefgihiajkac".toCharArray(),5,4)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArray$Part("abiacdefgihiajkac".toCharArray(),5,5)));
/*		{
			char[][] kkk = MQArrayChar.page("abcdefghi".toCharArray(),3);
			for(int i=0;i<kkk.length;i++){
				if(kkk[i].length == 0){
					System.out.println("null");					
				}else{
					System.out.println(MQArrayChar.toString(kkk[i]));
				}
			}	
		}*/
		{
			String strarray="ABCDEFGHIJKLMNOPQ";
			System.out.println(MQArrayChar.toString(MQArrayChar.clockStaggered(strarray.toCharArray(),true)));
		}
/*		{
			int maxrows=20;
			for(int iii=1;iii<=maxrows;iii++){
				System.out.print("[");
				if(iii<10)System.out.print("0");
				System.out.print(iii);
				System.out.print("]");
				{
					int[] array=UtilSuffix.ClockStaggered(iii,true);
					for(int i=0;i<array.length;i++){
						if(i>0)System.out.print("\t");
						System.out.print(array[i]);
					}
				}
				System.out.println();
			}
		}*/
		//System.out.println(MQArrayChar.toString(MQArrayChar.clearRepeat("abcd01c0ee21e3d".toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.clearRepeat("aXcdYefXgXhYijZYZ".toCharArray(),"XZ".toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.clearRepeat("aXcdYefXgXhYijZYZ".toCharArray(),"CD".toCharArray())));
		//System.out.println(MQArrayChar.interSectionCount("abcdef".toCharArray(),"dabc".toCharArray(),"gcha".toCharArray(),"1a2c3".toCharArray()));

		//System.out.println(MQArrayChar.toString(MQArrayChar.sectionUnion("abc1def".toCharArray(),"dcab1".toCharArray(),"1gchla".toCharArray(),"1cg9ha".toCharArray(),"1ac2c3".toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.sectionDifference("abc1def".toCharArray(),"dcab1".toCharArray(),"1gchka".toCharArray(),"1cg9ha".toCharArray(),"1ac2c3".toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.sectionInterDeprecated("abc1def".toCharArray(),"dcab1".toCharArray(),"1gcdhla".toCharArray(),"1dcgha".toCharArray(),"1ac2cd3".toCharArray())));
		
		//System.out.println(MQArrayChar.toString(MQArrayChar.sectionInter("abc1def".toCharArray(),"dcab1".toCharArray(),"1gchla".toCharArray(),"1cgha".toCharArray(),"1ac2c3".toCharArray())));
		/*		{
			for(int i=-20;i<21;i++){
				if(i==0)System.out.println();
				System.out.print(MQArrayChar.selectCycleArray("01234567".toCharArray(), i));
			}
			System.out.println();
		}*/
/*		{
			char[][] Array = {"X".toCharArray(),"AB".toCharArray(),"XY".toCharArray()};
			char[][] kkk = MQArrayChar.split("abcXYefXYTnTABcXYg".toCharArray(), Array);
			for(int i=0;i<kkk.length;i++){
				if(kkk[i].length == 0){
					System.out.println("null");					
				}else{
					System.out.println(MQArrayChar.toString(kkk[i]));
				}
			}	
		}*/
/*		{
			//char[][] Array = {"X".toCharArray(),"AB".toCharArray(),"XY".toCharArray()};
			byte[][] kkk = MQArrayChar.toArrayByte("a1国".toCharArray());
			for(int i=0;i<kkk.length;i++){
				for(int ii=0;ii<kkk[i].length;ii++)
					System.out.println(Integer.toBinaryString(kkk[i][ii]));
				System.out.println("result:"+getChar(kkk[i]));
			}	
		}*/
		List<Integer> a =new ArrayList<Integer>();
		a.add(new Integer(4));
		//System.out.println(MQArrayChar.maxIndexof("YXabgdfceh123z".toCharArray()));
		//System.out.println(MQArrayChar.minIndexof("YXabgdfceh123z".toCharArray()));
		//System.out.println(MQArrayChar.startsWith("abcdefgh".toCharArray(), "abc".toCharArray()));
		//System.out.println(MQArrayChar.endsWith("abcdefgh".toCharArray(), "h".toCharArray()));
		//System.out.println(MQArrayChar.isExistsLowerCase("ABaCE".toCharArray()));
		//System.out.println(MQArrayChar.isExistsUpperCase("abbEc".toCharArray()));
		//System.out.println(MQArrayChar.toString(MQArrayChar.left("abcdefgh".toCharArray(), 3)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.right("abcdefgh".toCharArray(), 10)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.sort("abgdfceh".toCharArray(), 2, 5, true)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.sort("abgdfceh".toCharArray(), 2, 5, false)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.sort("abgdfcehe3xs4".toCharArray(), true)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.sort("abgdfcehe3xs4".toCharArray(), false)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.deleteRepeat("abcd01c0ee21e3d".toCharArray())));
		//System.out.println(MQArrayChar.toString(MQArrayChar.subArrayForced("abcdefgh".toCharArray(),4, 5)));
		//System.out.println(MQArrayChar.IndexOf("abcdXYefgh".toCharArray(),"YX".toCharArray()));
		//char[][] ckc={"AB".toCharArray(),"YX".toCharArray()};
		//System.out.println(MQArrayChar.IndexOf("aBAbcdXYefgh".toCharArray(),ckc));
//		System.out.println(oldArrString);
/*		{
			char[][] kk=MQArrayChar.split(oldArrString.toCharArray(), 'b');
			for(int i=0;i<kk.length;i++){
				if(kk[i].length == 0){
					System.out.println("null");					
				}else{
					System.out.println(MQArrayChar.toString(kk[i]));
				}
			}
		}
*//*		{
		char[][] kk=MQArrayChar.split("XYabcXYefXYXYg".toCharArray(), "XY".toCharArray());
		for(int i=0;i<kk.length;i++){
			if(kk[i].length == 0){
				System.out.println("null");					
			}else{
				System.out.println(MQArrayChar.toString(kk[i]));
			}
		}
	}*//*{
		char[][] Array = {"X".toCharArray(),"AB".toCharArray(),"XY".toCharArray(),"cd".toCharArray()};
		char[][] kk=MQArrayChar.reverse(Array);
		for(int i=0;i<kk.length;i++){
			if(kk[i].length == 0){
				System.out.println("null");					
			}else{
				System.out.println(MQArrayChar.toString(kk[i]));
			}
		}
		}*/
		char[] arrayA={'a','b','c'};
		char[] arrayB={'1','2'};
		char[] arrayC={'X','Y'};
		char[] arrayD={};
		//System.out.println(MQArrayChar.toString(MQArrayChar.concat(arrayA,arrayB,arrayD,arrayC)));
		//char[][] arrayAA={arrayB,arrayA};
		//char[][] arrayBB={arrayC,arrayD,arrayA};
		//System.out.println(MQArrayChar.toString(MQArrayChar.concat(arrayAA,arrayBB)));
		//char[] array="0123456789abcdefghijlmnopqrstuvwxyz".toCharArray();;
		char[] array="0123456789abcdefghijklmn".toCharArray();
		//System.out.println(MQArrayChar.toString(MQArrayChar.selectNumberPrime(array)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.selectSuffixNumber(NumberType.Prime,array)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.selectSuffixNumber(NumberType.Odd,array)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.selectSuffixNumber(NumberType.Even,array)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.Math_Add(-1,array)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.Math_Operation(MathOperation.Mod,3,array)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.selectPrimeNumber(array)));
		//System.out.println(MQArrayChar.replaceAll(oldArrString.toCharArray(),'b',Array1));
		//System.out.println(MQArrayChar.repeatGroupSort("aXcdYefXgCCXaahYijZYZ".toCharArray()));
		//System.out.println(MQArrayChar.repeatGroupCount("aXcdYefXgXhYijZYZ".toCharArray()));
		//System.out.println(MQArrayChar.concat('z',ArrString.toCharArray()));
		//System.out.println(MQArrayChar.toString(MQArrayChar.ltrim(ArraySource)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.rtrim(ArraySource)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.trim(ArraySource)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.concat(ArraySource,ArraySource)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.concat('p',ArraySource)));
		//System.out.println(MQArrayChar.toString(MQArrayChar.concat('p',ArraySource)));
		//System.out.println(MQArrayChar.replaceAll(oldArrString.toCharArray(),'z','X'));
		//System.out.println(ArrString);
		//System.out.print("removeAll:");
		//System.out.println(MQArrayChar.removeAll("XYZabcXZYdefXYZgXYZ".toCharArray(),"XYZ".toCharArray()));
		//System.out.println(MQArrayChar.shift("abcdefgh".toCharArray(),9,2));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),2,3));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),2,0,5,'X'));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),2,2,5,'X'));
		
		//System.out.println("abcdefghjklmnopqrest");
		//System.out.println("0123456789");
		//System.out.println(MQArrayChar.shiftRoll("abcdefghjklmnopqrst".toCharArray(),-2,3,6));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),2));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),-2));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),-2,8,5,'X'));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),-2,2,5,'X'));
		//System.out.println(MQArrayChar.shift("abcdefghjklmnopqrest".toCharArray(),4,12,6,'X'));
		//System.out.println(MQArrayChar.shift("abcdefgh".toCharArray(),-2,3));
		//System.out.println(MQArrayChar.repeatCount("aXcdYefXgXhYijY".toCharArray()));
		//System.out.println(MQArrayChar.repeatCount(ArrayInt));
		//System.out.println(MQArrayChar.concat('c',"A".toCharArray(),"BC".toCharArray(),"".toCharArray()));
		//System.out.println(MQArrayChar.replace(ArrString.toCharArray(),oldArrString.toCharArray(),'中'));
		//System.out.println(MQArrayChar.replaceAll(ArrString12.toCharArray(),oldArrString.toCharArray(),'X'));
		//System.out.println(MQArrayChar.searchCount(ArrString.toCharArray(),'z'));
		//System.out.println(MQArrayChar.concat(c,c));
		//System.out.println(MQArrayChar.indexOf(ArrString.toCharArray(),oldArrString.toCharArray(),2));
		//System.out.println(MQArrayChar.reverse(ArrString.toCharArray()));
		//System.out.println(MQArrayChar.reverse("abcdefghi".toCharArray()));
		//System.out.println(MQArrayChar.reverse(newArrString.toCharArray()));
		//System.out.println(MQArrayChar.removeAll(ArrString.toCharArray(),c));
		//System.out.println(MQArrayChar.removeAll(ArrString.toCharArray(),oldArrString.toCharArray()));
		//System.out.println(MQArrayChar.PrivateArrayFindArray(ArrString.toCharArray(),oldArrString.toCharArray(),40));
		//System.out.println(MQArrayChar.getLenMaxArray(Arrayfour,Arraythree,ArrString.toCharArray()));
		//System.out.println(MQArrayChar.getLenMinArray(Arrayfour,Arraythree,ArrString.toCharArray(),Array1));
		//System.out.println(MQArrayChar.clone(ArrString.toCharArray()));
		//System.out.println(MQArrayChar.isSameLength(ArraySecond,Arrayfour,Arraythree22));
	   //char[] Arraythree33 = {'a','g','a','i','i','a','i','a'};
		//						0   1   2   3   4   5   6   7
		//System.out.println(MQArrayChar.indexOfLast(Arraythree33,'a',4,9));
		//char[] Arraythree1 = {'a','g','a','i','i','a','i','a'};
		String oldArrString1 ="ia";
		String oldArrString2 ="abiacdefgihiajk";
		//System.out.println(MQArrayChar.insert("abc".toCharArray(),3,'K'));
		//System.out.println(MQArrayChar.Recom("abcdefghijklmn".toCharArray(),2,3,4,3,10,12,20,1));
		//System.out.println(MQArrayChar.RecomCompulsory("abcdefghijklmn".toCharArray(),1,-1,0,90,100,0,2,60,0,3,4,3,10,102,2,20,1,100,-1,0));
		//System.out.println(MQArrayChar.remove(oldArrString2.toCharArray(),2,3));
		//System.out.println(MQArrayChar.remove(oldArrString2.toCharArray(),2,3,4));
		//System.out.println(MQArrayChar.reverse(oldArrString2.toCharArray(),2,3));
		//System.out.println(MQArrayChar.reverse(oldArrString2.toCharArray(),0,3));
		//System.out.println(MQArrayChar.reverse(oldArrString2.toCharArray(),4,8));

		//System.out.println(mem.sizeOf(oldArrString2.toCharArray()));
		//System.out.println(MQArrayChar.insert(oldArrString2.toCharArray(),4,oldArrString1.toCharArray()));
		//System.out.println(MQArrayChar.remove(oldArrString2.toCharArray(), 0,2,2,4,4,1));
		//System.out.println(MQArrayChar.view(oldArrString2.toCharArray(), 1,5,4,5,0,7,1));
		//System.out.println(MQArrayChar.subArray(oldArrString2.toCharArray(), 4,5));
		//System.out.println(MQArrayChar.insert(oldArrString2.toCharArray(),5,' '));
		//System.out.println(MQArrayChar.remove(oldArrString2.toCharArray(), 1));
		//char[] Arraythree = {'a','g','a','i','i','a','i','a'};
		//						0   1   2   3   4   5   6   7
		/*
		 * a b i a c d e f g i  h  i  a  j  k
		 * 0 1 2 3 4 5 6 7 8 9  10 11 12 13 14
		 */
		//System.out.println(MQArrayChar.indexOfLast("abiacdefgihiajk".toCharArray(),"ia".toCharArray(),13,2));
		//System.out.println(MQArrayChar.ArrayEquals(oldArrString2.toCharArray(),oldArrString1.toCharArray(),oldArrString1.toCharArray()));
//		{
//			for(int i=0;i<20;i++)
//			System.out.println(MQArrayChar.toString(MQArrayChar.subArrayRnd("abcdefgh".toCharArray(), 3)));
//		}
}

    public static char getChar(byte[] bytes)
    {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }
}