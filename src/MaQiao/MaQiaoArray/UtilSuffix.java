package MaQiao.MaQiaoArray;

/**
 * 数组的下标的基本操作 数组的下标，现阶段只基于循环运算进行检索，<br/>
 * 后期可以考虑用新下标数组的形式进行判断<br/>
 * (<br/>
 * 例：clearRepeat()方法，<br/>
 * 后期新建同长数组，检索加入来清除多余下标<br/>
 * 或先进行排序再 对排序后的下标数组进行检索(特殊情况除外，如按下标数组的顺序进行提取[不得排序])<br/>
 * )<br/>
 * @author Sunjian
 * @category 静态类型 下标操作
 */
//TODO UtilSuffix 下标的基本操作
public final class UtilSuffix {
	/**
	 * 判断下标数组是否在范围之内<br/>
	 * Cross:true 跨域调取(循环调取)<br/>
	 * 
	 * <pre>
	 * isSuffixExsit(12,false,{1,5,10,2,12})
	 * result:false
	 * </pre>
	 * @param len int
	 * @param Cross boolean
	 * @param Index int[]
	 * @return boolean
	 */
	static final boolean isSuffixExsit(final int len, final boolean Cross, final int... Index) {
		if (len == 0 || Index.length == 0) return false;
		if (Cross) return true;
		for (int i = 0, lenIndex = Index.length; i < lenIndex; i++)
			if (Index[i] < 0 || Index[i] >= len) return false;
		return true;
	}

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
