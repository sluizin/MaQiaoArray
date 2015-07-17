package MaQiao.MaQiaoArray;

public final class Consts {
	static final int Zero = 0;/* 数量初始值 */
	//private static final int INDEX_Zero = 0;/* 下标初始值 */
	static final int INDEX_NOT_FOUND = -1;/* 返回值或变量 */

	static final char ElementFillSpace = ' ';/* 填充物：稻草、塑料、橡胶、硅胶 */
	static final char ElementFill = '\0';/* 填充物：稻草、塑料、橡胶、硅胶 */
	static final char ElementIntFill = 0;/* 填充物：稻草、塑料、橡胶、硅胶 */

	static final char[] ArrayNull = new char[0]; /* 空数组，用于返回空值 */
	static final char[][] Array2Null = new char[0][0]; /* 空数组，用于返回空值 */
	static final byte[][] ArrayByte2Null = new byte[0][0]; /* 空数组，用于返回空值 */
	//private static final Character ElementNull = null; /* 空单元，用于临时变量转移使用 */
	static final int[] ArrayIntNull = new int[0]; /* 空数组，用于返回空值 */
	

	/**
	 * 取模,加,减,乘,除,的状态:<br/>
	 * @author Sunjian
	 */
	public static enum MathOperation {
		/**
		 * 取模
		 */
		Mod(0),
		/**
		 * 加
		 */
		Add(1),
		/**
		 * 减
		 */
		Subtract(2),
		/**
		 * 乘
		 */
		Multiply(3),
		/**
		 * 除
		 */
		Divide(4);
		/**
		 * 取模,加,减,乘,除<br/>
		 */
		int index;

		/**
		 * 构造初始化
		 * @param index int
		 */
		private MathOperation(final int index) {
			this.index = index;
		}
		/**
		 * 得到枚举中的int index值
		 * @return int
		 */
		public final int getIndex(){
			return index;
		}
		public static final MathOperation getType(final int index) {
			for (MathOperation c : MathOperation.values())
				if (c.index == index) return c;
			return null;
		}
	}
	/**
	 * 素数，奇数，偶数,的状态:<br/>
	 * @author Sunjian
	 */
	public static enum NumberType {
		/**
		 * 素数
		 */
		Prime(0),
		/**
		 * 奇数
		 */
		Odd(1),
		/**
		 * 偶数
		 */
		Even(2);
		/**
		 * 素数，奇数，偶数<br/>
		 */
		int index;

		/**
		 * 构造初始化
		 * @param index int
		 */
		private NumberType(final int index) {
			this.index = index;
		}
		/**
		 * 得到枚举中的int index值
		 * @return int
		 */
		public final int getIndex(){
			return index;
		}
		public static final NumberType getType(final int index) {
			for (NumberType c : NumberType.values())
				if (c.index == index) return c;
			return null;
		}
	}		
}
