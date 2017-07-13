package jdbc01;

import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Connection;

public class JDBCTest01 {
    /*
     * DriverManager是驱动的管理类
     * 需要先创建数据库
     * 可以通过重载的getConnection（）方法获取数据连接，较为方便
     * 可以同时管理多个驱动程序；若注册了多个数据库连接，则调用getConnection()
     * 方法时返回不同的数据库连接
     * */
	@Test
	public void testDriverManager() throws Exception{
		String driverClass = null;//驱动的全类名
		String jdbcUrl = null;
		String user=null;
		String password=null;
		//读取类路径下的jdbc.properties文件
		InputStream in = 
				getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);//从输入流中读取属性列表（键和元素对）。
		driverClass=properties.getProperty("driver");//用指定的键在此属性列表中搜索属性
		jdbcUrl=properties.getProperty("jdbcUrl");
		user=properties.getProperty("user");
		password = properties.getProperty("password");
		//加载数据库驱动程序
		Class.forName(driverClass);
        Connection connection=
		      (Connection) DriverManager.getConnection(jdbcUrl, user, password);
	    System.out.println(connection);
	}
	

}
