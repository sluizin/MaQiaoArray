package MaQiao.MaQiaoArray;

import java.util.Random;
import static MaQiao.MaQiaoArray.Consts.NumberType;

/**
 * @author Sunjian
 * @category UtilTool工具一些数字的判断等
 */
//TODO UtilTool 工具
public final class UtilTool {
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

	//TODO UtilTool is 判断 素数,奇数
	/**
	 * 判断任意一个整数是否素数,奇数，偶数<br/>
	 * @param NTYPE NumberType
	 * @param n int
	 * @return boolean
	 */
	static final boolean isNumberType(final NumberType NTYPE, final int n) {
		switch (NTYPE) {
		case Odd://奇数判断方法
			return (n & 1) == 0 ? false : true;
		case Even://偶数判断方法
			return (n & 1) == 0 ? true : false;
		default://默认素数判断方法
			if (n <= 0) return false;
			for (int i = 2; i <= Math.sqrt(n); i++)
				if (n % i == Consts.Zero) return false;
		}
		return true;
	}

	//TODO UtilTool getNumCounts 得到某种数的数量 素数,奇数，偶数
	/**
	 * 素数,奇数，偶数 等 数量(1-n之间的数量)
	 * @param n int
	 * @return int
	 */
	static final int getNumberCounts(final NumberType NTYPE, final int n) {
		int count = Consts.Zero;
		for (int i = 1; i <= n; i++)
			if (isNumberType(NTYPE, i)) count++;
		return count;
	}

	/**
	 * 判断一个数字，可组成多少个X*Y形式
	 * @param n int
	 * @return int
	 */
	static final int getCompriseRanksCount(final int n) {
		int count = Consts.Zero;
		for (int i = 2; i <= Math.sqrt(n); i++)
			if (n % i == Consts.Zero) count++;
		return count;
	}

	/**
	 * 对下标数组进行集合操作(相加，如num为负，则所有下标减去num)
	 * @param num int
	 * @param array int[]
	 * @return int[]
	 */
	static final int[] Math_Add(final int num, final int... array) {
		int len;
		if ((len = array.length) == Consts.Zero) return array;
		for (int i = 0; i < len; i++)
			array[i] += num;
		return array;
	}

}
