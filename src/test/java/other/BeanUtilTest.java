package other;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class BeanUtilTest {
    @Test
    public void simpleTest() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //其实就是按名字调用GetSet方法，
        Employee employee = new Employee("0");

        Employee employee1 = new Employee("1");
        System.out.println(( PropertyUtils.getIndexedProperty(employee, "lists", 0)));
        PropertyUtils.setIndexedProperty(employee, "lists", 0, employee1);
        System.out.println(( PropertyUtils.getIndexedProperty(employee, "lists", 0)));

        Address address = new Address("wuhan");
        System.out.println(PropertyUtils.getMappedProperty(employee,"addresses","home"));
        PropertyUtils.setMappedProperty(employee, "addresses", "home", address);
        System.out.println(PropertyUtils.getMappedProperty(employee, "addresses", "home"));

        System.out.println(PropertyUtils.getProperty(employee,"sex"));

        String city = (String) PropertyUtils.getProperty(employee,
                "lists[0].addresses(home).city");
        System.out.println(city);
    }
}
