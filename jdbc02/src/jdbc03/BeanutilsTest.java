package jdbc03;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

public class BeanutilsTest {

	@Test
	public void testSetProperty() throws IllegalAccessException, InvocationTargetException {
		Object object = new Student();
		System.out.println(object);
		//通过set方法赋值，为对象的属性赋值
		BeanUtils.setProperty(object, "idCard", "122134458987");
		System.out.println(object);
	}
	@Test
	public void testGetProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Object object = new Student();
		System.out.println(object);
		//通过set方法赋值
		BeanUtils.setProperty(object, "idCard", "122134458987");
		System.out.println(object);
		//通过get方法获取值   获取对象属性的值
		Object val = BeanUtils.getProperty(object, "idCard");
		System.out.println(val);
	}
}
