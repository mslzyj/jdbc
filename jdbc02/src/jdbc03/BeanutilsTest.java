package jdbc03;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

public class BeanutilsTest {

	@Test
	public void testSetProperty() throws IllegalAccessException, InvocationTargetException {
		Object object = new Student();
		System.out.println(object);
		//ͨ��set������ֵ��Ϊ��������Ը�ֵ
		BeanUtils.setProperty(object, "idCard", "122134458987");
		System.out.println(object);
	}
	@Test
	public void testGetProperty() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Object object = new Student();
		System.out.println(object);
		//ͨ��set������ֵ
		BeanUtils.setProperty(object, "idCard", "122134458987");
		System.out.println(object);
		//ͨ��get������ȡֵ   ��ȡ�������Ե�ֵ
		Object val = BeanUtils.getProperty(object, "idCard");
		System.out.println(val);
	}
}
