package indi.mofan.a14;

import org.springframework.cglib.core.Signature;

/**
 * @author mofan
 * @date 2023/1/19 17:11
 */
public class TargetFastClass {

    static Signature s0 = new Signature("save", "()V");
    static Signature s1 = new Signature("save", "(I)V");
    static Signature s2 = new Signature("save", "(J)V");

    /**
     * <p>获取目标方法的编号</p>
     * <p>
     * Target 目标类中的方法：
     * save()             0
     * save(int)          1
     * save(long)         2
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
     * 根据 getIndex() 方法返回的方法编号正常调用目标对象方法
     *
     * @param index       方法编号
     * @param target       目标对象
     * @param args 调用目标对象方法需要的参数
     * @return 方法返回结果
     */
    public Object invoke(int index, Object target, Object[] args) {
        if (index == 0) {
            ((Target) target).save();
            return null;
        }
        if (index == 1) {
            ((Target) target).save((int) args[0]);
            return null;
        }
        if (index == 2) {
            ((Target) target).save((long) args[0]);
            return null;
        }
        throw new RuntimeException("无此方法");
    }

    public static void main(String[] args) {
        TargetFastClass fastClass = new TargetFastClass();
        int index = fastClass.getIndex(new Signature("save", "()V"));
        fastClass.invoke(index, new Target(), new Object[0]);

        index = fastClass.getIndex(new Signature("save", "(J)V"));
        fastClass.invoke(index, new Target(), new Object[]{2L});
    }
}
