package MaQiao.MaQiaoArray;
public final class MQJNIArrayChar {
	static {
		System.loadLibrary("MQJNIArrayChar");// 装入动态链接库，"HelloWorldImpl"是要装入的动态链接库名称。
	}
	/**
	 * <font color='red'>耦合</font> 查看一维数组中，有多少个数量>=2的字符 此方法与 deleteRepeat[只保留唯一字符]方法相反<br/>
	 * <pre>
	 * coupling({"abxyxzyzxcy"})
	 * result:{"xyz"}
	 * </pre>
	 * @param array char[]
	 * @return char[]
	 */
	public static final native char[] coupling(final char... array);
	
}
