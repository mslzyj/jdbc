package jdbc01;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
/*
 * 通过driver接口获得数据库连接
 * */
public class JDBCTest {
	/*
	 * Driver 是一个接口，数据库厂商必须提供实现的接口，能从其中获取数据库连接
	 * 通过driver类的实现类对象获取数据库连接
	 * */
//	@Test
//	public void testDriver() throws SQLException {
//     //创建一个driver实现类对象
//	 Driver driver=new com.mysql.jdbc.Driver();
//		//准备连接数据库的基本信息
//     String url="jdbc:mysql://localhost:3306/test";
//     Properties info = new Properties();
//     info.put("user", "root");
//     info.put("password", "123456");
//     //调用driver接口说去数据库连接
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
	 *通用方法，在不修改程序的情况下，可以获取任何数据库的连接 
	 *配置文件中，通过参数哦配置文件的方法实现和具体的数据库解耦。
	 * */
	public Connection getConnection()throws Exception{
		String driver = null;
		String jdbcUrl = null;
		String user=null;
		String password=null;
		//读取类路径下的jdbc.properties文件
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);//从输入流中读取属性列表（键和元素对）。
		driver=properties.getProperty("driver");//用指定的键在此属性列表中搜索属性
		jdbcUrl=properties.getProperty("jdbcUrl");
		user=properties.getProperty("user");
		password = properties.getProperty("password");
		//通过反射创建driver对象
		Driver driver1 = (Driver) Class.forName(driver).newInstance();
		Properties info = new Properties();//创建一个无默认值的空属性列表。
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
