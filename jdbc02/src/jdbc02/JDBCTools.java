package jdbc02;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class JDBCTools {
	/**
	 * 执行SQl的方法
	 * insert，upadte或delete，而不包含select
	 * @param sql
	 */
	public static void UpdatableResultSet(String sql){
		Connection connection=null;
		Statement statement =null;
		try {
			//获取数据库连接
			connection=getConnection();
			//调用ConnectionStatement（）方法，获取Statement对象
			statement=(Statement) connection.createStatement();
			//发送SQL语句，调用statement对象的executeUpdate（sql）方法
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			release(statement, connection, null);
	   }
	}
	/**
	 * 释放数据库资源的方法
	 * @param statement
	 * @param connection
	 * @param rs
	 */
	public static void release(Statement statement,Connection connection,ResultSet rs){
		if(statement!=null){
			try {
				statement.close();
			} catch (Exception e2) {
				
			}
		}
	
		if(connection!=null){
			try {
				connection.close();
			} catch (Exception e2) {
				
			}
		}
		if(rs!=null){
			try {
				rs.close();
			} catch (Exception e2) {
				
			}
		}
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		// 1.准备连接数据库的4个字符串
		Properties properties = new Properties();
		// 2.创建properties对象
		InputStream in = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
		// 3.获取jdbc.properties对应的输入流
		properties.load(in);
		// 4.加载对象的输入流
		// 5.具体决定user，password等4个字符串
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String driver = properties.getProperty("driver");
		// 6.加载数据库驱动程序（对应的driver实现类中有注册的静态代码块）
		Class.forName(driver);
		// 7.通过driverMansger的getConnection（）方法获取数据库连接
		return (Connection) DriverManager.getConnection(jdbcUrl, user, password);
	}
}
