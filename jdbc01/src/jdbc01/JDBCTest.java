package jdbc01;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
/*
 * ͨ��driver�ӿڻ�����ݿ�����
 * */
public class JDBCTest {
	/*
	 * Driver ��һ���ӿڣ����ݿ⳧�̱����ṩʵ�ֵĽӿڣ��ܴ����л�ȡ���ݿ�����
	 * ͨ��driver���ʵ��������ȡ���ݿ�����
	 * */
//	@Test
//	public void testDriver() throws SQLException {
//     //����һ��driverʵ�������
//	 Driver driver=new com.mysql.jdbc.Driver();
//		//׼���������ݿ�Ļ�����Ϣ
//     String url="jdbc:mysql://localhost:3306/test";
//     Properties info = new Properties();
//     info.put("user", "root");
//     info.put("password", "123456");
//     //����driver�ӿ�˵ȥ���ݿ�����
//     Connection connection = (Connection) driver.connect(url, info);
//     System.out.println(connection);
//	}
	
	
//  private static final String URL="jdbc:mysql://127.0.0.1:3306/test";
//	private static final String NAME="root";
//	private static final String PASSWORD="123456";
//	@Test
//	public void test() throws ClassNotFoundException, SQLException {
//	Class.forName("com.mysql.jdbc.Driver");
//	java.sql.Connection conn=DriverManager.getConnection(URL, NAME, PASSWORD);
//	java.sql.Statement stmt=conn.createStatement();
//	java.sql.ResultSet rs=stmt.executeQuery("select workername from ");
//	while(rs.next()){
//		System.out.println(rs.getString("workername"));
//	}
//	}
	
	/*
	 *ͨ�÷������ڲ��޸ĳ��������£����Ի�ȡ�κ����ݿ������ 
	 *�����ļ��У�ͨ������Ŷ�����ļ��ķ���ʵ�ֺ;�������ݿ���
	 * */
	public Connection getConnection()throws Exception{
		String driver = null;
		String jdbcUrl = null;
		String user=null;
		String password=null;
		//��ȡ��·���µ�jdbc.properties�ļ�
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);//���������ж�ȡ�����б�����Ԫ�ضԣ���
		driver=properties.getProperty("driver");//��ָ���ļ��ڴ������б�����������
		jdbcUrl=properties.getProperty("jdbcUrl");
		user=properties.getProperty("user");
		password = properties.getProperty("password");
		//ͨ�����䴴��driver����
		Driver driver1 = (Driver) Class.forName(driver).newInstance();
		Properties info = new Properties();//����һ����Ĭ��ֵ�Ŀ������б�
		info.put("user", user);
		info.put("password", password);
		Connection connection = (Connection) driver1.connect(jdbcUrl, info);
		return connection;
	}
	@Test
	public void testGetConnection() throws Exception{
		System.out.println(getConnection());
	}
	
}
