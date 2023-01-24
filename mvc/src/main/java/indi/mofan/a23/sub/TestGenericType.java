package indi.mofan.a23.sub;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author mofan
 * @date 2023/1/24 19:20
 */
public class TestGenericType {
    public static void main(String[] args) {
        // 1. java api
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        // 带有泛型信息的父类信息
        Type teacherDaoType = TeacherDao.class.getGenericSuperclass();
        System.out.println("TeacherDao type: " + teacherDaoType);
        System.out.println("TeacherDao type class: " + teacherDaoType.getClass());

        Type employeeDaoType = EmployeeDao.class.getGenericSuperclass();
        System.out.println("EmployeeDao type: " + employeeDaoType);
        System.out.println("EmployeeDao type class: " + employeeDaoType.getClass());

        // 有泛型参数的 Type 对象才是 ParameterizedType 类型
        if (teacherDaoType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) teacherDaoType;
            System.out.println(parameterizedType.getActualTypeArguments()[0]);
        }

        // 2. spring api 1
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        Class<?> t = GenericTypeResolver.resolveTypeArgument(TeacherDao.class, BaseDao.class);
        System.out.println(t);

        // 3. spring api 2
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(ResolvableType.forClass(StudentDao.class).getSuperType().getGeneric().resolve());
    }
}
