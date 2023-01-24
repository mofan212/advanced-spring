package indi.mofan.a22;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author mofan
 * @date 2023/1/24 15:03
 */
public class A22 {
    public static void main(String[] args) throws Exception {
        // 反射获取参数名
        Method foo = Bean2.class.getMethod("foo", String.class, int.class);
//        for (Parameter parameter : foo.getParameters()) {
//            System.out.println(parameter.getName());
//        }

        // 基于 LocalVariableTable 本地变量表获取
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(foo);
        System.out.println(Arrays.toString(parameterNames));
    }
}
