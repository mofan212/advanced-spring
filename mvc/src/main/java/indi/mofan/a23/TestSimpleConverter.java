package indi.mofan.a23;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

/**
 * @author mofan
 * @date 2023/1/24 16:46
 */
public class TestSimpleConverter {
    public static void main(String[] args) {
        SimpleTypeConverter converter = new SimpleTypeConverter();
        Integer number = converter.convertIfNecessary("13", int.class);
        System.out.println(number);
        Date date = converter.convertIfNecessary("1999/03/04", Date.class);
        System.out.println(date);
    }
}
