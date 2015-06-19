package MaQiao.MaQiaoArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import sun.misc.Unsafe;
import MaQiao.Constants.Constants;
import MaQiao.MaQiaoStringBuilder.MQSBuilder;
//import java.util.Random;
/**
 * <font color="red"><h1>看吧！数组！！！想吐不？</h1></font><br/>
 * <font color="red">注意：自己改泛型或接口去。本人不负责泛型或接口<br/>
 * (System.arraycopy对对象只有浅复制，自己修改toString方法)</font><br/>
 * 本例主负责字符数组的操作，其它局部变量都是依据数组而存在<br/>
 * 用泛型时，请修改 ElementEquals方法(单元素的比较) ArrayElementSet方法(方法赋值。char使用UNSAFE.putChar)<br/>
 * ElementSet方法预留，用于后期，对象的克隆或引用决定(暂未开发)<br/>
 * 因局部变量存在，针对字符数组为主操作（无关性能）<br/>
 * 强调：此工具主要是新建 基本对象<br/>
 * 主要是逻辑运算<br/>
 * @author Sunjian
 * @QQ 75583378
 * @Email sluizin@sohu.com
 * @version 1.0
 * @category char数组操作工具
 * @since 1.7
 * @Datetime 2015-4-9
 */
public final class MQArrayChar {
	private static final Unsafe UNSAFE = Constants.UNSAFE;

	//TODO wave 波浪数组的生成
	/**
	 * 把一维数组转成波浪数组<br/>
	 * Cross:false 为空时以 Consts.ElementFill [\0] 代替（正常范围）<br/>
	 * Cross:true 跨域调取(循环调取)（超常范围）<br/>
	 * 
	 * <pre>
	 * wave("abcE1234",3,1,false)
	 * result:{"E","c1","b2","a3","\04"}
	 * </pre>
	 * @param ArraySource char[]
	 * @param FromIndex int
	 * @param Size int
	 * @param Cross boolean
	 * @return char[][]
	 */
	public static final char[][] wave(final char[] ArraySource, final int StartIndex, final int Size, final boolean Cross) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || StartIndex < 0 || StartIndex >= len || Size <= Consts.Zero) return toArray(ArraySource);
		int pPoint = (StartIndex + Size > len) ? len - 1 : StartIndex + Size - 1;/* 定位最右边界 */
		int level = Math.max(StartIndex, (len - pPoint - 1));
		final char[][] Array = new char[level + 1][];
		Array[0] = subArray(ArraySource, StartIndex, Size);
		final char[] c = new char[2];
		for (int i = 1; i <= level; i++) {
			ArrayElementSet(c, 0, get(ArraySource, StartIndex - i, Cross));
			ArrayElementSet(c, 1, get(ArraySource, pPoint + i, Cross));
			/*c[0] = get(ArraySource, StartIndex - i, Cross);c[1] = get(ArraySource, pPoint + i, Cross);*/
			ArrayElementSet(Array, i, c);
		}
		return Array;
	}

	//TODO get 得到数组或二维数组指定位置的字符与数组
	/**
	 * 得到数组指定位置的字符元素<br/>
	 * Cross:false 为空时以 Consts.ElementFill [\0] 代替<br/>
	 * Cross:true 跨域调取(循环调取)<br/>
	 * 
	 * <pre>
	 * get("abcdef",2,false)
	 * result:c
	 * </pre>
	 * @param ArraySource char[]
	 * @param Index int
	 * @param Cross boolean
	 * @return char
	 */
	public static final char get(final char[] ArraySource, final int Index, boolean Cross) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || Index < 0 || Index >= len) {
			if (Cross) return ArraySource[selectCycleArrayIndex(len, Index)];
			return Consts.ElementFill;
		}
		return ArraySource[Index];
	}

	/**
	 * 得到二维数组指定位置的字符元素<br/>
	 * Cross:false 为空时以 Consts.ArrayNull [char[0]] 代替<br/>
	 * Cross:true 跨域调取(循环调取)<br/>
	 * 
	 * <pre>
	 * get({"ab","cd","ef","XY","st"},3,false)
	 * result:"XY"
	 * </pre>
	 * @param ArraySource char[][]
	 * @param Index int
	 * @param Cross boolean
	 * @return char[]
	 */
	public static final char[] get(final char[][] ArraySource, final int Index, boolean Cross) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || Index < 0 || Index >= len) {
			if (Cross) return ArraySource[selectCycleArrayIndex(len, Index)];
			return Consts.ArrayNull;
		}
		return ArraySource[Index];
	}

	//TODO displace 字符数组的置换
	/**
	 * 数组的位置置换 与 replace、replaceAll不同<br/>
	 * 置换是指数组某段与某段进行交换，只提供相同大小的区块置换</br>
	 * 
	 * <pre>
	 * displace("abXYZfg123lmnopq",2,7,3)
	 * result:  "ab123fgXYZlmnopq"
	 * </pre>
	 * @param ArraySource char[]
	 * @param FromIndex int
	 * @param ToIndex int
	 * @param Size int
	 * @return char[]
	 */
	public static final char[] displace(final char[] ArraySource, final int FromIndex, final int ToIndex, final int Size) {
		return exChange(ArraySource, FromIndex, Size, ToIndex, Size);
	}

	//TODO folio 字符数组的对折
	/**
	 * 字符数组的对折<br/>
	 * 
	 * <pre>
	 * folio("ABC$123",true)
	 * result:A3B2C1$
	 * folio("ABC$123",false)
	 * result:3A2B1C$
	 * </pre>
	 * @param ArraySource char[]
	 * @param order boolean
	 * @return char[]
	 */
	public static final char[] folio(final char[] ArraySource, final boolean order) {
		int len;
		if ((len = ArraySource.length) < 2) return ArraySource;
		int half = len >> 1;
		final char[] Array = new char[len];
		for (int i = 0, p = 0; i < half; i++) {
			if (order) {
				Array[p++] = ArraySource[i];
				Array[p++] = ArraySource[len - 1 - i];
			} else {
				Array[p++] = ArraySource[len - 1 - i];
				Array[p++] = ArraySource[i];
			}
		}
		if (len % 2 != Consts.Zero) Array[len - 1] = ArraySource[half];
		return Array;
	}

	//TODO toArrayByte 字符数组转成Byte数组
	/**
	 * 字符数组转成Byte数组
	 * 
	 * <pre>
	 * toArrayByte("a1国")
	 * result:
	 * {'1100001','0'}
	 * {'110001' ,'0'}
	 * {'11111111111111111111111111111101' ,'1010110'}
	 * </pre>
	 * @param ArraySource char[]
	 * @return byte[][]
	 */
	public static final byte[][] toArrayByte(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == 0) return Consts.ArrayByte2Null;
		final byte[][] ByteArray = new byte[len][];
		while (--len > Consts.INDEX_NOT_FOUND)
			ByteArray[len] = getBytes(ArraySource[len]);
		return ByteArray;
	}

	/**
	 * 单独字符char转成byte[]
	 * @param data char
	 * @return byte[]
	 */
	private static final byte[] getBytes(final char data) {
		final byte[] bytes = new byte[2];
		bytes[0] = (byte) (data);
		bytes[1] = (byte) (data >> 8);
		return bytes;
	}

	//TODO isExistsLowerCase 判断数组是否含有小写字母
	/**
	 * 判断数组是否含有小写字母<br/>
	 * 主过程 {@link #PrivateisExistsCase(char[], boolean) PrivateisExistsCase}<br/>
	 * 
	 * <pre>
	 * isExistsLowerCase("ABCDEFaGH")
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @return boolean
	 */
	public static final boolean isExistsLowerCase(final char[] ArraySource) {
		return PrivateisExistsCase(ArraySource, true);
	}

	//TODO isExistsUpperCase 判断数组是否含有大写字母
	/**
	 * 判断数组是否含有大写字母<br/>
	 * 主过程 {@link #PrivateisExistsCase(char[], boolean) PrivateisExistsCase}<br/>
	 * 
	 * <pre>
	 * isExistsUpperCase("abcdeZfgh")
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @return boolean
	 */
	public static final boolean isExistsUpperCase(final char[] ArraySource) {
		return PrivateisExistsCase(ArraySource, false);
	}

	/**
	 * 判断数组是否含有大写或小写字母<br/>
	 * 
	 * <pre>
	 * PrivateisExistsCase("abcdeZfgh",false)
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param Lower boolean
	 * @return boolean
	 */
	private static final boolean PrivateisExistsCase(final char[] ArraySource, final boolean Lower) {
		int len;
		if ((len = ArraySource.length) == 0) return false;
		/* Now check if there are any characters that need to be changed. */
		char c;
		for (int firstUpper = 0, supplChar; firstUpper < len;) {
			c = ArraySource[firstUpper];
			if ((c >= Character.MIN_HIGH_SURROGATE) && (c <= Character.MAX_HIGH_SURROGATE)) {
				supplChar = Character.codePointAt(ArraySource, firstUpper, len);
				if (Lower && supplChar == Character.toLowerCase(supplChar)) return true;
				if (!Lower && supplChar != Character.toLowerCase(supplChar)) return true;
				firstUpper += Character.charCount(supplChar);
			} else {
				if (!Lower && c != Character.toLowerCase(c)) return true;
				if (Lower && c == Character.toLowerCase(c)) return true;
				firstUpper++;
			}
		}
		return false;
	}

	//TODO startsWith endsWith
	/**
	 * 判断 数组是主数组的开头数组<br/>
	 * 
	 * <pre>
	 * startsWith("abcdefgh","abc")
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return boolean
	 */
	public static final boolean startsWith(final char[] ArraySource, final char[] Array) {
		return PrivateArrayFindArray(ArraySource, Array, 0);
	}

	/**
	 * 判断 数组是主数组的结尾数组<br/>
	 * 
	 * <pre>
	 * endsWith("abcdefgh","fgh")
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return boolean
	 */
	public static final boolean endsWith(final char[] ArraySource, final char[] Array) {
		return PrivateArrayFindArray(ArraySource, Array, ArraySource.length - Array.length);
	}

	//TODO left right 数组左侧，右侧子数组
	/**
	 * 数组左侧N位的数组<br/>
	 * 
	 * <pre>
	 * left("abcdefgh",3)
	 * result:abc
	 * </pre>
	 * @param ArraySource char[]
	 * @param size int
	 * @return char[]
	 */
	public static final char[] left(final char[] ArraySource, final int size) {
		return subArray(ArraySource, 0, size);
	}

	/**
	 * 数组右侧N位的数组<br/>
	 * 
	 * <pre>
	 * right("abcdefgh",3)
	 * result:fgh
	 * </pre>
	 * @param ArraySource char[]
	 * @param size int
	 * @return char[]
	 */
	public static final char[] right(final char[] ArraySource, final int size) {
		int leftint;
		if ((leftint = (ArraySource.length - size)) < 0) leftint = 0;
		return subArray(ArraySource, leftint, size);
	}

	//TODO disOrganize 打乱顺序
	/**
	 * 随机打乱数组顺序<br/>
	 * 
	 * <pre>
	 * disOrganize("abcd1234")
	 * result:1dab2c43
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] disOrganize(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) < 2) return ArraySource;
		final char[] newArray = clone(ArraySource);
		final Character c = Consts.ElementFill;
		for (int i = 0; i < len; i++)
			exChange(newArray, i, UtilTool.getRndInt(0, len - 1), c);
		return newArray;
	}

	//TODO max,min 数组中最大的值
	/**
	 * 数组中最大值<br/>
	 * 
	 * <pre>
	 * max("YXabgdfceh123z")
	 * result:'z'
	 * </pre>
	 * @param ArraySource char[]
	 * @return char
	 */
	public static final char max(final char[] ArraySource) {
		int p;
		if ((p = minmaxIndex(ArraySource, true)) == Consts.INDEX_NOT_FOUND) return Consts.ElementFill;
		return ArraySource[p];
	}

	/**
	 * 数组中最小值<br/>
	 * 
	 * <pre>
	 * min("YXabgdfceh123z")
	 * result:'1'
	 * </pre>
	 * @param ArraySource char[]
	 * @return char
	 */
	public static final char min(final char[] ArraySource) {
		int p;
		if ((p = minmaxIndex(ArraySource, false)) == Consts.INDEX_NOT_FOUND) return Consts.ElementFill;
		return ArraySource[p];
	}

	//TODO length 数组的长度
	/**
	 * 二维数组的总长度<br/>
	 * 
	 * <pre>
	 * length({"abc","12","XYZK"})
	 * result:9
	 * </pre>
	 * @param Array char[][]
	 * @return int
	 */
	public static final int length(final char[]... Array) {
		int count = 0;
		for (int i = 0, len = Array.length; i < len; i++)
			count += Array.length;
		return count;
	}

	//TODO maxIndex,minIndex 数组中最大，最小值的下标
	/**
	 * 数组中最大值所在位置(下标)<br/>
	 * 
	 * <pre>
	 * maxIndex("YXabgdfceh123z")
	 * result:13
	 * </pre>
	 * @param ArraySource char[]
	 * @return int
	 */
	public static final int maxIndex(final char[] ArraySource) {
		return minmaxIndex(ArraySource, true);
	}

	/**
	 * 数组中最小值所在位置(下标)<br/>
	 * 
	 * <pre>
	 * minIndex("YXabgdfceh123zc")
	 * result:10
	 * </pre>
	 * @param ArraySource char[]
	 * @return int
	 */
	public static final int minIndex(final char[] ArraySource) {
		return minmaxIndex(ArraySource, false);
	}

	/**
	 * 数组中最小值或最大值所在位置(下标)<br/>
	 * 
	 * <pre>
	 * minmaxIndex("YXabgdfceh123zc",true)
	 * result:13
	 * </pre>
	 * @param ArraySource char[]
	 * @return int
	 */
	private static final int minmaxIndex(final char[] ArraySource, final boolean isMax) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return Consts.INDEX_NOT_FOUND;
		int point = Consts.Zero, i = 0;
		for (char c = ArraySource[(i++)]; i < len; i++) {
			if (isMax && (ElementComparison(c, ArraySource[i]) == -1)) c = ArraySource[(point = i)];
			if (!isMax && (ElementComparison(c, ArraySource[i]) == 1)) c = ArraySource[(point = i)];
		}
		return point;
	}

	//TODO toArrayList Character数组转成 ArrayList<Character>
	/**
	 * Character数组转成 ArrayList< Character ><br/>
	 * @param ArraySource char[]
	 * @return List< Character >
	 */
	public static final List<Character> toArrayList(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return null;
		List<Character> newList = new ArrayList<Character>(len);
		for (int i = 0; i < len; i++)
			newList.add(ArraySource[i]);
		return newList;
	}

	//TODO toHashSet Character数组转成HashSet<Character>
	/**
	 * Character数组转成HashSet< Character ><br/>
	 * @param ArraySource char[]
	 * @return Set< Character >
	 */
	public static final Set<Character> toHashSet(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return null;
		Set<Character> newSet = new HashSet<Character>(len);
		for (int i = 0; i < len; i++)
			newSet.add(ArraySource[i]);
		return newSet;
	}

	//TODO sort 数组的排序
	/* sort 数组的排序主要还是在原数组的基础上进行交换进行输出的<br/> 后期，如果数组很小，可以定义新的数组，以脱离原数组 */
	/**
	 * 数组进行排序<br/>
	 * <font color='red'>泛型请修改ElementComparison方法，进行二次元素比较</font><br/>
	 * 
	 * <pre>
	 * sort ("gdfce",true)
	 * result:gfedc
	 * sort ("gdfce",false)
	 * result:cdefg
	 * </pre>
	 * @param ArraySource char[]
	 * @param isDown boolean
	 * @return char[]
	 */
	public static final char[] sort(final char[] ArraySource, final boolean isDown) {
		return sort(ArraySource, 0, isDown);
	}

	/**
	 * 数组指定位置范围内进行排序(默认到数组结尾)<br/>
	 * <font color='red'>泛型请修改ElementComparison方法，进行二次元素比较</font><br/>
	 * 
	 * <pre>
	 * sort ("abgdfce",2,true)
	 * result:abgfedc
	 * sort ("abgdfce",2,false)
	 * result:abcdefg
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param isDown boolean
	 * @return char[]
	 */
	public static final char[] sort(final char[] ArraySource, final int StartIndex, final boolean isDown) {
		return sort(ArraySource, StartIndex, ArraySource.length - StartIndex, isDown);
	}

	/**
	 * 数组指定位置范围内进行排序<br/>
	 * <font color='red'>泛型请修改ElementComparison方法，进行二次元素比较</font><br/>
	 * 
	 * <pre>
	 * sort ("abgdfceh",2,5,true)
	 * result:abgfedch
	 * sort ("abgdfceh",2,5,false)
	 * result:abcdefgh
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param size int
	 * @param isDown boolean
	 * @return char[]
	 */
	public static final char[] sort(final char[] ArraySource, final int StartIndex, final int size, final boolean isDown) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || StartIndex < 0 || StartIndex >= len || size <= Consts.Zero) return ArraySource;
		int pPoint = (StartIndex + size > len) ? len - 1 : StartIndex + size - 1;/* 定位最右边界 */
		final Character c = Consts.ElementFill;
		for (int i = StartIndex; i <= pPoint - 1; i++)
			for (int ii = i + 1; ii <= pPoint; ii++)
				if ((isDown && ElementComparison(ArraySource[i], ArraySource[ii]) < 0) || (!isDown && ElementComparison(ArraySource[i], ArraySource[ii]) > 0)) exChange(ArraySource, ii, i, c);
		return ArraySource;

	}

	//TODO copyOf 复制N次组成新的数组
	/**
	 * 复制N个字符`组成数组<br/>
	 * 
	 * <pre>
	 * copyOf('X',5)
	 * result:XXXXX
	 * </pre>
	 * @param c char
	 * @param N int
	 * @return char[]
	 */
	public static final char[] copyOf(final char c, final int N) {
		if (N <= Consts.Zero) return Consts.ArrayNull;
		final char[] newArray = new char[N];
		for (int i = 0; i < N; i++)
			ArrayElementSet(newArray, i, c);
		return newArray;
	}

	/**
	 * 复制N个数组组成新的数组<br/>
	 * 
	 * <pre>
	 * copyOf("ab1",2)
	 * result:ab1ab1
	 * </pre>
	 * @param ArraySource char[]
	 * @param N int
	 * @return char[]
	 */
	public static final char[] copyOf(final char[] ArraySource, final int N) {
		return copyOf(ArraySource, 0, ArraySource.length, N);
	}

	/**
	 * 复制N个 从数组中选中的串 组成新的数组<br/>
	 * 暂时不引用subArray<br/>
	 * 
	 * <pre>
	 * copyOf("ab1234",2,3,2)
	 * result:123123
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param size int
	 * @param N int
	 * @return char[]
	 */
	public static final char[] copyOf(final char[] ArraySource, final int StartIndex, final int size, final int N) {
		int len;/* 循环征用 */
		if ((len = ArraySource.length) == Consts.Zero || StartIndex < 0 || StartIndex >= len || size <= Consts.Zero || N <= 0) return Consts.ArrayNull;
		len = ((StartIndex + size) >= len) ? len - StartIndex : size;
		final char[] newArray = new char[len * N];
		for (int i = 0; i < N; i++)
			System.arraycopy(ArraySource, StartIndex, newArray, (i * len), len);
		return newArray;
	}

	//TODO fill 数组填充
	/**
	 * 数组全部填充 ' '<br/>
	 * 
	 * <pre>
	 * fillSpace("abcdefg")
	 *    result:"       "
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] fillSpace(final char[] ArraySource) {
		return fill(ArraySource, 0, ArraySource.length);
	}

	/**
	 * 数组指定位置的范围内填充' '<br/>
	 * 
	 * <pre>
	 * fillSpace("abcdefg",2,3)
	 *     result:ab   fg
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] fillSpace(final char[] ArraySource, final int StartIndex, final int size) {
		return fill(ArraySource, StartIndex, size, Consts.ElementFillSpace);
	}

	/**
	 * 数组全部填充'\0'<br/>
	 * 
	 * <pre>
	 * fill("abcdefg")
	 * result:\0\0\0\0\0\0\0
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] fill(final char[] ArraySource) {
		return fill(ArraySource, 0, ArraySource.length);
	}

	/**
	 * 数组指定位置的范围内填充'\0'<br/>
	 * 
	 * <pre>
	 * fill("abcdefg",2,3)
	 * result:ab\0\0\0fg
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] fill(final char[] ArraySource, final int StartIndex, final int size) {
		return fill(ArraySource, StartIndex, size, Consts.ElementFill);
	}

	/**
	 * 数组指定位置的范围内填充字符<br/>
	 * 
	 * <pre>
	 * fill("abcdefg",2,3,'X')
	 * result:abXXXfg
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param size int
	 * @param Consts.ElementFill char
	 * @return char[]
	 */
	public static final char[] fill(final char[] ArraySource, final int StartIndex, final int size, final char elementFill) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || StartIndex < 0 || StartIndex >= len || size <= Consts.Zero) return ArraySource;
		for (int i = StartIndex; (i - StartIndex) <= size && i < len; i++)
			ArrayElementSet(ArraySource, i, elementFill);
		return ArraySource;
	}

	//TODO fillCycle  数组填充循环字符数组
	/**
	 * 数组填充循环字符数组<br/>
	 * 
	 * <pre>
	 * fill("abcdefg","XYZT")
	 * result:XYZTXYZ
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return char[]
	 */
	public static final char[] fillCycle(final char[] ArraySource, final char... Array) {
		return fillCycle(ArraySource, 0, Array);
	}

	/**
	 * 数组指定位置的范围(到数组结尾)内填充循环字符数组<br/>
	 * 
	 * <pre>
	 * fill("abcdefg",2,"XYZT")
	 * result:abXYZTX
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param Array char[]
	 * @return char[]
	 */
	public static final char[] fillCycle(final char[] ArraySource, final int StartIndex, final char... Array) {
		return fillCycle(ArraySource, StartIndex, ArraySource.length - StartIndex, true, Array);
	}

	/**
	 * 数组指定位置的范围内填充循环字符数组<br/>
	 * 
	 * <pre>
	 * fill("abcdefg",2,3,"XYZT")
	 * result:abXYZfg
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param size int
	 * @param Array char[]
	 * @return char[]
	 */
	public static final char[] fillCycle(final char[] ArraySource, final int StartIndex, final int size, final char... Array) {
		return fillCycle(ArraySource, StartIndex, size, true, Array);
	}

	/**
	 * 数组指定位置的范围内填充循环字符数组(向前(Backward:false)或向后(Backward:true))<br/>
	 * 
	 * <pre>
	 * fill("abcdefg",2,3,false,"XYZT")
	 * result:abTZYfg
	 * </pre>
	 * @param ArraySource char[]
	 * @param StartIndex int
	 * @param size int
	 * @param Backward boolean
	 * @param Array char[]
	 * @return char[]
	 */
	public static final char[] fillCycle(final char[] ArraySource, final int StartIndex, final int size, final boolean Backward, final char... Array) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || StartIndex < 0 || StartIndex >= len || size <= Consts.Zero) return ArraySource;
		for (int i = StartIndex, p = 0; (i - StartIndex) < size && i < len; i++)
			ArrayElementSet(ArraySource, i, selectCycleArray(Array, (Backward) ? p++ : --p));
		return ArraySource;
	}

	/**
	 * 调出数组指定下标的字符(循环调出)<br/>
	 * 负数时：从数组尾向前调<br/>
	 * 正数时：从数组头向后调<br/>
	 * 
	 * <pre>
	 * selectCycleArray("abcd",-1)
	 * result:d
	 * selectCycleArray("abcd",4)
	 * result:a
	 * </pre>
	 * @param ArraySource char[]
	 * @param Index int
	 * @return char
	 */
	public static final char selectCycleArray(final char[] ArraySource, final int Index) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return Consts.ElementFill;
		return ArraySource[selectCycleArrayIndex(len, Index)];
	}

	/**
	 * 调出数组指定下标(循环调出)[跨域调取]<br/>
	 * 负数时：从数组尾向前调<br/>
	 * 正数时：从数组头向后调<br/>
	 * 
	 * <pre>
	 * 下标：0 1 2 3 4
	 * Index: -2 -1 0 1 2 3 4 5 6
	 * 输出:   3 4 0 1 2 3 4 0 1
	 * selectCycleArrayIndex(5,6)
	 * result:1
	 * </pre>
	 * @param ArraySource char[]
	 * @param Index int
	 * @return int
	 */
	private static final int selectCycleArrayIndex(final int len, final int Index) {
		return (Index >= 0) ? Index % len : (len + (Index % len)) % len;
	}

	// TODO clear 清空数组
	/**
	 * 清空字符(1:填充空数据)<br/>
	 * @param c char
	 */
	public static final void clear(Character c) {
		c = Consts.ElementFill;
		c = null;
	}

	/**
	 * 清空下标数组(1:填充空数据)<br/>
	 * @param Array int[]
	 */
	public static final void clear(int[] Array) {
		int ArrayLen;
		if ((ArrayLen = Array.length) == Consts.Zero) return;
		while (--ArrayLen > Consts.INDEX_NOT_FOUND)
			Array[ArrayLen] = Consts.ElementIntFill;
		Array = null;
	}

	/**
	 * 清空数组(1:填充空数据，2:转向空数组，3:设置成Null)<br/>
	 * @param ArraySource char[]
	 */
	public static final void clear(char[] ArraySource) {
		int len = ArraySource.length;
		while (--len > Consts.INDEX_NOT_FOUND)
			ArraySource[len] = Consts.ElementFill;
		ArraySource = Consts.ArrayNull;
		ArraySource = null;
	}

	// TODO exChange 交换数组位置
	/**
	 * 交换数组位置<br/>
	 * 
	 * <pre>
	 * exChange("abCdEfg",2,4)
	 * result:abEdCfg
	 * </pre>
	 * @param ArraySource char[]
	 * @param FromIndex int
	 * @param ToIndex int
	 */
	public static final void exChange(final char[] ArraySource, final int FromIndex, final int ToIndex) {
		int len;
		if (FromIndex < 0 || ToIndex < 0 || FromIndex == ToIndex || (len = ArraySource.length) <= FromIndex || len <= ToIndex) return;
		char c = ArraySource[FromIndex];/*Character*/
		ArrayElementSet(ArraySource, FromIndex, ArraySource[ToIndex]);
		ArrayElementSet(ArraySource, ToIndex, c);
		c = Consts.ElementFill;
	}

	/**
	 * 交换数组位置<br/>
	 * 
	 * <pre>
	 * exChange("abCdEfg",2,4,'')
	 * result:abEdCfg
	 * </pre>
	 * @param ArraySource char[]
	 * @param FromIndex int
	 * @param ToIndex int
	 * @param c Character 临时指针，用于减少大量交换时，临时指针的建立
	 */
	public static final void exChange(final char[] ArraySource, final int FromIndex, final int ToIndex, Character c) {
		int len;
		if (FromIndex < 0 || ToIndex < 0 || FromIndex == ToIndex || (len = ArraySource.length) <= FromIndex || len <= ToIndex) return;
		c = ArraySource[FromIndex];/*Character*/
		ArrayElementSet(ArraySource, FromIndex, ArraySource[ToIndex]);
		ArrayElementSet(ArraySource, ToIndex, c);
	}

	/**
	 * 交换二维数组位置<br/>
	 * 
	 * <pre>
	 * exChange({"ab","cd","ef"},0,2,"")
	 * result:{"ef","cd","ab"}
	 * </pre>
	 * @param ArraySource char[][]
	 * @param FromIndex int
	 * @param ToIndex int
	 * @param c char[]
	 */
	public static final void exChange(final char[][] ArraySource, final int FromIndex, final int ToIndex, char[] c) {
		int len;
		if (FromIndex < 0 || ToIndex < 0 || FromIndex == ToIndex || (len = ArraySource.length) <= FromIndex || len <= ToIndex) return;
		c = ArraySource[FromIndex];/*Character*/
		ArrayElementSet(ArraySource, FromIndex, ArraySource[ToIndex]);
		ArrayElementSet(ArraySource, ToIndex, c);
	}

	/**
	 * 对外请用{@link #displace(char[], int, int, int) displace()}方法<br/>
	 * exChange()为保留方法重载<br/>
	 * 数组的位置置换 与 replace、replaceAll不同<br/>
	 * 置换是指数组某段与某段进行交换，只提供相同大小的区块置换</br>
	 * 
	 * <pre>
	 * exChange("abXYZfg123lmnopq",2,7,3)
	 * result:  "ab123fgXYZlmnopq"
	 * </pre>
	 * @param ArraySource char[]
	 * @param FromIndex int
	 * @param ToIndex int
	 * @param Size int
	 * @return char[]
	 */
	@Deprecated
	public static final char[] exChange(final char[] ArraySource, final int FromIndex, final int ToIndex, final int Size) {
		return exChange(ArraySource, FromIndex, Size, ToIndex, Size);
	}

	/**
	 * 数组的位置置换 与 replace、replaceAll不同<br/>
	 * 置换是指数组某段与某段进行交换，置换允许置换区块大小不同，允许交叉跳跃(绝对混乱！！！)<br/>
	 * <font color='red'>混乱程序可执行。现阶段只提供相同大小的区块置换</font><br/>
	 * 
	 * <pre>
	 * exChange("abcdefghi",2,2,5,3)
	 * result:"abfghecdi"
	 * </pre>
	 * @param ArraySource char[]
	 * @param FromIndex int
	 * @param FromSize int
	 * @param ToIndex int
	 * @param ToSize int
	 * @return char[]
	 */
	@Deprecated
	public static final char[] exChange(final char[] ArraySource, final int FromIndex, final int FromSize, final int ToIndex, final int ToSize) {
		int len;
		if (FromIndex < 0 || ToIndex < 0 || FromSize <= Consts.Zero || ToSize <= Consts.Zero || FromIndex == ToIndex || (len = ArraySource.length) <= FromIndex || len <= ToIndex) return ArraySource;
		final char[] Array = new char[len];
		int SizeFrom = ((FromIndex + FromSize) >= len) ? len - FromIndex : FromSize;
		int SizeTo = ((ToIndex + ToSize) >= len) ? len - ToIndex : ToSize;
		int p = 0, tempsize;
		int minIndex = (FromIndex < ToIndex) ? ToIndex : FromIndex;
		int minSize = (FromIndex < ToIndex) ? SizeTo : SizeFrom;
		int maxIndex = (FromIndex < ToIndex) ? FromIndex : ToIndex;
		int maxSize = (FromIndex < ToIndex) ? SizeFrom : SizeTo;
		if (maxIndex >= 0) {
			System.arraycopy(ArraySource, 0, Array, 0, maxIndex);
			p += maxIndex;
		}
		System.arraycopy(ArraySource, minIndex, Array, p, minSize);
		p += minSize;
		if ((tempsize = minIndex - p) > Consts.Zero) {
			System.arraycopy(ArraySource, maxIndex + maxSize, Array, p, tempsize);
			p += tempsize;
		}
		System.arraycopy(ArraySource, maxIndex, Array, p, maxSize);
		p += maxSize;
		if ((tempsize = len - p) > Consts.Zero) System.arraycopy(ArraySource, p, Array, p, tempsize);
		return Array;
	}

	//TODO toArray 单独字符转成数组 单独数组转成两维数组
	/**
	 * 单独字符转成数组<br/>
	 * 
	 * <pre>
	 * toArray('a')
	 * result:"a"
	 * </pre>
	 * @param c char
	 * @return char[]
	 */
	public static final char[] toArray(final char c) {
		final char[] newArray = { c };
		return newArray;
	}

	/**
	 * 单独数组转成两维数组<br/>
	 * 
	 * <pre>
	 * toArray("abc")
	 * result:{"abc"}
	 * </pre>
	 * @param c char[]
	 * @return char[][]
	 */
	public static final char[][] toArray(final char... c) {
		final char[][] newArray = { c };
		return newArray;
	}

	/**
	 * 单独字符转成两维数组<br/>
	 * 
	 * <pre>
	 * toArray0To2('a')
	 * result:{"a"}
	 * </pre>
	 * @param c char
	 * @return char[][]
	 */
	public static final char[][] toArray0To2(final char c) {
		return toArray(toArray(c));
	}

	//TODO shift 移位操作

	/**
	 * 移位，当 bitNum > 0 右移，bitNum < 0左移<br/>
	 * 长度范围为数组全部 <br/>
	 * 空出位置进行填充（默认空格）<br/>
	 * 
	 * <pre>
	 * shift ("abcdefgh",2)
	 * result:"  abcdef"
	 * shift ("abcdefgh",-2)
	 * result:"cdefgh  "
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int
	 * @return char[]
	 */
	public static final char[] shift(final char[] ArraySource, final int bitNum) {
		if (bitNum == Consts.Zero) return ArraySource;
		return shift(ArraySource, bitNum, 0);
	}

	/**
	 * 移位，当 bitNum > 0 右移，bitNum < 0左移 Index开始移位位置<br/>
	 * size默认为数组结尾 <br/>
	 * 空出位置进行填充（默认空格）<br/>
	 * 
	 * <pre>
	 * shift("abcdefgh",2,3)
	 * result:abc  dgh
	 * shift("abcdefgh",-2,3)
	 * result:abcf  gh
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int
	 * @param StartIndex int
	 * @return char[]
	 */
	public static final char[] shift(final char[] ArraySource, final int bitNum, final int StartIndex) {
		return shift(ArraySource, bitNum, StartIndex, ArraySource.length - StartIndex);
	}

	/**
	 * 移位，当 bitNum > 0 右移，bitNum < 0左移 Index开始移位位置 size长度范围 <br/>
	 * 空出位置进行填充（默认空格）<br/>
	 * 
	 * <pre>
	 * shift("abcdefgh",2,3,3)
	 * result:abc  dgh
	 * shift("abcdefgh",-2,3,3)
	 * result:abcf  gh
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int
	 * @param StartIndex int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] shift(final char[] ArraySource, final int bitNum, final int StartIndex, final int size) {
		return shift(ArraySource, bitNum, StartIndex, size, Consts.ElementFillSpace);
	}

	/**
	 * 移位，当 bitNum > 0 右移，bitNum < 0左移 Index开始移位位置<br/>
	 * 空出位置进行填充<br/>
	 * 
	 * <pre>
	 * shift("abcdefgh",2,3,' ')
	 * result:abc  def
	 * shift("abcdefgh",-2,3,' ')
	 * result:cd  efgh
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int
	 * @param StartIndex int
	 * @param Consts.ElementFill char
	 * @return char[]
	 */
	@Deprecated
	public static final char[] shift(final char[] ArraySource, final int bitNum, final int StartIndex, final char elementFill) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || bitNum == Consts.Zero || StartIndex < 0 || StartIndex >= len) return ArraySource;
		final char[] ArrayNew = new char[len];/* 复制对象 */
		System.arraycopy(ArraySource, 0, ArrayNew, 0, len);
		int i = 0, cutPoint = StartIndex + bitNum;
		if (bitNum > Consts.Zero) {/* 右移 */
			for (i = (len - 1); i >= cutPoint - 1; i--)
				if (i - bitNum >= Consts.Zero) ArrayElementSet(ArrayNew, i, ArrayNew[i - bitNum]);
			for (i = StartIndex; i < cutPoint; i++)
				if (i < len) ArrayElementSet(ArrayNew, i, elementFill);
		} else {/* 左移 */
			for (i = 0; i <= cutPoint; i++)
				if (i < len) ArrayElementSet(ArrayNew, i, ArrayNew[i - bitNum]);
			for (i = cutPoint + 1; i <= StartIndex; i++)
				if (i < len && i >= 0) ArrayElementSet(ArrayNew, i, elementFill);
		}
		return ArrayNew;
	}

	/**
	 * 移位，当 bitNum > 0 右移，bitNum < 0左移 Index开始移位位置 size长度范围 <br/>
	 * 空出位置进行填充<br/>
	 * 
	 * <pre>
	 * shift("abcdefgh",2,3,3,' ')
	 * result:abc  dgh
	 * shift("abcdefgh",-2,3,3,' ')
	 * result:abcf  gh
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int
	 * @param StartIndex int
	 * @param size int
	 * @param Consts.ElementFill char
	 * @return char[]
	 */
	public static final char[] shift(final char[] ArraySource, final int bitNum, final int StartIndex, final int size, final char elementFill) {
		int len;/* 循环征用 */
		if ((len = ArraySource.length) == Consts.Zero || bitNum == Consts.Zero || StartIndex < 0 || StartIndex >= len || size <= Consts.Zero) return ArraySource;
		int pPoint = (StartIndex + size > len) ? len - 1 : StartIndex + size - 1;/* 定位最右边界 */
		if (pPoint == StartIndex) return ArraySource;
		final char[] ArrayNew = clone(ArraySource);/* 复制对象 */
		if (bitNum > Consts.Zero) {/* 右移 */
			for (len = pPoint; len >= StartIndex; len--) {/* 区间内，右移则检索则 从右向左 */
				if ((len - bitNum) < StartIndex) {
					ArrayElementSet(ArrayNew, len, elementFill);
					continue;
				}
				ArrayElementSet(ArrayNew, len, ArrayNew[len - bitNum]);
			}
		} else {/* 左移 */
			for (len = StartIndex; len <= pPoint; len++) {/* 区间内，左移则检索则 从左向右 */
				if ((len - bitNum) > pPoint) {
					ArrayElementSet(ArrayNew, len, elementFill);
					continue;
				}
				ArrayElementSet(ArrayNew, len, ArrayNew[len - bitNum]);
			}
		}
		return ArrayNew;
	}

	//TODO shiftRoll 移动元素操作循环体
	/**
	 * 滚动元素操作循环体，当 bitNum > 0 右滚，bitNum < 0左滚<br/>
	 * 长度范围为数组全部 <br/>
	 * size默认为数组结尾 <br/>
	 * 
	 * <pre>
	 * shiftRoll("abcdefghj",2))
	 *     result:hjabcdefg
	 * shiftRoll("abcdefghj",-2))
	 *     result:cdefghjab
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int 滚动次数
	 * @return char[]
	 */
	public static final char[] shiftRoll(final char[] ArraySource, final int bitNum) {
		if (bitNum == Consts.Zero) return ArraySource;
		return shiftRoll(ArraySource, bitNum, 0);
	}

	/**
	 * 滚动元素操作循环体，当 bitNum > 0 右滚，bitNum < 0左滚 Index开始移位位置 <br/>
	 * size默认为数组结尾 <br/>
	 * 
	 * <pre>
	 * shiftRoll("abcdefghj",2,3))
	 *     result:abchjdefg
	 * shiftRoll("abcdefghj",-2,3))
	 *     result:abcfghjde
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int
	 * @param StartIndex int
	 * @return char[]
	 */
	public static final char[] shiftRoll(final char[] ArraySource, final int bitNum, final int StartIndex) {
		return shiftRoll(ArraySource, bitNum, StartIndex, ArraySource.length - StartIndex);
	}

	/**
	 * 滚动元素操作循环体，当 <br/>
	 * bitNum > 0 右滚<br/>
	 * bitNum < 0 左滚<br/>
	 * Index开始移位位置<br/>
	 * size长度范围 <br/>
	 * 
	 * <pre>
	 * shiftRoll("abcdefghjklmnopqrst",2,3,6))
	 *     result:abchjdefgklmnopqrst
	 * shiftRoll("abcdefghjklmnopqrst",-2,3,6))
	 *     result:abcfghjdeklmnopqrst
	 * </pre>
	 * @param ArraySource char[]
	 * @param bitNum int
	 * @param StartIndex int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] shiftRoll(final char[] ArraySource, final int bitNum, final int StartIndex, final int size) {
		int len;/* 循环征用 */
		if ((len = ArraySource.length) == Consts.Zero || bitNum == Consts.Zero || StartIndex < 0 || StartIndex >= len || size <= Consts.Zero) return ArraySource;
		int pPoint = (StartIndex + size > len) ? len - 1 : StartIndex + size - 1;/* 定位最右边界 */
		if (pPoint == StartIndex) return ArraySource;
		final char[] ArrayNew = clone(ArraySource);/* 复制对象 */
		int rollid;/*, cutPoint = StartIndex + bitNum;*/
		Character c;
		if (bitNum > Consts.Zero) {/* 右移 */
			for (rollid = Consts.Zero; rollid < bitNum; rollid++) {
				c = ArrayNew[pPoint];
				for (len = pPoint; len > StartIndex; len--)
					/* 区间内，右移则检索则 从右向左 */
					ArrayElementSet(ArrayNew, len, ArrayNew[len - 1]);
				ArrayElementSet(ArrayNew, StartIndex, c);
			}
		} else {/* 左移 */
			for (rollid = Consts.Zero; rollid < -bitNum; rollid++) {
				c = ArrayNew[StartIndex];
				for (len = StartIndex; len < pPoint; len++)
					/* 区间内，左移则检索则 从左向右 */
					ArrayElementSet(ArrayNew, len, ArrayNew[len + 1]);
				ArrayElementSet(ArrayNew, pPoint, c);
			}
		}
		c = null;
		return ArrayNew;
	}

	// TODO concat 单独字符与数组全并
	/**
	 * 多个单独字符合并成数组<br/>
	 * 
	 * <pre>
	 * concat('a','b','c','d','e','f','g')
	 * result:abcdefg
	 * </pre>
	 * @param c char
	 * @return char[]
	 */
	public static final char[] concat(char... c) {
		return c;
	}

	/**
	 * 单独字符与字符数组合并(有顺序)<br/>
	 * 
	 * <pre>
	 * concat('X',"abcdefg")
	 * result:Xabcdefg
	 * </pre>
	 * @param c char
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] concat(final char c, final char[] ArraySource) {
		final int len = ArraySource.length;
		final char[] ArrayNew = new char[len + 1];
		ArrayElementSet(ArrayNew, 0, c);
		System.arraycopy(ArraySource, 0, ArrayNew, 1, len);
		return ArrayNew;
	}

	/**
	 * 单独字符与多个数组全并(有顺序)<br/>
	 * 
	 * <pre>
	 * concat('X',"ab","cd","efg")
	 * result:Xabcdefg
	 * </pre>
	 * @param c char
	 * @param ArraySource char[][]
	 * @return char[]
	 */
	public static final char[] concat(final char c, final char[]... ArraySource) {
		char[] ArrayNew = concat(ArraySource);
		if (ArrayNew.length == Consts.Zero) return toArray(c);
		final char[] ReusltArrayNew = new char[ArrayNew.length + 1];
		ArrayElementSet(ReusltArrayNew, 0, c);
		System.arraycopy(ArrayNew, 0, ReusltArrayNew, 1, ArrayNew.length);
		ArrayNew = null;
		return ReusltArrayNew;
	}

	/**
	 * 字符数组与单独字符合并(有顺序)<br/>
	 * 
	 * <pre>
	 * concat("abc",'d','e','f','g')
	 * result:abcdefg
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @return char[]
	 */
	public static final char[] concat(final char[] ArraySource, final char... c) {
		int len, lenc = c.length;
		if ((len = ArraySource.length) == Consts.Zero && lenc == Consts.Zero) return Consts.ArrayNull;
		if (lenc == Consts.Zero) return ArraySource;
		if (len == Consts.Zero) return concat(c);
		final char[] ArrayNew = new char[len + lenc];
		if (len > Consts.Zero) System.arraycopy(ArraySource, 0, ArrayNew, 0, len);
		if (lenc > Consts.Zero) System.arraycopy(concat(c), 0, ArrayNew, len, lenc);
		return ArrayNew;
	}

	/**
	 * 多个字符数组合并<br/>
	 * 
	 * <pre>
	 * concat("ab","cd","e","fg")
	 * result:abcdefg
	 * </pre>
	 * @param Array char[][]
	 * @return char[]
	 */
	public static final char[] concat(final char[]... Array) {
		int len;
		if ((len = Array.length) == Consts.Zero) return Consts.ArrayNull;
		int count = Consts.Zero, i;
		for (i = 0; i < len; i++)
			count += Array[i].length;
		if (count == (i = Consts.Zero)) return Consts.ArrayNull;
		final char[] ArrayNew = new char[count];
		for (int p = 0; i < len; i++) {
			if (Array[i].length == 0) continue;
			System.arraycopy(Array[i], 0, ArrayNew, p, Array[i].length);
			p += Array[i].length;
		}
		return ArrayNew;
	}

	// TODO contains 判断是否包含 正序检索
	/**
	 * 判断是否包含 查找数组全部<br/>
	 * 
	 * <pre>
	 * contains("abcdef",'c')
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @return boolean
	 */
	public static final boolean contains(final char[] ArraySource, final char c) {
		return contains(ArraySource, c, 0);
	}

	/**
	 * 判断是否包含 查找字符在字符数组的位置 (从fromIndex开始查找)到数组结尾<br/>
	 * 
	 * <pre>
	 * contains("abcdef",'c',0)
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @param fromIndex int
	 * @return boolean
	 */
	public static final boolean contains(final char[] ArraySource, final char c, final int fromIndex) {
		return contains(ArraySource, c, fromIndex, ArraySource.length);
	}

	/**
	 * 判断是否包含 查找字符在字符数组的位置 (从fromIndex开始查找)限制范围<br/>
	 * 
	 * <pre>
	 * contains("abcdef",'c',0,4)
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @param fromIndex int
	 * @param size int
	 * @return boolean
	 */
	public static final boolean contains(final char[] ArraySource, final char c, final int fromIndex, final int size) {
		return indexOf(ArraySource, c, fromIndex, size) != Consts.INDEX_NOT_FOUND;
	}

	/**
	 * 判断是否包含 查找字符数组在字符数组的位置 查找数组全部<br/>
	 * 
	 * <pre>
	 * contains("abcdef","cde")
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return boolean
	 */
	public static final boolean contains(final char[] ArraySource, final char[] Array) {
		return contains(ArraySource, Array, 0);
	}

	/**
	 * 判断是否包含 查找字符数组在字符数组的位置 (从fromIndex开始查找)到数组结尾<br/>
	 * 
	 * <pre>
	 * contains("abcdef","cde",1)
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @param fromIndex int
	 * @return boolean
	 */
	public static final boolean contains(final char[] ArraySource, final char[] Array, final int fromIndex) {
		return contains(ArraySource, Array, fromIndex, ArraySource.length);
	}

	/**
	 * 判断是否包含 查找字符数组在字符数组的位置 (从fromIndex开始查找)限制范围<br/>
	 * 
	 * <pre>
	 * contains("abcdef","cde",0,6)
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @param fromIndex int
	 * @param size int
	 * @return boolean
	 */
	public static final boolean contains(final char[] ArraySource, final char[] Array, final int fromIndex, final int size) {
		return indexOf(ArraySource, Array, fromIndex, size) != Consts.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组包含在二维数组中<br/>
	 * 
	 * <pre>
	 * contains({"abc","defg","m1978"},"m1978")
	 * result:true
	 * </pre>
	 * @param ArraySource char[][]
	 * @param Array char[]
	 * @return boolean
	 */
	public static final boolean contains(final char[][] ArraySource, final char[] Array) {
		return indexOf(ArraySource, Array) != Consts.INDEX_NOT_FOUND;
	}

	// TODO trim ltrim rtrim清除两边空格 参考了String API 中的trim()方法
	/**
	 * 清除数组左侧空格<br/>
	 * 
	 * <pre>
	 * ltrim(" abcdef  ")
	 * result:abcdef__
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] ltrim(char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return ArraySource;
		int st = 0;
		while ((st < len) && (ArraySource[st] <= ' '))
			st++;
		if (st > 0) {
			final char[] ArrayNew = new char[len - st];
			System.arraycopy(ArraySource, st, ArrayNew, 0, len - st);
			return ArrayNew;
		}
		return ArraySource;
	}

	/**
	 * 清除数组右侧空格<br/>
	 * 
	 * <pre>
	 * rtrim(" abcdef ")
	 * result: abcdef
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] rtrim(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return ArraySource;
		while ((0 < len) && (ArraySource[len - 1] <= ' '))
			len--;
		if (len < ArraySource.length) {
			final char[] ArrayNew = new char[len];
			System.arraycopy(ArraySource, 0, ArrayNew, 0, len);
			return ArrayNew;
		}
		return ArraySource;
	}

	/**
	 * 清除数组两侧空格<br/>
	 * 
	 * <pre>
	 * trim(" abcdef ")
	 * result:abcdef
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] trim(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return ArraySource;
		int st = 0;
		while ((st < len) && (ArraySource[st] <= ' '))
			st++;
		while ((st < len) && (ArraySource[len - 1] <= ' '))
			len--;
		if ((st > 0) || (len < ArraySource.length)) {
			final char[] ArrayNew = new char[len - st];
			System.arraycopy(ArraySource, st, ArrayNew, 0, len - st);
			return ArrayNew;
		}
		return ArraySource;
	}

	// TODO toString 转成字符串
	/**
	 * 转成String<br/>
	 * 
	 * <pre>
	 * toString("abcdef")
	 * result:abcdef
	 * </pre>
	 * @param ArraySource char[]
	 * @return String
	 */
	public static final String toString(final char[] ArraySource) {
		if (ArraySource == null || ArraySource.length == Consts.Zero) return null;
		return new String(ArraySource);
	}

	// TODO isEmpty 判断数组是否为空
	/**
	 * 判断数组是否为空(null和length=0的时候都为空)<br/>
	 * 
	 * <pre>
	 * isEmpty("abcdef")
	 * result:false
	 * </pre>
	 * @param ArraySource char[]
	 * @return boolean
	 */
	public static final boolean isEmpty(final char[] ArraySource) {
		if (ArraySource == null || ArraySource.length == Consts.Zero) return true;
		return false;
	}

	// TODO isExistRepeat 判断是否含有重复数据
	/**
	 * 判断是否含有重复数据
	 * 
	 * <pre>
	 * isExistRepeat("abcdefc")
	 * result:true
	 * </pre>
	 * @param Array char[]
	 * @return boolean
	 */
	public static final boolean isExistRepeat(final char[] Array) {
		for (int i = 0, ii = 0, len = Array.length; i < len; i++)
			for (ii = i + 1; ii < len; ii++)
				/* 判断后面数组是否含此字符 */
				if (ElementEquals(Array[i], Array[ii])) return true;
		return false;
	}

	/**
	 * 判断是否含有重复数据(数组)
	 * 
	 * <pre>
	 * isExistRepeat({"abc","def","gh","def"})
	 * result:true
	 * </pre>
	 * @param Array char[][]
	 * @return boolean
	 */
	public static final boolean isExistRepeat(final char[]... Array) {
		for (int i = 0, ii = 0, len = Array.length; i < len; i++)
			for (ii = i + 1; ii < len; ii++)
				/*判断后面数组是否含此数组 */
				if (ArrayEquals(Array[i], Array[ii])) return true;
		return false;
	}

	// TODO split 分组
	/**
	 * 以某个字符为标准对字符数组进行分组<br/>
	 * 
	 * <pre>
	 * split("abcXefXXg",'X')
	 * 0:abc
	 * 1:ef
	 * 2:
	 * 3:g
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @return char[][]
	 */
	public static final char[][] split(final char[] ArraySource, final char c) {
		int lenSource;
		if ((lenSource = ArraySource.length) == Consts.Zero) return Consts.Array2Null;
		int sort;
		if ((sort = searchCount(ArraySource, c)) == Consts.Zero) {
			final char[][] resultArray = new char[1][];
			resultArray[0] = clone(ArraySource);
			return resultArray;
		}
		final char[][] resultArray = new char[sort + 1][];
		int i = 0, Index = 0, p = 0;
		while ((Index = indexOf(ArraySource, c, i)) > Consts.INDEX_NOT_FOUND) {
			resultArray[p++] = subArray(ArraySource, i, Index - i);
			i = Index + 1;
		}
		if (Index < lenSource) resultArray[p] = subArray(ArraySource, i, lenSource - i);
		return resultArray;
	}

	/**
	 * 以某个字符数组为标准对字符数组进行分组<br/>
	 * 
	 * <pre>
	 * split("abcXYefXYXYg","XY")
	 * 0:abc
	 * 1:ef
	 * 2:
	 * 3:g
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return char[][]
	 */
	public static final char[][] split(final char[] ArraySource, final char[] Array) {
		int lenSource, lenArray;
		if ((lenSource = ArraySource.length) == Consts.Zero || (lenArray = Array.length) == Consts.Zero) return Consts.Array2Null;
		int sort;
		if ((sort = searchCount(ArraySource, Array)) == Consts.Zero) {
			final char[][] resultArray = new char[1][];
			resultArray[0] = clone(ArraySource);
			return resultArray;
		}
		final char[][] resultArray = new char[sort + 1][];
		int i = 0, Index = 0, p = 0;
		while ((Index = indexOf(ArraySource, Array, i)) > Consts.INDEX_NOT_FOUND) {
			resultArray[p++] = subArray(ArraySource, i, Index - i);
			i = Index + lenArray;
		}
		if (Index < lenSource) resultArray[p] = subArray(ArraySource, i, lenSource - i);
		return resultArray;
	}

	/**
	 * 以N个字符数组为标准对字符数组进行分组<br/>
	 * 注意：切割标准数组是有优先级的，下标为0的优先级最高<br/>
	 * 如:{"ABC","AB"}，则"ABC"的优先级最高<br/>
	 * 
	 * <pre>
	 * split("abcXYefXYXYg",{"AB","XY"})
	 * 0:abc
	 * 1:ef
	 * 2:
	 * 3:g
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[][]
	 * @return char[][]
	 */
	public static final char[][] split(final char[] ArraySource, final char[][] Array) {
		int lenSource;
		if ((lenSource = ArraySource.length) == Consts.Zero || Array.length == Consts.Zero) return Consts.Array2Null;
		int Index;/* 先期当容量，后期作下标 */
		if ((Index = searchCount(ArraySource, Array)) == Consts.Zero) {
			final char[][] resultArray = new char[1][];
			resultArray[0] = clone(ArraySource);
			return resultArray;
		}
		final char[][] resultArray = new char[Index + 1][];
		int i = Index = 0, p = 0;/*, Findselect = 0;*/
		while ((Index = indexOf(ArraySource, Array, i)) != Consts.INDEX_NOT_FOUND) {
			resultArray[p++] = subArray(ArraySource, i, Index - i);
			i = Index + Array[PrivateArrayFindArray(ArraySource, Array, Index)].length;
		}
		if (Index < lenSource) resultArray[p] = subArray(ArraySource, i, lenSource - i);
		return resultArray;
	}

	// TODO reverse 数组的反转
	/**
	 * 得到数组完整的反转<br/>
	 * 
	 * <pre>
	 * reverse("abcdef")
	 * result:fedcba
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] reverse(final char[] ArraySource) {
		return reverse(ArraySource, 0, ArraySource.length);
	}

	/**
	 * 得到数组指定位置的范围内的反转进行超范围操作<br/>
	 * 请修改默认空元素 Consts.ElementFill<br/>
	 * 
	 * <pre>
	 * reverse("abcdef",3,5)
	 * result:abc  fed
	 * </pre>
	 * @param ArraySource char[]
	 * @param startIndex int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] reverse(final char[] ArraySource, final int startIndex, final int size) {
		int len;
		if (startIndex < 0 || (len = ArraySource.length) == Consts.Zero || startIndex >= len || size == Consts.Zero) return ArraySource;
		final char[] ArrayNew = new char[((startIndex + size) > len) ? (startIndex + size) : len];
		/* if ((startIndex + size) > len) size = len - startIndex;//如果范围超过长度，则自动选择总长度*/
		if (startIndex > 0) System.arraycopy(ArraySource, 0, ArrayNew, 0, startIndex);
		for (int i = 0; i < size; i++) {
			if (((startIndex + size) - i - 1) < len) {
				ArrayElementSet(ArrayNew, startIndex + i, ArraySource[(startIndex + size) - i - 1]);
				continue;
			}
			ArrayElementSet(ArrayNew, startIndex + i, Consts.ElementFill);/* 填充空 */
		}
		if ((startIndex + size) < len) System.arraycopy(ArraySource, startIndex + size, ArrayNew, startIndex + size, len - (startIndex + size));
		return ArrayNew;
	}

	/**
	 * 得到二维数组完整的反转<br/>
	 * 
	 * <pre>
	 * reverse({"ab","cd","ef","gh"})
	 * result:{"gh","ef","cd","ab"}
	 * </pre>
	 * @param ArraySource char[][]
	 * @return char[][]
	 */
	public static final char[][] reverse(final char[][] ArraySource) {
		return reverse(ArraySource, 0, ArraySource.length);
	}

	/**
	 * 得到二维数组指定位置的范围内的反转进行超范围操作<br/>
	 * 请修改默认空元素 Consts.ArrayNull<br/>
	 * 
	 * <pre>
	 * reverse({"ab","cd","ef","gh"},1,2)
	 * result:{"ab","ef","cd","gh"}
	 * </pre>
	 * @param ArraySource char[][]
	 * @param startIndex int
	 * @param size int
	 * @return char[][]
	 */
	public static final char[][] reverse(final char[][] ArraySource, final int startIndex, final int size) {
		int len;
		if (startIndex < 0 || (len = ArraySource.length) == Consts.Zero || startIndex >= len || size == Consts.Zero) return ArraySource;
		final char[][] ArrayNew = new char[((startIndex + size) > len) ? (startIndex + size) : len][0];
		/* if ((startIndex + size) > len) size = len - startIndex;//如果范围超过长度，则自动选择总长度*/
		if (startIndex > 0) System.arraycopy(ArraySource, 0, ArrayNew, 0, startIndex);
		for (int i = 0; i < size; i++) {
			if (((startIndex + size) - i - 1) < len) {
				ArrayElementSet(ArrayNew, startIndex + i, ArraySource[(startIndex + size) - i - 1]);
				continue;
			}
			ArrayElementSet(ArrayNew, startIndex + i, Consts.ArrayNull);/* 填充空 */
		}
		if ((startIndex + size) < len) System.arraycopy(ArraySource, startIndex + size, ArrayNew, startIndex + size, len - (startIndex + size));
		return ArrayNew;
	}

	/**
	 * 得到数组指定位置的范围内的反转(正常，非常正常，一切正常，符合数组正常，符合范围正常)还是先过期吧!!<br/>
	 * 
	 * <pre>
	 * reverse_Deprecated("abcdef",2,3)
	 * result:abedcf
	 * </pre>
	 * @param ArraySource char[]
	 * @param startIndex int
	 * @param size int
	 * @return char[]
	 */
	@Deprecated
	public static final char[] reverse_Deprecated(final char[] ArraySource, final int startIndex, int size) {
		int len;
		if (startIndex < 0 || (len = ArraySource.length) == Consts.Zero || startIndex >= len || size == Consts.Zero) return ArraySource;
		final char[] ArrayNew = new char[len];
		if ((startIndex + size) > len) size = len - startIndex;/* 如果范围超过长度，则自动选择总长度*/
		if (startIndex > 0) System.arraycopy(ArraySource, 0, ArrayNew, 0, startIndex);
		for (int i = 0; i < size; i++)
			ArrayElementSet(ArrayNew, startIndex + i, ArraySource[(startIndex + size) - i - 1]);
		if ((startIndex + size) < len) System.arraycopy(ArraySource, startIndex + size, ArrayNew, startIndex + size, len - (startIndex + size));
		return ArrayNew;
	}

	/**
	 * 得到数组完整的反转<br/>
	 * 
	 * <pre>
	 * reverseDeprecated("abcdef")
	 * result:fedcba
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	@Deprecated
	public static final char[] reverseDeprecated(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return ArraySource;
		final char[] ArrayNew = new char[len];
		for (int i = 0; i < len; i++)
			ArrayElementSet(ArrayNew, i, ArraySource[len - i - 1]);
		return ArrayNew;
	}

	//TODO IndexOf 查找字符在字符数组的位置(正序)混乱型
	/**
	 * 乱序型查找，暂未开发 大小写？？？！！！
	 * @param ArraySource char[]
	 * @param c char
	 * @return int
	 */
	public static final int IndexOf(final char[] ArraySource, final char c) {
		return Consts.Zero;
	}

	/**
	 * 查找字符数组在字符数组的位置(默认从0位置查) 混乱型<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY","YX")
	 * result:1
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @return int
	 */
	public static final int IndexOf(final char[] ArraySource, final char[] ArraySearch) {
		if (ArraySource.length == ArraySearch.length && ArrayEquals(ArraySource, ArraySearch)) return Consts.Zero;
		return IndexOf(ArraySource, ArraySearch, 0);
	}

	/**
	 * 查找数组在字符数组的位置 (从fromIndex开始查找)到数组结尾 混乱型<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY","YX",2)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @param fromIndex int
	 * @return int
	 */
	public static final int IndexOf(final char[] ArraySource, final char[] ArraySearch, final int fromIndex) {
		return IndexOf(ArraySource, ArraySearch, fromIndex, ArraySource.length);
	}

	/**
	 * 查找数组在字符数组的位置 (从fromIndex开始查找) 混乱型<br/>
	 * 
	 * <pre>
	 * IndexOf("aXYcdeXYfgXY","YX",2)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @param fromIndex int
	 * @return int
	 */
	public static final int IndexOf(final char[] ArraySource, final char[] ArraySearch, final int fromIndex, final int size) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || fromIndex < 0 || fromIndex >= len || size <= Consts.Zero) return Consts.INDEX_NOT_FOUND;
		len = ((fromIndex + size) >= len) ? len - fromIndex : size;
		int lenSearch;
		if ((lenSearch = ArraySearch.length) == Consts.Zero) return Consts.INDEX_NOT_FOUND;
		for (int i = fromIndex; i < (len - lenSearch + 1); i++)
			if (PrivateArrayFindArrayChaos(ArraySource, ArraySearch, i)) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	/*IndexOf(final char[] ArraySource, final char[][] ArraySearch, final int fromIndex, final int size)*/
	/**
	 * 查找N个数组在字符数组的位置 (从fromIndex开始查找)到数组结尾 混乱型<br/>
	 * 
	 * <pre>
	 * IndexOf("aXYcdeXYfgXY",{"AB","YX"})
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[][]
	 * @return int
	 */
	public static final int IndexOf(final char[] ArraySource, final char[][] ArraySearch) {
		return IndexOf(ArraySource, ArraySearch, 0);
	}

	/**
	 * 查找N个数组在字符数组的位置 (从fromIndex开始查找)到数组结尾 混乱型<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY",{"AB","YX"},2)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[][]
	 * @param fromIndex int
	 * @return int
	 */
	public static final int IndexOf(final char[] ArraySource, final char[][] ArraySearch, final int fromIndex) {
		return IndexOf(ArraySource, ArraySearch, fromIndex, ArraySource.length);
	}

	/**
	 * 查找N个数组在字符数组的位置 (从fromIndex开始查找) 混乱型<br/>
	 * 
	 * <pre>
	 * IndexOf("aXYcdeXYfgXY",{"AB","YX"},2,10)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[][]
	 * @param fromIndex int
	 * @param size int
	 * @return int
	 */
	public static final int IndexOf(final char[] ArraySource, final char[][] ArraySearch, final int fromIndex, final int size) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || fromIndex < 0 || fromIndex >= len || size <= Consts.Zero) return Consts.INDEX_NOT_FOUND;
		len = ((fromIndex + size) >= len) ? len - fromIndex : size;
		if (ArraySearch.length == Consts.Zero) return Consts.INDEX_NOT_FOUND;
		for (int i = fromIndex; i < len; i++)
			if (PrivateArrayFindArrayChaos(ArraySource, ArraySearch, i) != Consts.INDEX_NOT_FOUND) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	// TODO indexOf 查找字符在字符数组的位置(正序)
	/*indexOf(final char[] ArraySource, final char c, final int fromIndex,final int size)*/
	/**
	 * 查找字符在字符数组的位置(默认从0位置查)<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY",'X')
	 * result:1
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char c) {
		return indexOf(ArraySource, c, 0);
	}

	/**
	 * 查找字符在字符数组的位置 (从fromIndex开始查找)到数组结尾<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY",'X',3)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @param fromIndex int
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char c, final int fromIndex) {
		return indexOf(ArraySource, c, fromIndex, ArraySource.length);
	}

	/**
	 * 查找字符在字符数组的位置 (从fromIndex开始查找)<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY",'X',3)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @param fromIndex int
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char c, final int fromIndex, final int size) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || fromIndex < 0 || fromIndex >= len || size <= Consts.Zero) return Consts.INDEX_NOT_FOUND;
		len = ((fromIndex + size) >= len) ? len - fromIndex : size;
		for (int i = fromIndex; i < len; i++)
			if (ElementEquals(ArraySource[i], c)) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	/*
	 * indexOf(final char[] ArraySource, final char[] ArraySearch, final int fromIndex,final int size)
	*/
	/**
	 * 查找字符数组在字符数组的位置(默认从0位置查)<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY","XY")
	 * result:1
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char[] ArraySearch) {
		if (ArraySource.length == ArraySearch.length && ArrayEquals(ArraySource, ArraySearch)) return Consts.Zero;
		return indexOf(ArraySource, ArraySearch, 0);
	}

	/**
	 * 查找数组在字符数组的位置 (从fromIndex开始查找)到数组结尾<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY","XY",2)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @param fromIndex int
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char[] ArraySearch, final int fromIndex) {
		return indexOf(ArraySource, ArraySearch, fromIndex, ArraySource.length);
	}

	/**
	 * 查找数组在字符数组的位置 (从fromIndex开始查找)<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY","XY",2)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @param fromIndex int
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char[] ArraySearch, final int fromIndex, final int size) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || fromIndex < 0 || fromIndex >= len || size <= Consts.Zero) return Consts.INDEX_NOT_FOUND;
		len = ((fromIndex + size) >= len) ? len - fromIndex : size;
		int lenSearch;
		if ((lenSearch = ArraySearch.length) == Consts.Zero) return Consts.INDEX_NOT_FOUND;
		for (int i = fromIndex; i < (len - lenSearch + 1); i++)
			if (PrivateArrayFindArray(ArraySource, ArraySearch, i)) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	/*
	 * indexOf(final char[] ArraySource, final char[][] ArraySearch, final int fromIndex,final int size)
	*/
	/**
	 * 查找N个数组在字符数组的位置 (从fromIndex开始查找)到数组结尾<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY",{"AB","XY"})
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[][]
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char[][] ArraySearch) {
		return indexOf(ArraySource, ArraySearch, 0);
	}

	/**
	 * 查找N个数组在字符数组的位置 (从fromIndex开始查找)到数组结尾<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY",{"AB","XY"},2)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[][]
	 * @param fromIndex int
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char[][] ArraySearch, final int fromIndex) {
		return indexOf(ArraySource, ArraySearch, fromIndex, ArraySource.length);
	}

	/**
	 * 查找N个数组在字符数组的位置 (从fromIndex开始查找)<br/>
	 * 
	 * <pre>
	 * indexOf("aXYcdeXYfgXY",{"AB","XY"},2,10)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[][]
	 * @param fromIndex int
	 * @param size int
	 * @return int
	 */
	public static final int indexOf(final char[] ArraySource, final char[][] ArraySearch, final int fromIndex, final int size) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || fromIndex < 0 || fromIndex >= len || size <= Consts.Zero) return Consts.INDEX_NOT_FOUND;
		len = ((fromIndex + size) >= len) ? len - fromIndex : size;
		if (ArraySearch.length == Consts.Zero) return Consts.INDEX_NOT_FOUND;
		for (int i = fromIndex; i < len; i++)
			if (PrivateArrayFindArray(ArraySource, ArraySearch, i) != Consts.INDEX_NOT_FOUND) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组在二维数组中的位置<br/>
	 * 
	 * <pre>
	 * indexOf({"abc","defg","m1978"},"m1978")
	 * result:true
	 * </pre>
	 * @param ArraySource char[][]
	 * @param Array char[]
	 * @return int
	 */
	public static final int indexOf(final char[][] ArraySource, final char[] Array) {
		int len, ArrayLen;
		if ((len = ArraySource.length) == Consts.Zero || (ArrayLen = Array.length) == Consts.Zero) return Consts.INDEX_NOT_FOUND;/* 没有发现可比性*/
		for (int i = 0; i < len; i++)
			if (ArraySource[i].length == ArrayLen && ArrayEquals(ArraySource[i], Array)) return i;
		return -1;
	}

	// TODO indexOfLast 查找字符在字符数组的位置(倒序)
	/*
	 * indexOfLast(final char[] ArraySource, final char c, final int maxIndex,final int size)
	 */
	/**
	 * 从逆序开始搜索,搜到就返回当前的index否则返回 Consts.INDEX_NOT_FOUND(-1) (默认为数组尾向前)<br/>
	 * 
	 * <pre>
	 * indexOfLast("aXcdeXfgXh",'X')
	 * result:8
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @return int
	 */
	public static final int indexOfLast(final char[] ArraySource, final char c) {
		return indexOfLast(ArraySource, c, ArraySource.length - 1);
	}

	/**
	 * 从逆序开始搜索,搜到就返回当前的index否则返回 Consts.INDEX_NOT_FOUND(-1)(以maxIndex定位向前)<br/>
	 * 
	 * <pre>
	 * lastIndexOf("aXcdeXfgX",'X',6)
	 * result:5
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @param maxIndex int
	 * @return int
	 */
	public static final int indexOfLast(final char[] ArraySource, final char c, final int maxIndex) {
		return indexOfLast(ArraySource, c, maxIndex, maxIndex + 1);
	}

	/**
	 * 从逆序开始搜索,搜到就返回当前的index否则返回 Consts.INDEX_NOT_FOUND(-1)(以maxIndex定位向前)指定范围内<br/>
	 * 
	 * <pre>
	 * indexOfLast("aXcdeXfgX",'X',6,3)
	 * result:5
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @param maxIndex int
	 * @param size int
	 * @return int
	 */
	public static final int indexOfLast(final char[] ArraySource, final char c, final int maxIndex, final int size) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero || maxIndex < 0 || maxIndex >= len || size <= Consts.Zero) return Consts.INDEX_NOT_FOUND;
		len = ((maxIndex - size) > 0) ? maxIndex - size + 1 : 0;
		for (int i = maxIndex; i >= len; i--)
			if (ElementEquals(ArraySource[i], c)) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	/*
	 * indexOfLast(final char[] ArraySource, final char[] ArraySearch, int maxIndex,final int size)
	*/
	/**
	 * 从逆序开始搜索,搜到就返回当前的index否则返回 Consts.INDEX_NOT_FOUND(-1) (默认为数组尾向前)<br/>
	 * 
	 * <pre>
	 * indexOfLast("aXYcdeXYfgXY","XY")
	 * result:10
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @return int
	 */
	public static final int indexOfLast(final char[] ArraySource, final char[] ArraySearch) {
		if (ArraySource.length == ArraySearch.length) if (ArrayEquals(ArraySource, ArraySearch)) return 0;
		return indexOfLast(ArraySource, ArraySearch, ArraySource.length - 1);
	}

	/**
	 * 从逆序开始搜索,搜到就返回当前的index否则返回 Consts.INDEX_NOT_FOUND(-1)(以maxIndex定位向前)<br/>
	 * 
	 * <pre>
	 * indexOfLast("aXYcdeXYfgXY","XY",8)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @param maxIndex int
	 * @return int
	 */
	public static final int indexOfLast(final char[] ArraySource, final char[] ArraySearch, int maxIndex) {
		return indexOfLast(ArraySource, ArraySearch, maxIndex, maxIndex + 1);
	}

	/**
	 * 从逆序开始搜索,搜到就返回当前的index否则返回 Consts.INDEX_NOT_FOUND(-1)(以maxIndex定位向前)指定范围<br/>
	 * 
	 * <pre>
	 * indexOfLast("aXYcdeXYfgXY","XY",8,5)
	 * result:6
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArraySearch char[]
	 * @param maxIndex int
	 * @param size int
	 * @return int
	 */
	public static final int indexOfLast(final char[] ArraySource, final char[] ArraySearch, int maxIndex, final int size) {
		int len, lenSearch;
		if ((len = ArraySource.length) == Consts.Zero || (lenSearch = ArraySearch.length) == Consts.Zero || maxIndex < 0 || maxIndex >= len || maxIndex < lenSearch || size <= Consts.Zero
				|| size < lenSearch) return Consts.INDEX_NOT_FOUND;
		len = ((maxIndex - size) > 0) ? maxIndex - size + 1 : 0;
		for (int i = maxIndex - lenSearch + 1; i >= len; i--)
			if (PrivateArrayFindArray(ArraySource, ArraySearch, i)) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	// TODO replace 替换一个
	/**
	 * 替换单个 单个字符转成单个字符(默认从第一个替换)<br/>
	 * 
	 * <pre>
	 * replace("abcdebf",'b','Y')
	 * result:aYcdebf
	 * </pre>
	 * @param ArraySource char[]
	 * @param oldChar char
	 * @param newChar char
	 * @return char[]
	 */
	public static final char[] replace(final char[] ArraySource, final char oldChar, final char newChar) {
		return replace(ArraySource, oldChar, newChar, 0);
	}

	/**
	 * 替换单个 单个字符转成单个字符(指定位置后)<br/>
	 * 
	 * <pre>
	 * replace("abcdebf",'b','Y',3)
	 * result:abcdeYf
	 * </pre>
	 * @param ArraySource char[]
	 * @param oldChar char
	 * @param newChar char
	 * @param startIndex int
	 * @return char[]
	 */
	public static final char[] replace(final char[] ArraySource, final char oldChar, final char newChar, final int startIndex) {
		if (ArraySource.length == Consts.Zero) return ArraySource;
		int Index;
		if ((Index = indexOf(ArraySource, oldChar, startIndex)) != Consts.INDEX_NOT_FOUND) ArrayElementSet(ArraySource, Index, newChar);
		return ArraySource;
	}

	// TODO replaceAll 全部替换
	/**
	 * 全部替换 单个字符转成单个字符<br/>
	 * 
	 * <pre>
	 * replaceAll("abcdebf",'b','X')
	 * result:aXcdeXf
	 * </pre>
	 * @param ArraySource char[]
	 * @param oldChar char
	 * @param newChar char
	 * @return char[]
	 */
	public static final char[] replaceAll(final char[] ArraySource, final char oldChar, final char newChar) {
		if (ArraySource.length == Consts.Zero) return ArraySource;
		int Index = 0;
		while (((Index = indexOf(ArraySource, oldChar, Index)) != Consts.INDEX_NOT_FOUND))
			ArrayElementSet(ArraySource, Index++, newChar);
		return ArraySource;
	}

	/**
	 * 全部替换 单个字符转成字符数组<br/>
	 * 
	 * <pre>
	 * replaceAll("abcdbe",'b',"XY")
	 * result:aXYcdXYe
	 * </pre>
	 * @param ArraySource char[]
	 * @param oldChar char
	 * @param ArrayNew char[]
	 * @return char[]
	 */
	public static final char[] replaceAll(final char[] ArraySource, final char oldChar, final char[] ArrayNew) {
		int lenSource, lenNew;
		if ((lenSource = ArraySource.length) == Consts.Zero || (lenNew = ArrayNew.length) == Consts.Zero) return ArraySource;
		int count;
		if ((count = searchCount(ArraySource, oldChar)) == Consts.Zero) return ArraySource;
		final char[] ResultArrayNew = new char[lenSource + (lenNew - 1) * count];
		int i = 0, Index = 0, p = 0;
		while ((Index = indexOf(ArraySource, oldChar, i)) > Consts.INDEX_NOT_FOUND) {
			System.arraycopy(ArraySource, i, ResultArrayNew, p, Index - i);
			p += (Index - i);
			System.arraycopy(ArrayNew, 0, ResultArrayNew, p, lenNew);
			p += lenNew;
			i = Index + 1;
		}
		if (i < lenSource) System.arraycopy(ArraySource, i, ResultArrayNew, p, lenSource - i);
		return ResultArrayNew;
	}

	/**
	 * 把数组中的数组替换成单字符<br/>
	 * 
	 * <pre>
	 * replaceAll("abcdefbcde","bcd",'X')
	 * result:aXefXe
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayOld char[]
	 * @param newChar char
	 * @return char[]
	 */
	public static final char[] replaceAll(final char[] ArraySource, final char[] ArrayOld, final char newChar) {
		int lenSource, lenOld;
		if (((lenSource = ArraySource.length) < (lenOld = ArrayOld.length))) return ArraySource;
		int count = Consts.Zero;
		if ((count = searchCount(ArraySource, ArrayOld)) == Consts.Zero) return ArraySource;
		final char[] ArrayNew = new char[lenSource - (lenOld - 1) * count];
		for (int i = 0, p = 0; i < lenSource; i++) {
			if (PrivateArrayFindArray(ArraySource, ArrayOld, i)) {
				/* 替换数组*/
				ArrayElementSet(ArrayNew, p++, newChar);
				i += (lenOld - 1);
			} else {
				ArrayElementSet(ArrayNew, p++, ArraySource[i]);
			}
		}
		return ArrayNew;
	}

	/**
	 * 把数组中的数组替换成数组<br/>
	 * 
	 * <pre>
	 * replaceAll("abcdefbcde","bcd","XY")
	 * result:aXYefXYe
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayOld char[]
	 * @param ArrayNew char[]
	 * @return char[]
	 */
	public static final char[] replaceAll(final char[] ArraySource, final char[] ArrayOld, final char[] ArrayNew) {
		int lenSource, lenOld;
		if (((lenSource = ArraySource.length) < (lenOld = ArrayOld.length))) return ArraySource;
		int count = Consts.Zero;
		if ((count = searchCount(ArraySource, ArrayOld)) == Consts.Zero) return ArraySource;
		int lenNew = ArrayNew.length;
		final char[] ResultArrayNew = new char[lenSource - (lenOld - lenNew) * count];
		for (int i = 0, p = 0; i < lenSource; i++) {
			if (PrivateArrayFindArray(ArraySource, ArrayOld, i)) {
				/* 替换数组*/
				System.arraycopy(ArrayNew, 0, ResultArrayNew, p, lenNew);
				p += lenNew;
				i += (lenOld - 1);
			} else {
				ArrayElementSet(ResultArrayNew, p++, ArraySource[i]);
			}
		}
		return ResultArrayNew;
	}

	// TODO clone 拷贝数组
	/**
	 * 拷贝数组<br/>
	 * 
	 * <pre>
	 * clone("abcdef")
	 * result:abcdef
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] clone(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return Consts.ArrayNull;
		final char[] ResultArrayNew = new char[len];
		System.arraycopy(ArraySource, 0, ResultArrayNew, 0, len);
		return ResultArrayNew;
	}

	/**
	 * 拷贝字符<br/>
	 * 
	 * <pre>
	 * clone('X')
	 * result:X
	 * </pre>
	 * @param c char
	 * @return char
	 */
	public static final char clone(final char c) {
		final char d = c;
		return d;
	}

	// TODO isSameLength 判断多个个数组的长度是否相等
	/**
	 * 判断多个个数组的长度是否相等<br/>
	 * 
	 * <pre>
	 * isSameLength("abcdef","feaabc","123456")
	 * result:true
	 * </pre>
	 * @param ArraySource char[][]
	 * @return boolean
	 */
	public static final boolean isSameLength(final char[]... ArraySource) {
		int len;
		if ((len = ArraySource.length) < 2) return false;/* 没有发现可比性*/
		while (--len > 0)
			if (ArraySource[len].length != ArraySource[0].length) return false;
		return true;
	}

	// TODO LenMaxArray LenMinArray 获取数组中最长(短)的数组
	/**
	 * 获取数组中最长的数组<br/>
	 * 
	 * <pre>
	 * LenMaxArray("abcdef","bcd","abcde")
	 * result:abcdef
	 * </pre>
	 * @param ArraySource char[]...
	 * @return char[]
	 */
	public static final char[] LenMaxArray(final char[]... ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return Consts.ArrayNull;
		int p = 0;
		for (int i = 0, Linelen = 0; i < len; i++)
			if (ArraySource[i].length > Linelen) Linelen = ArraySource[(p = i)].length;
		return ArraySource[p];
	}

	/**
	 * 获取数组中最短的数组<br/>
	 * 
	 * <pre>
	 * LenMinArray("abcdef","bcd","abcde")
	 * result:bcd
	 * </pre>
	 * @param ArraySource char[]...
	 * @return char[]
	 */
	public static final char[] LenMinArray(final char[]... ArraySource) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return Consts.ArrayNull;
		int p = 0;
		for (int i = 1, Linelen = ArraySource[0].length; i < len; i++)
			if (ArraySource[i].length < Linelen) Linelen = ArraySource[(p = i)].length;
		return ArraySource[p];
	}

	/**
	 * 数组指定位置插入一个字符<br/>
	 * 
	 * <pre>
	 * insert("abcdef",2,'X')
	 * result:abXcdef
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int
	 * @param c char
	 * @return char[]
	 */
	public static final char[] insert(final char[] ArraySource, final int point, final char c) {
		int len;
		if (point < 0 || (len = ArraySource.length) == Consts.Zero || point > len) return ArraySource;
		final char[] ResultArrayNew = new char[len + 1];
		if (point > 0) System.arraycopy(ArraySource, 0, ResultArrayNew, 0, point);
		ArrayElementSet(ResultArrayNew, point, c);
		if (point < len) System.arraycopy(ArraySource, point, ResultArrayNew, point + 1, len - point);
		return ResultArrayNew;
	}

	/**
	 * 数组指定位置插入一个字符数组，如果 位置>=长度，则返回原数组<br/>
	 * 
	 * <pre>
	 * insert("abcdef",2,"XY")
	 * result:abXYcdef
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int
	 * @param ArrayN char[]
	 * @return char[]
	 */
	public static final char[] insert(final char[] ArraySource, final int point, final char[] ArrayN) {
		int len, lenN;
		if (point < 0 || (len = ArraySource.length) == Consts.Zero || point > len || (lenN = ArrayN.length) == Consts.Zero) return ArraySource;
		/* final char[] ResultArrayNew = new char[((point > len)?len + lenN:point+lenN)]; //后期如果允许（数组+空格+数组(新)）*/
		final char[] ResultArrayNew = new char[len + lenN];
		if (point > 0) System.arraycopy(ArraySource, 0, ResultArrayNew, 0, point);
		System.arraycopy(ArrayN, 0, ResultArrayNew, point, lenN);
		if (point < len) System.arraycopy(ArraySource, point, ResultArrayNew, point + lenN, len - point);
		return ResultArrayNew;
	}

	// TODO subArray 截取数组
	/**
	 * 截取数组 如果 point + size 大于数组的长度,则取point之后的所有数据<br/>
	 * 
	 * <pre>
	 * subArray("abcdef",1,3)
	 * result:bcd
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] subArray(final char[] ArraySource, final int point, final int size) {
		int len;
		if (size <= Consts.Zero || point < 0 || (len = ArraySource.length) == Consts.Zero || point >= len) return Consts.ArrayNull;
		final char[] Array = new char[((point + size) > len) ? len - point : size];
		System.arraycopy(ArraySource, point, Array, 0, Array.length);
		return Array;
	}

	/**
	 * 强制截取数组 如果 point + size 大于数组的长度,则取point之后的所有数据余下填充空数据<br/>
	 * 
	 * <pre>
	 * subArrayForced("abcdef",5,3)
	 * result:"f\0\0"
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] subArrayForced(final char[] ArraySource, final int point, final int size) {
		int len;
		if (size <= Consts.Zero || point < 0 || (len = ArraySource.length) == Consts.Zero || point >= len) return Consts.ArrayNull;
		final char[] ResultArrayNew = new char[size];
		int readlen = ((point + size) > len) ? len - point : size;
		System.arraycopy(ArraySource, point, ResultArrayNew, 0, readlen);
		for (int i = 1; i < (size - readlen); i++)
			ResultArrayNew[readlen + i] = Consts.ElementFill;
		return ResultArrayNew;
	}

	//TODO subArrayRnd 数组的随机子数组，允许限制长度范围
	/**
	 * 数组的随机子数组<br/>
	 * 
	 * <pre>
	 * subArrayRnd("abcdefgh")
	 * result:bcd
	 * result:ef
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] subArrayRnd(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) == 0) return Consts.ArrayNull;
		int Size = UtilTool.getRndInt(0, len);
		return subArrayRnd(ArraySource, Size, Size);
	}

	/**
	 * 数组的随机子数组，允许限制长度范围<br/>
	 * Size为0时，则不限制范围<br/>
	 * 
	 * <pre>
	 * subArrayRnd("abcdefgh",3)
	 * result:bcd
	 * subArrayRnd("abcdefgh",0)
	 * result:ef
	 * </pre>
	 * @param ArraySource char[]
	 * @param Size int
	 * @return char[]
	 */
	public static final char[] subArrayRnd(final char[] ArraySource, final int Size) {
		int len;
		if (Size < 0 || (len = ArraySource.length) == 0) return Consts.ArrayNull;
		if (len <= Size) return ArraySource;
		return subArrayRnd(ArraySource, Size, Size);
	}

	/**
	 * 数组的随机子数组，允许限制长度范围<br/>
	 * minSize,maxSize为0时，则不限制最大与最小范围<br/>
	 * 
	 * <pre>
	 * subArrayRnd("abcdefgh",3,5)
	 * result:bcd
	 * </pre>
	 * @param ArraySource char[]
	 * @param minSize int
	 * @param maxSize int
	 * @return char[]
	 */
	public static final char[] subArrayRnd(final char[] ArraySource, final int minSize, final int maxSize) {
		int len;
		if (maxSize < 0 || minSize < 0 || (len = ArraySource.length) == 0) return Consts.ArrayNull;
		if (minSize >= len || maxSize >= len) return ArraySource;
		if (maxSize > 0 && minSize > 0 && maxSize < minSize) return Consts.ArrayNull;
		int size;
		/*		
		if (maxSize == 0) {
			if (minSize == 0) {
				size = UtilTool.getRndInt(1, len);
			} else {
				size = UtilTool.getRndInt(minSize, len);
			}
		} else {
			if (minSize == 0) {
				size = UtilTool.getRndInt(1, maxSize);
			} else {
				size = UtilTool.getRndInt(minSize, maxSize);
			}
		}*/
		/*size = maxSize == 0 ? minSize == 0 ? UtilTool.getRndInt(1, len) : UtilTool.getRndInt(minSize, len) : minSize == 0 ? UtilTool.getRndInt(1, maxSize) : UtilTool.getRndInt(minSize, maxSize);*/
		size = UtilTool.getRndInt((minSize == 0) ? 1 : minSize, (maxSize == 0) ? len : maxSize);
		return subArray(ArraySource, UtilTool.getRndInt(0, len - size), size);
	}

	/**
	 * 得到数组的前半部分<br/>
	 * 
	 * <pre>
	 * subArray$2_1("abcdefg")
	 * result:abc
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] subArray$2_1(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) < 2) return ArraySource;
		final char[] ArrayNew = new char[len >> 1];
		System.arraycopy(ArraySource, 0, ArrayNew, 0, ArrayNew.length);
		return ArrayNew;
	}

	/**
	 * 得到数组的后半部分<br/>
	 * 
	 * <pre>
	 * subArray$2_2("abcdefg")
	 * result:defg
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] subArray$2_2(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) < 2) return ArraySource;
		final char[] ArrayNew = new char[(len % 2 == 0) ? len >> 1 : ((len >> 1) + 1)];
		System.arraycopy(ArraySource, len >> 1, ArrayNew, 0, ArrayNew.length);
		return ArrayNew;
	}

	/**
	 * 得到数组的几分之几的部分<br/>
	 * 
	 * <pre>
	 * subArray$Part("abcdefg",3,1)
	 * result:ab
	 * subArray$Part("abcdefg",3,2)
	 * result:cd
	 * subArray$Part("abcdefg",3,3)
	 * result:efg
	 * </pre>
	 * @param ArraySource char[]
	 * @param partSort int
	 * @param part int
	 * @return char[]
	 */
	public static final char[] subArray$Part(final char[] ArraySource, final int partSort, final int part) {
		int len;
		if (partSort <= 0 || part <= 0 || partSort < part || (len = ArraySource.length) < partSort) {
			if (part == 1) return ArraySource;
			return Consts.ArrayNull;
		}
		int size, point;
		if ((len % partSort > 0) && part == partSort) {
			size = ((len - (len % partSort)) / partSort) + (len % partSort);
			point = (part - 1) * ((len - (len % partSort)) / partSort);
		} else {
			size = len / partSort;
			point = (part - 1) * size;
		}
		final char[] ArrayNew = new char[size];
		System.arraycopy(ArraySource, point, ArrayNew, 0, size);
		return ArrayNew;
	}

	//TODO page 分页，同split相似。split以字符或多个字符进行拆分。page以数量进行拆分
	/**
	 * 对数组进行分页
	 * 
	 * <pre>
	 * page   ("abcdefg",3)
	 * result:{"abc","def","g"}
	 * </pre>
	 * @param ArraySource char[]
	 * @param size int
	 * @return char[][]
	 */
	public static final char[][] page(final char[] ArraySource, final int size) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return Consts.Array2Null;
		if (size <= Consts.Zero || size >= len) return toArray(ArraySource);
		int pagesort = (len % size == 0) ? len / size : (len - len % size) / size + 1;
		final char[][] resultArray = new char[pagesort][];
		for (int i = 1; i <= pagesort; i++)
			resultArray[i - 1] = subArray(ArraySource, (i - 1) * size, size);
		return resultArray;
	}

	/**
	 * Recombination[重组] 检索数组，N个位置(不规则，可重复)的字符。组成新的数组(未发现，则跳过)<br/>
	 * 
	 * <pre>
	 * Recom("abcdef",0,10,2,4,60,0)
	 * result:acea
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int[]
	 * @return char[]
	 */
	public static final char[] Recom(final char[] ArraySource, final int... point) {
		int len, lenPoint;
		if ((len = ArraySource.length) == Consts.Zero || (lenPoint = point.length) == Consts.Zero) return Consts.ArrayNull;
		int count = Consts.Zero;
		while (--lenPoint > Consts.INDEX_NOT_FOUND)
			if (point[lenPoint] > Consts.INDEX_NOT_FOUND && point[lenPoint] < len) count++;
		if (count == Consts.Zero) return Consts.ArrayNull;
		final char[] ResultArray = new char[count];
		for (lenPoint = point.length; lenPoint > 0; lenPoint--)
			if (point[lenPoint - 1] >= 0 && point[lenPoint - 1] < len) ArrayElementSet(ResultArray, --count, ArraySource[point[lenPoint - 1]]);
		return ResultArray;
	}

	/**
	 * RecombinationCompulsory[强制重组] 检索数组N个位置(不规则，可重复)的字符。<br/>
	 * 组成新的数组(未发现，则置填充)<br/>
	 * 
	 * <pre>
	 * RecomCompulsory("abcdef",0,10,2,4,60,0)
	 * result:a ce a
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int[]
	 * @return char[]
	 */
	public static final char[] RecomCompulsory(final char[] ArraySource, final int... point) {
		int len, lenPoint;
		if ((len = ArraySource.length) == Consts.Zero || (lenPoint = point.length) == Consts.Zero) return Consts.ArrayNull;
		final char[] ResultArrayNew = new char[lenPoint];
		while (--lenPoint > Consts.INDEX_NOT_FOUND)
			if (point[lenPoint] > Consts.INDEX_NOT_FOUND && point[lenPoint] < len) {
				ArrayElementSet(ResultArrayNew, lenPoint, ArraySource[point[lenPoint]]);
			} else {
				ArrayElementSet(ResultArrayNew, lenPoint, Consts.ElementFill);
			}
		return ResultArrayNew;
	}

	// TODO removeAll remove 移除

	/**
	 * 数组移除N个位置的元素(允许重复下标，在运行时删除重复下标)<br/>
	 * <font color='red'>注意：N > 2 如果 N<=2 时将调重载其它方法</font><br/>
	 * 
	 * <pre>
	 * remove("abcdefg",1,3,5,3)
	 * result:aceg
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int[]
	 * @return char[]
	 */
	public static final char[] remove(final char[] ArraySource, final int... point) {
		int len, lenPoint;
		int[] nPoint;
		if ((len = ArraySource.length) == Consts.Zero || (lenPoint = (nPoint = UtilSuffix.clearRepeat(point)).length) == Consts.Zero) return ArraySource;
		int count = Consts.Zero;
		while (--lenPoint > Consts.INDEX_NOT_FOUND)
			if (nPoint[lenPoint] > Consts.INDEX_NOT_FOUND && nPoint[lenPoint] < len) count++;
		if (count == Consts.Zero) return ArraySource;
		final char[] ResultArrayNew = new char[len - count];
		/* 把lenPoint换成其它含义的变量 */
		for (lenPoint = count = 0; lenPoint < len; lenPoint++)
			if (!UtilSuffix.contains(nPoint, lenPoint)) ArrayElementSet(ResultArrayNew, count++, ArraySource[lenPoint]);
		return ResultArrayNew;
	}

	/**
	 * 移除数组中某位置的单元<br/>
	 * 
	 * <pre>
	 * remove("abcdbef",2)
	 * result:abdef
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int
	 * @return char[]
	 */
	public static final char[] remove(final char[] ArraySource, final int point) {
		return remove(ArraySource, point, 1);
	}

	/**
	 * 移除数组中某位置，连续长度的单元<br/>
	 * 
	 * <pre>
	 * remove("abcdefg",2,3)
	 * result:abfg
	 * </pre>
	 * @param ArraySource char[]
	 * @param point int
	 * @param size int
	 * @return char[]
	 */
	public static final char[] remove(final char[] ArraySource, final int point, final int size) {
		int len;
		if (size < 1 || point < 0 || (len = ArraySource.length) == Consts.Zero || point >= len || (point + size) > len) return ArraySource;
		final char[] ResultArrayNew = new char[point + (len - (point + size))];
		if (point > 0) System.arraycopy(ArraySource, 0, ResultArrayNew, 0, point);
		if (len - (point + size) > 0) System.arraycopy(ArraySource, point + size, ResultArrayNew, point, len - (point + size));
		return ResultArrayNew;
	}

	/**
	 * 移走字符数组中的某个字符(全部)<br/>
	 * 
	 * <pre>
	 * removeAll("abcdXefgX",'X')
	 * result:abcdefg
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @return char[]
	 */
	public static final char[] removeAll(final char[] ArraySource, final char c) {
		int lenSource;
		if ((lenSource = ArraySource.length) == Consts.Zero) return ArraySource;
		int count;
		if ((count = searchCount(ArraySource, c)) == Consts.Zero) return ArraySource;
		final char[] ResultArrayNew = new char[lenSource - count];
		for (int i = 0, p = 0; i < lenSource; i++)
			if (!ElementEquals(ArraySource[i], c)) ArrayElementSet(ResultArrayNew, p++, ArraySource[i]);
		return ResultArrayNew;
	}

	/**
	 * 移走字符数组中的某个字符数组(全部)<br/>
	 * 
	 * <pre>
	 * removeAll("abcXYdefXYg","XY")
	 * result:abcdefg
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayOld char[]
	 * @return char[]
	 */
	public static final char[] removeAll(final char[] ArraySource, final char[] ArrayOld) {
		int lenSource, lenOld;
		if (((lenSource = ArraySource.length) < (lenOld = ArrayOld.length))) return ArraySource;
		int count;
		if ((count = searchCount(ArraySource, ArrayOld)) == Consts.Zero) return ArraySource;
		final char[] ResultArrayNew = new char[lenSource - lenOld * count];
		for (int i = count = 0; i < lenSource; i++) {
			if (PrivateArrayFindArray(ArraySource, ArrayOld, i)) {
				i += (lenOld - 1);
				continue;
			}
			ArrayElementSet(ResultArrayNew, count++, ArraySource[i]);
		}
		return ResultArrayNew;

	}

	/**
	 * 移走字符数组中的某个字符数组(全部)[嵌套移除]<br/>
	 * 
	 * <pre>
	 * removeAllNested("aXXYYce","XY")
	 * result:ace
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayOld char[]
	 * @return char[]
	 */
	public static final char[] removeAllNested(final char[] ArraySource, final char[] ArrayOld) {
		if (ArraySource.length < ArrayOld.length) return ArraySource;
		if (searchCount(ArraySource, ArrayOld) == Consts.Zero) return ArraySource;
		char[] ResultArrayNew = Consts.ArrayNull;
		while (searchCount(ArraySource, ArrayOld) > Consts.Zero)
			ResultArrayNew = removeAll(ArraySource, ArrayOld);
		return ResultArrayNew;
	}

	// TODO PrivateArrayFindArray 数组位置的数组比较
	/**
	 * 数组中指定位置，后面的与数组进行比较（混乱型）<br/>
	 * 原数组第point位置的字符存在在指定数组中，并指定数组都存在在原数组的后面<br/>
	 * 
	 * <pre>
	 * PrivateArrayFindArrayChaos("abcYXdef","XY",3)
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayFind char[]
	 * @param point int
	 * @return boolean
	 */
	private static final boolean PrivateArrayFindArrayChaos(final char[] ArraySource, final char[] ArrayFind, final int point) {
		int len, lenFind;
		if (point < 0 || (len = ArraySource.length) < point || ((len - point) < (lenFind = ArrayFind.length) || lenFind <= Consts.Zero) || !contains(ArrayFind, ArraySource[point])) return false;
		/* 用循环从后向前递推 */
		while (--lenFind >= 0)
			if (indexOf(ArraySource, ArrayFind[lenFind], point) == Consts.INDEX_NOT_FOUND) { return false; }
		return true;
	}

	/**
	 * 数组中指定位置，后面的与数组进行比较（混乱型）<br/>
	 * 找出第一个相同的子数组的下标<br/>
	 * 
	 * <pre>
	 * PrivateArrayFindArray("abcXYdef",{"aX","YZ","YX"},3)
	 * result:2
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayFind char[][]
	 * @param point int
	 * @return int
	 */
	private static final int PrivateArrayFindArrayChaos(final char[] ArraySource, final char[][] ArrayFind, final int point) {
		int len, lenFind;
		if ((len = ArraySource.length) == Consts.Zero || point < 0 || point > len || (lenFind = ArrayFind.length) == Consts.Zero) return Consts.INDEX_NOT_FOUND;
		for (int i = 0; i < lenFind; i++)
			if (PrivateArrayFindArrayChaos(ArraySource, ArrayFind[i], point)) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	/**
	 * 预留过程，用于数组某个位置后的几位，与某个子数组相同，但不移动主循环指针 前期先预留<br/>
	 * 如果使用此过程，则多余多个局部变量<br/>
	 * 
	 * <pre>
	 * PrivateArrayFindArray("abcXYdef","XY",3)
	 * result:true
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayFind char[]
	 * @param point int
	 * @return boolean
	 */
	private static final boolean PrivateArrayFindArray(final char[] ArraySource, final char[] ArrayFind, final int point) {
		int len, lenFind;
		if (point < 0 || (len = ArraySource.length) < point || ((len - point) < (lenFind = ArrayFind.length))) return false;
		/* 用循环从后向前递推 */
		while (lenFind-- > 0)
			if (!ElementEquals(ArraySource[point + lenFind], ArrayFind[lenFind])) { return false; }
		return true;
	}

	/**
	 * 预留过程，用于数组某个位置后的几位，与多个子数组相同，但不移动主循环指针 前期先预留<br/>
	 * 找出第一个相同的子数组的下标<br/>
	 * 
	 * <pre>
	 * PrivateArrayFindArray("abcXYdef",{"aX","YZ","XY"},3)
	 * result:2
	 * </pre>
	 * @param ArraySource char[]
	 * @param ArrayFind char[][]
	 * @param point int
	 * @return int
	 */
	private static final int PrivateArrayFindArray(final char[] ArraySource, final char[][] ArrayFind, final int point) {
		int len, lenFind;
		if ((len = ArraySource.length) == Consts.Zero || point < 0 || point > len || (lenFind = ArrayFind.length) == Consts.Zero) return Consts.INDEX_NOT_FOUND;
		for (int i = 0; i < lenFind; i++)
			if (PrivateArrayFindArray(ArraySource, ArrayFind[i], point)) return i;
		return Consts.INDEX_NOT_FOUND;
	}

	/* ================================================================================================================== =============== */
	// TODO Equals ElementEquals ElementComparison ArrayEquals 重写方法。为了以后修改成接口，暂不开发大小写的判断

	/**
	 * <font color='red'>泛型请在这里修改</font><br/>
	 * 两个字符元素的比较<br/>
	 * 如c > d 则返回 1<br/>
	 * 如c = d 则返回 0<br/>
	 * 如c < d 则返回 -1<br/>
	 * 
	 * <pre>
	 * ElementComparison('a','b')
	 * result:-1
	 * </pre>
	 * @param c char c
	 * @param d char c
	 * @return int
	 */
	public static final int ElementComparison(final char c, final char d) {
		if (c == d) return 0;
		if (c > d) return 1;
		return -1;
	}

	/**
	 * 预留 用于对象的比较，后期，可以重写此方法<br/>
	 * <font color='red'>泛型请在这里修改</font><br/>
	 * 判断两个字符元素是否相同<br/>
	 * 
	 * <pre>
	 * ElementEquals('a','a')
	 * result:true
	 * </pre>
	 * @param c char
	 * @param d char
	 * @return boolean
	 */
	public static final boolean ElementEquals(final char c, final char d) {
		if (c == d) return true;
		return false;
	}

	/**
	 * 预留 用于对象的比较，后期，可以重写此方法<br/>
	 * <font color='red'>泛型请在这里修改</font><br/>
	 * 
	 * <pre>
	 * ElementEquals('a','a','a')
	 * result:true
	 * </pre>
	 * @param Array char[]
	 * @return boolean
	 */
	public static final boolean ElementEquals(final char... Array) {
		int len;
		if ((len = Array.length) < 2) return false;
		while (--len > 0)
			if (Array[0] != Array[len]) return false;
		return true;
	}

	/**
	 * 用于多个数组的比较，后期，可以重写此方法<br/>
	 * 
	 * <pre>
	 * ArrayEquals("ab","ab","ab")
	 * result:true
	 * </pre>
	 * @param Array char[][]
	 * @return boolean
	 */
	public static final boolean ArrayEquals(final char[]... Array) {
		int ArrayLen;
		if ((ArrayLen = Array.length) < 2) return false;
		if (!ArrayEqualsLength(Array)) return false;/* 判断多个数组的长度是否一致 */
		int len;
		if ((len = Array[0].length) == Consts.Zero) return true;
		while (--ArrayLen > 0)
			for (int i = 0; i < len; i++)
				if (!ElementEquals(Array[0][i], Array[ArrayLen][i])) return false;
		return true;
	}

	/* 预留 用于对象的赋值，后期，可以重写此方法[暂不开发] */
	@SuppressWarnings("unused")
	private static char ElementSet(final char c) {
		return c;
	}

	/**
	 * 用于给某个数组对象某个位置进行赋值(通过参数地址，直接修改数组参数地址)<br/>
	 * 
	 * <pre>
	 * ArrayElementSet("abcdefg",2,'X')
	 * result:abXdefg
	 * </pre>
	 * @param ArraySource char[]
	 * @param Index int
	 * @param c char
	 */
	/* 用于给某个数组对象某个位置进行赋值 */
	public static final void ArrayElementSet(final char[] ArraySource, final int Index, final char c) {
		/* 直接赋值 */
		// ArraySource[Index] = c;
		/* UNSAFE赋值 */
		if (Index < 0 || Index >= ArraySource.length) return;/*UNSAFE不安全 前期判断？？？*/
		UNSAFE.putChar(ArraySource, (long) ((UNSAFE.arrayBaseOffset(char[].class)) + (Unsafe.ARRAY_CHAR_INDEX_SCALE * Index)), c);
	}

	/**
	 * 用于给某个二维数组对象某个位置进行赋值(通过参数地址，直接修改数组参数地址)<br/>
	 * 
	 * <pre>
	 * ArrayElementSet({"ab","cd","ef"},1,"XY")
	 * result:{"ab","XY","ef"}
	 * </pre>
	 * @param ArraySource char[][]
	 * @param Index int
	 * @param c char[]
	 */
	public static final void ArrayElementSet(final char[][] ArraySource, final int Index, final char[] c) {
		/* 直接赋值 */
		ArraySource[Index] = c;
		/*
		 *  UNSAFE赋值
		 *  对二维数组的赋值
		 *  */

	}

	/* ================================================================================================================== =============== */

	/**
	 * 判断多个数组的长度是否相同<br/>
	 * 
	 * <pre>
	 * ArrayEqualsLength("abc","def","ghi")
	 * result:true
	 * </pre>
	 * @param Array char[][]
	 * @return boolean
	 */
	public static final boolean ArrayEqualsLength(final char[]... Array) {
		int ArrayLen;
		if ((ArrayLen = Array.length) < 2) return false;
		while (--ArrayLen > 0)
			if (Array[0].length != Array[ArrayLen].length) return false;
		return true;
	}

	// TODO searchCount 搜索数组中 的数量
	/**
	 * 搜索数组中数组 数量<br/>
	 * 
	 * <pre>
	 * searchCount("aXYbcXYdefXYg","XY")
	 * result:3
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return int
	 */
	public static final int searchCount(final char[] ArraySource, final char[] Array) {
		int len, lenp;
		if ((len = ArraySource.length) < (lenp = Array.length) || len == Consts.Zero || lenp == Consts.Zero) return Consts.Zero;
		int count = Consts.Zero;
		for (int i = 0; i < (len - lenp + 1); i++)
			if (PrivateArrayFindArray(ArraySource, Array, i)) {
				count++;
				i += (lenp - 1);
			}
		return count;
	}

	/**
	 * 搜索数组中多个数组的数量,多个数组有优先级<br/>
	 * 
	 * <pre>
	 * searchCount("aXYbcXYdefXYg",{"XY","AB","CD"})
	 * result:3
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return int
	 */
	public static final int searchCount(final char[] ArraySource, final char[]... Array) {
		int len, lenp;
		if ((len = ArraySource.length) < (lenp = Array.length) || len == Consts.Zero || lenp == Consts.Zero) return Consts.Zero;
		int count = Consts.Zero;
		for (int i = 0, point = 0; i < (len - lenp + 1); i++)
			if ((point = PrivateArrayFindArray(ArraySource, Array, i)) != Consts.INDEX_NOT_FOUND) {
				count++;
				i += (Array[point].length - 1);
			}
		return count;
	}

	/**
	 * 搜索数组中单独字符 数量<br/>
	 * 
	 * <pre>
	 * searchCount("aXbcXdefXg",'X')
	 * result:3
	 * </pre>
	 * @param ArraySource char[]
	 * @param c char
	 * @return int
	 */
	public static final int searchCount(final char[] ArraySource, final char c) {
		int len;
		if ((len = ArraySource.length) == Consts.Zero) return Consts.Zero;
		int count = Consts.Zero;
		while (len-- > 0)
			if (ElementEquals(ArraySource[len], c)) count++;
		return count;
	}

	//TODO UtilSuffix 下标的基本操作
	/**
	 * 数组的下标的基本操作 数组的下标，现阶段只基于循环运算进行检索，<br/>
	 * 后期可以考虑用新下标数组的形式进行判断<br/>
	 * (<br/>
	 * 例：clearRepeat()方法，<br/>
	 * 后期新建同长数组，检索加入来清除多余下标<br/>
	 * 或先进行排序再 对排序后的下标数组进行检索(特殊情况除外，如按下标数组的顺序进行提取[不得排序])<br/>
	 * )<br/>
	 * @author Sunjian
	 * @category 静态类型
	 */
	static final class UtilSuffix {
		/**
		 * 数组下标的交集<br/>
		 * 
		 * <pre>
		 * mixed(12,"-1,5,0,-2,9,13,15")
		 * result:"5,0,9"
		 * </pre>
		 * @param len int
		 * @param Index int[]
		 * @return int[]
		 */
		static final int[] mixed(final int len, final int... Index) {
			int lenPoint;
			if ((lenPoint = Index.length) == Consts.Zero) return new int[Consts.Zero];
			int count = Consts.Zero;
			while (--lenPoint > Consts.INDEX_NOT_FOUND)
				if (Index[lenPoint] > Consts.INDEX_NOT_FOUND && Index[lenPoint] < len) count++;
			if (count == Consts.Zero) return Consts.ArrayIntNull;
			final int[] ResultArrayNew = new int[count];
			lenPoint = Index.length;
			while (--lenPoint > Consts.INDEX_NOT_FOUND)
				if (Index[lenPoint] > Consts.INDEX_NOT_FOUND && Index[lenPoint] < len) ResultArrayNew[--count] = Index[lenPoint];
			return ResultArrayNew;
		}

		/**
		 * 判断下标是否在下标组中<br/>
		 * 
		 * <pre>
		 * contains("1,5,0,2,9,13,15",5)
		 * result:true
		 * </pre>
		 * @param Array int[]
		 * @param Index int
		 * @return boolean
		 */
		static final boolean contains(final int[] Array, final int Index) {
			if (Array.length == Consts.Zero) return false;
			return indexOf(Array, Index) != Consts.INDEX_NOT_FOUND;
		}

		/**
		 * 查找字符在字符数组的位置 <br/>
		 * 
		 * <pre>
		 * indexOf("1,5,0,2,9,13,15,5",5)
		 * result:7
		 * </pre>
		 * @param Array int[]
		 * @param FindIndex int
		 * @return int
		 */
		static final int indexOf(final int[] Array, final int FindIndex) {
			if (Array.length == Consts.Zero) return Consts.INDEX_NOT_FOUND;
			return indexOf(Array, FindIndex, 0);

		}

		/**
		 * 查找字符在字符数组的位置 (从fromIndex开始查找)<br/>
		 * 
		 * <pre>
		 * indexOf("1,5,0,2,9,13,15,5",5,3)
		 * result:7
		 * </pre>
		 * @param Array int[]
		 * @param FindIndex int
		 * @param fromIndex int
		 * @return int
		 */
		static final int indexOf(final int[] Array, final int FindIndex, final int fromIndex) {
			int len;
			if ((len = Array.length) == Consts.Zero || fromIndex < Consts.INDEX_NOT_FOUND || fromIndex >= len) return Consts.INDEX_NOT_FOUND;
			int i = fromIndex;
			while (i < len)
				if (Array[i++] == FindIndex) return i - 1;
			return Consts.INDEX_NOT_FOUND;
		}

		/**
		 * 从逆序开始搜索,搜到就返回当前的index否则返回 Consts.INDEX_NOT_FOUND(-1)(以maxIndex定位向前)<br/>
		 * 
		 * <pre>
		 * indexOfLast("1,5,0,2,9,5,13,15",5,6)
		 * result:5
		 * </pre>
		 * @param Array int[]
		 * @param FindIndex int
		 * @param maxIndex int
		 * @return int
		 */
		static final int indexOfLast(final int[] Array, final int FindIndex, final int maxIndex) {
			int len;
			if ((len = Array.length) == Consts.Zero || maxIndex < 0 || maxIndex >= len) return Consts.INDEX_NOT_FOUND;
			int i = maxIndex;
			while (i-- > Consts.INDEX_NOT_FOUND)
				if (Array[i + 1] == FindIndex) return i + 1;
			return Consts.INDEX_NOT_FOUND;
		}

		/**
		 * 判断下标数组中是否含重复下标<br/>
		 * 
		 * <pre>
		 * isRepeat("1,5,0,2,9,5,13,15")
		 * result:true
		 * </pre>
		 * @param Array int[]
		 * @return boolean
		 */
		static final boolean isRepeat(final int[] Array) {
			for (int i = 0, ii = 0, len = Array.length; i < (len - 1); i++)
				for (ii = i + 1; ii < len; ii++)
					if (Array[i] == Array[ii]) return true;
			return false;
		}

		/**
		 * 发现下标数组中含有多少个重复下标<br/>
		 * 
		 * <pre>
		 * repeatCount("1,5,0,2,9,5,2,15,5")
		 * result:3
		 * </pre>
		 * @param Array int[]
		 * @return int
		 */
		static final int repeatCount(final int[] Array) {
			int count = Consts.Zero;
			loop: for (int i = 0, ii = 0, iii = 0, len = Array.length; i < len; i++) {
				for (iii = i - 1; iii >= 0; iii--)
					/* 判断前面数组是否含此字符，如有则已判断过，则跳到下一个字符进行比较 */
					if (Array[i] == Array[iii]) continue loop;
				for (ii = i + 1; ii < len; ii++)
					/* 判断后面数组是否含此字符，如有则计数加1 */
					if (Array[i] == Array[ii]) count++;
			}
			//			boolean find=false;
			//			for (int i = 0, ii = 0,iii=0, len = Array.length; i < len; i++,find = false){
			//				for (iii = i-1; iii >= 0 && find==false; iii--) {
			//					if (Array[i] == Array[iii]) {
			//						find=true;
			//					}
			//				}
			//				if(!find){
			//					for (ii = i + 1; ii < len; ii++){
			//						if (Array[i] == Array[ii]) {
			//							count++;
			//						}					
			//					}
			//				}
			//			}
			return count;
		}

		/**
		 * 清除重复的下标数组<br/>
		 * 
		 * <pre>
		 * clearRepeat("1,5,0,2,9,5,2,15,5")
		 * result:"1,5,0,2,9,15"
		 * </pre>
		 * @param Array int[]
		 * @return int[]
		 */
		static final int[] clearRepeat(final int[] Array) {
			int len;
			if ((len = Array.length) == Consts.Zero) return Array;
			if (!isRepeat(Array)) return Array;
			final int[] ArrayNew = new int[len - repeatCount(Array)];
			for (int i = 0, p = 0; i < len; i++)
				if (!contains(ArrayNew, Array[i])) ArrayNew[p++] = Array[i];
			return ArrayNew;
		}

		/**
		 * 判断group数组在values数组中的下标，生成下标组,过滤多余与无效<br/>
		 * 
		 * <pre>
		 * indexArray({1,2,3,4,5,6,7,8,9},{2,5,9,2,6,1,5,1,9,1})
		 * result:{1,4,8,5,0}
		 * </pre>
		 * @param values int[]
		 * @param group int[]
		 * @return int[]
		 */
		static final int[] indexArray(final int[] values, final int[] group) {
			int len;
			if (values == null || (len = values.length) == Consts.Zero) return Consts.ArrayIntNull;
			int vlen;
			if (group == null || (vlen = group.length) == Consts.Zero) return Consts.ArrayIntNull;
			int i, ii, iii, count = i = Consts.Zero;
			for (; i < vlen; i++)
				loop: for (ii = 0; ii < len; ii++)
					if (group[i] == values[ii]) {
						if (i > 0) for (iii = i - 1; iii >= 0; iii--)
							if (group[i] == group[iii]) break loop;
						count++;
						break;
					}
			if (count > Consts.Zero) {
				final int[] Array = new int[count];
				int p = i = 0;
				for (; i < vlen; i++)
					loop: for (ii = 0; ii < len; ii++)
						if (group[i] == values[ii]) {
							if (i > 0) for (iii = i - 1; iii >= 0; iii--)
								if (group[i] == group[iii]) break loop;
							Array[p++] = ii;
							break;
						}
				return Array;
			}
			return Consts.ArrayIntNull;
		}
	}

	//TODO UtilTool 工具
	static final class UtilTool {
		static Random rd = new Random();

		/**
		 * 得到min-max之间的随机数<br/>
		 * @param min
		 * @param max
		 * @return int
		 */
		static final int getRndInt(final int min, final int max) {
			return min + rd.nextInt(max - min + 1);
		}

		/**
		 * 判断任意一个整数是否素数<br/>
		 * @param n int
		 * @return boolean
		 */
		static final boolean isPrimes(final int n) {
			for (int i = 2; i <= Math.sqrt(n); i++)
				if (n % i == Consts.Zero) return false;
			return true;
		}

		/**
		 * 素数数量
		 * @param n int
		 * @return int
		 */
		static final int getPrimesCount(final int n) {
			int count = Consts.Zero;
			for (int i = 2; i <= Math.sqrt(n); i++)
				if (n % i == Consts.Zero) count++;
			return count;
		}

		/**
		 * 素数数组
		 * @param n int
		 * @return int[]
		 */
		static final int[] getPrimes(final int n) {
			if (!isPrimes(n)) return Consts.ArrayIntNull;
			final int[] intArray = new int[getPrimesCount(n)];/*保存含有重复数值的数组*/
			int p = Consts.Zero;
			for (int i = 2; i <= Math.sqrt(n); i++)
				if (n % i == Consts.Zero) intArray[p++] = i;
			/*final int[] outArray = UtilSuffix.clearRepeat(intArray);清除重复数值*/
			return intArray;
		}
	}

	/*
	 * 下标数组含有多个重复下标<br/>
	 * 
	 * <pre>
	 * repeatCount("1,5,0,2,9,5,2,15,5")
	 * result:3
	 * </pre>
	 * @param Array int[]
	 * @return int
	* /
	public static final int repeatCount(final int[] Array) {
		return UtilSuffix.repeatCount(Array);
	}*/

	/**
	 * 发现数组中含有多少个重复字符(不限)(不含本体)<br/>
	 * 
	 * <pre>
	 * repeatCount("aXcdYefXgXhYijY")
	 * result:4
	 * </pre>
	 * @param Array char[]
	 * @return int
	 */
	public static final int repeatCount(final char[] Array) {
		int count = Consts.Zero;
		loop: for (int i = 0, ii = 0, iii = 0, len = Array.length; i < len; i++) {
			for (iii = i - 1; iii >= 0; iii--)
				/* 判断前面数组是否含此字符，如有则已判断过，则跳到下一个字符进行比较 */
				if (ElementEquals(Array[i], Array[iii])) continue loop;
			for (ii = i + 1; ii < len; ii++)
				/* 判断后面数组是否含此字符，如有则计数加1 */
				if (ElementEquals(Array[i], Array[ii])) count++;
		}
		return count;
	}

	/**
	 * 发现数组中含有多少个重复字符(某个字符)<br/>
	 * 
	 * <pre>
	 * repeatCount("aXcdYefXgXhYijY",'X')
	 * result:3
	 * </pre>
	 * @param Array char[]
	 * @param c char
	 * @return int
	 */
	public static final int repeatCount(final char[] Array, final char c) {
		int count = Consts.Zero;
		for (int i = 0, len = Array.length; i < len; i++)
			/* 计数加1 */
			if (ElementEquals(Array[i], c)) count++;
		return count;
	}

	/**
	 * 发现数组中含有多少个重复字符(多个字符，限制重复范围)(不含本体)<br/>
	 * 
	 * <pre>
	 * repeatCountExcess("aXcdYefXgXhYijZYZ",{'X','Y'})
	 * result:4
	 * </pre>
	 * @param Array char[]
	 * @param c char[]
	 * @return int
	 */
	public static final int repeatCountExcess(final char[] Array, final char... c) {
		int count = Consts.Zero;
		for (int i = 0, sort = Consts.Zero, len = c.length; i < len; i++)
			if ((sort = repeatCount(Array, c[i])) > 1) count += (sort - 1);
		return count;
	}

	/**
	 * 发现数组中含有多少个重复字符（含本体）<br/>
	 * 
	 * <pre>
	 * repeatGroupCount("aXcdYefXgXhYijZYZ")
	 * result:8
	 * </pre>
	 * @param Array char[]
	 * @return int
	 */
	public static final int repeatGroupCount(final char... Array) {
		int count = Consts.Zero;
		final char[] newArray = clearRepeat(Array);
		for (int i = 0, sort = 0, len = newArray.length; i < len; i++)
			if ((sort = repeatCount(Array, newArray[i])) > 1) count += sort;
		return count;
	}

	/**
	 * 发现数组中含有多少组重复字符(只有本体数量)<br/>
	 * 
	 * <pre>
	 * repeatGroupSort("aXcdYefXgXhYijZYZ")
	 * result:3
	 * </pre>
	 * @param Array char[]
	 * @return int
	 */
	public static final int repeatGroupSort(final char... Array) {
		int count = Consts.Zero;
		final char[] newArray = clearRepeat(Array);
		for (int i = 0, len = newArray.length; i < len; i++)
			if (repeatCount(Array, newArray[i]) > 1) count++;
		return count;
	}

	/**
	 * 按数组顺序清除重复字符，组成新的数组(保留重复本体)<br/>
	 * 
	 * <pre>
	 * clearRepeat("abcd01c0ee21e3d")
	 * result:abcd01e23
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] clearRepeat(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) <= 1) return ArraySource;
		final char[] ArrayNew = new char[len - repeatCount(ArraySource)];
		loop: for (int i = 0, ii = 0, iii = 0; i < len; i++) {
			for (iii = i - 1; iii >= 0; iii--)
				/* 判断前面数组是否含此字符，如有则已判断过，则跳到下一个字符进行比较 */
				if (ElementEquals(ArraySource[i], ArraySource[iii])) continue loop;
			ArrayElementSet(ArrayNew, ii++, ArraySource[i]);
		}
		return ArrayNew;
	}

	/**
	 * 按数组顺序清除重复字符(指定范围内)，组成新的数组<br/>
	 * 在指定范围内只能出现一次字符，不允许重复<br/>
	 * 
	 * <pre>
	 * clearRepeat("aXcdYefXgXhYijZYZ","XZ")
	 * result:aXcdYefghYijZY
	 * </pre>
	 * @param ArraySource char[]
	 * @param Array char[]
	 * @return char[]
	 */
	public static final char[] clearRepeat(final char[] ArraySource, final char... Array) {
		int lenSource;
		if ((lenSource = ArraySource.length) <= 1 || Array.length == Consts.Zero) return ArraySource;
		int ii = repeatCountExcess(ArraySource, Array);
		final char[] ArrayNew = new char[lenSource - ii];
		loop: for (int i = ii = 0, iii = 0; i < lenSource; i++) {
			for (iii = i - 1; iii >= 0; iii--)
				/* 判断前面数组是否含此字符，如有则已判断过，则跳到下一个字符进行比较 */
				if (ElementEquals(ArraySource[i], ArraySource[iii]) && contains(Array, ArraySource[i])) continue loop;
			ArrayElementSet(ArrayNew, ii++, ArraySource[i]);
		}
		return ArrayNew;
	}

	/**
	 * 按数组顺序去掉有重复字符，组成新的数组<br/>
	 * 数组中的元素是唯一出现的<br/>
	 * 
	 * <pre>
	 * deleteRepeat("abcd01c0ee21e3d")
	 * result:ab23
	 * </pre>
	 * @param ArraySource char[]
	 * @return char[]
	 */
	public static final char[] deleteRepeat(final char[] ArraySource) {
		int len;
		if ((len = ArraySource.length) <= 1) return ArraySource;
		final char[] ArrayNew = new char[len - repeatGroupCount(ArraySource)];
		loop: for (int i = 0, ii = 0, iii = 0; i < len; i++) {
			for (iii = 0; iii < len; iii++)
				/* 判断数组是否含1个以上此字符，如有则已判断过，则跳到下一个字符进行比较 */
				if (iii != i && ElementEquals(ArraySource[i], ArraySource[iii])) continue loop;
			ArrayElementSet(ArrayNew, ii++, ArraySource[i]);
		}
		return ArrayNew;
	}

	/*
	 * sectionInter,sectionUnion,sectionDifference 数组交集、并集、差集
	 * 方法1:sectionInterDeprecated
	 * 数组的交集使用的是正常的逻辑判断(必须先期判断交集的总数(在判断时要先去除重复项，以唯一数组进行计算总数)，再进行检索)[逻辑上运行重]
	 * 方法2:
	 * 数组的并集使用的是数组的总集合去除重复项[数据对象数量与内存占有重]
	 * 
	 * 说明：如果重复项过多，则使用方法1
	 * 运行速度与内存占有，抉择中.....
	 */
	/**
	 * 多个数组的交集数量(删除重复单元)[用于决定交集数组的大小]<br/>
	 * 
	 * <pre>
	 * sectionInterCount("abcdef","dab","gha","1a23")
	 * result:1
	 * </pre>
	 * @param Array char[][]
	 * @return int
	 */
	public static final int sectionInterCount(final char[]... Array) {
		if (Array.length == Consts.Zero) return Consts.Zero;
		final char[] delMilti = clearRepeat(Array[0]);
		return sectionInterCount(delMilti, Array);
	}

	/**
	 * 多个数组的交集数量(删除重复单元)[用于决定交集数组的大小]<br/>
	 * clearRepeat(char[]):二维数组最短的数组的去掉重复的字符，用于以此标准进行检索。<br/>
	 * 
	 * <pre>
	 * sectionInterCount([abcdef],"abcdeff","dab","gha","1a23")
	 * result:1
	 * </pre>
	 * @param clearRepeat char[]
	 * @param Array char[][]
	 * @return int
	 */
	public static final int sectionInterCount(final char[] clearRepeat, final char[]... Array) {
		int len;
		if ((len = Array.length) == Consts.Zero) return Consts.Zero;
		int len0 = clearRepeat.length;
		if (len == 1) return len0;
		int count = Consts.Zero;
		loop: for (int i = 0, ii = 0; i < len0; i++) {
			for (ii = 0; ii < len; ii++)
				if (!contains(Array[ii], clearRepeat[i])) continue loop;
			count++;
		}
		return count;
	}

	//TODO sectionInter 数组交集
	/**
	 * 多个数组的交集(删除重复单元)<br/>
	 * <font color='red'>注意:此为正常的交集逻辑运算</font><br/>
	 * 
	 * <pre>
	 * sectionInter("abcdef","dab","gha","1a23")
	 * result:a
	 * </pre>
	 * @param Array char[][]
	 * @return char[]
	 */
	public static final char[] sectionInter(final char[]... Array) {
		int len;
		if ((len = Array.length) == Consts.Zero) return Consts.ArrayNull;
		final char[] standArray = clearRepeat(LenMinArray(Array));
		if (len == 1) return standArray;
		int count = sectionInterCount(Array);
		if (count == Consts.Zero) return Consts.ArrayNull;
		final char[] ArrayNew = new char[count];
		loop: for (int i = count = 0, ii = 0, len0 = standArray.length; i < len0; i++) {
			for (ii = 0; ii < len; ii++)
				if (!contains(Array[ii], standArray[i])) continue loop;
			ArrayElementSet(ArrayNew, count++, standArray[i]);
		}
		return ArrayNew;
	}

	/**
	 * 多个数组的交集(删除重复单元)<br/>
	 * <font color='red'>注意:此为UNSAFE方式进行存储，不用前期计算结果数组大小<br/>
	 * 后期修改成接口或泛型时，请不要使用此方法<br/>
	 * 因为MQSBuilder只支持char数组的存储<br/>
	 * 扩展尺寸:Unsafe.ARRAY_CHAR_INDEX_SCALE</font><br/>
	 * 
	 * <pre>
	 * sectionInter("abcdef","dab","gha","1a23")
	 * result:a
	 * </pre>
	 * @param Array char[][]
	 * @return char[]
	 */
	@Deprecated
	public static final char[] sectionInterDeprecated(final char[]... Array) {
		int len;
		if ((len = Array.length) == Consts.Zero) return Consts.ArrayNull;
		final char[] standArray = clearRepeat(LenMinArray(Array));
		if (len == 1) return standArray;
		MQSBuilder sb = new MQSBuilder();
		loop: for (int i = 0, ii = 0, len0 = standArray.length; i < len0; i++) {
			for (ii = 0; ii < len; ii++)
				if (!contains(Array[ii], standArray[i])) continue loop;
			sb.append(standArray[i]);
		}
		final char[] ArrayNew = sb.getArray();
		sb.close();
		return ArrayNew;
	}

	//TODO sectionUnion 数组并集
	/**
	 * 多个数组的并集(删除重复单元)<br/>
	 * 
	 * <pre>
	 * sectionUnion("ab12","a13","235","b12")
	 * result:ab1235
	 * </pre>
	 * @param Array char[][]
	 * @return char[]
	 */
	public static final char[] sectionUnion(final char[]... Array) {
		if (Array.length == Consts.Zero) return Consts.ArrayNull;
		return clearRepeat(concat(Array));
	}

	//TODO sectionDifference 数组差集
	/**
	 * 多个数组的差集(删除重复单元)<br/>
	 * 
	 * <pre>
	 * sectionDifference("ab192","a1c3","235","b1z2")
	 * result:9c5z
	 * </pre>
	 * @param Array char[][]
	 * @return char[]
	 */
	public static final char[] sectionDifference(final char[]... Array) {
		if (Array.length == Consts.Zero) return Consts.ArrayNull;
		return deleteRepeat(concat(Array));
	}
}
