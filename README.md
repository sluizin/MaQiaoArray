Constants 通用常量<br/>
MaQiaoStringBuilder jvm外内存存储char，只支持(++=)，不提供(+-=)<br/>
/**
 * <font color="red"><h1>看吧！数组！想吐不？(嘿嘿)总之我是吐了，以后再也不写数组操作了。</h1></font><br/>
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