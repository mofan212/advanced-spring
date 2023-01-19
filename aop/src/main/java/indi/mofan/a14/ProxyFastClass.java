package indi.mofan.a14;

import org.springframework.cglib.core.Signature;

/**
 * @author mofan
 * @date 2023/1/19 17:09
 */
public class ProxyFastClass {
    static Signature s0 = new Signature("saveSuper", "()V");
    static Signature s1 = new Signature("saveSuper", "(I)V");
    static Signature s2 = new Signature("saveSuper", "(J)V");

    /**
     * <p>获取代理方法的编号</p>
     * <p>
     * Proxy 代理类中的方法：
     * saveSuper()             0
     * saveSuper(int)          1
     * saveSuper(long)         2
     * </p>
     *
     * @param signature 包含方法名称、参数返回值
     * @return 方法编号
     */
    public int getIndex(Signature signature) {
        if (s0.equals(signature)) {
            return 0;
        }
        if (s1.equals(signature)) {
            return 1;
        }
        if (s2.equals(signature)) {
            return 2;
        }
        return -1;
    }

    /**
     * 根据 getIndex() 方法返回的方法编号正常调用代理对象中带原始功能的方法
     *
     * @param index 方法编号
     * @param proxy 代理对象
     * @param args  调用方法需要的参数
     * @return 方法返回结果
     */
    public Object invoke(int index, Object proxy, Object[] args) {
        if (index == 0) {
            ((Proxy) proxy).saveSuper();
            return null;
        }
        if (index == 1) {
            ((Proxy) proxy).saveSuper((int) args[0]);
            return null;
        }
        if (index == 2) {
            ((Proxy) proxy).saveSuper((long) args[0]);
            return null;
        }
        throw new RuntimeException("无此方法");
    }

    public static void main(String[] args) {
        ProxyFastClass fastClass = new ProxyFastClass();
        int index = fastClass.getIndex(new Signature("saveSuper", "()V"));
        fastClass.invoke(index, new Proxy(), new Object[0]);
    }
}
