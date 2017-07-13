package jdbc01;

import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Connection;

public class JDBCTest01 {
    /*
     * DriverManager�������Ĺ�����
     * ��Ҫ�ȴ������ݿ�
     * ����ͨ�����ص�getConnection����������ȡ�������ӣ���Ϊ����
     * ����ͬʱ����������������ע���˶�����ݿ����ӣ������getConnection()
     * ����ʱ���ز�ͬ�����ݿ�����
     * */
	@Test
	public void testDriverManager() throws Exception{
		String driverClass = null;//������ȫ����
		String jdbcUrl = null;
		String user=null;
		String password=null;
		//��ȡ��·���µ�jdbc.properties�ļ�
		InputStream in = 
				getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);//���������ж�ȡ�����б�����Ԫ�ضԣ���
		driverClass=properties.getProperty("driver");//��ָ���ļ��ڴ������б�����������
		jdbcUrl=properties.getProperty("jdbcUrl");
		user=properties.getProperty("user");
		password = properties.getProperty("password");
		//�������ݿ���������
		Class.forName(driverClass);
        Connection connection=
		      (Connection) DriverManager.getConnection(jdbcUrl, user, password);
	    System.out.println(connection);
	}
	

}
