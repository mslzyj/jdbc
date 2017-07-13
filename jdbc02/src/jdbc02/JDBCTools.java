package jdbc02;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class JDBCTools {
	/**
	 * ִ��SQl�ķ���
	 * insert��upadte��delete����������select
	 * @param sql
	 */
	public static void UpdatableResultSet(String sql){
		Connection connection=null;
		Statement statement =null;
		try {
			//��ȡ���ݿ�����
			connection=getConnection();
			//����ConnectionStatement������������ȡStatement����
			statement=(Statement) connection.createStatement();
			//����SQL��䣬����statement�����executeUpdate��sql������
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			release(statement, connection, null);
	   }
	}
	/**
	 * �ͷ����ݿ���Դ�ķ���
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
	 * ��ȡ���ݿ�����
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		// 1.׼���������ݿ��4���ַ���
		Properties properties = new Properties();
		// 2.����properties����
		InputStream in = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
		// 3.��ȡjdbc.properties��Ӧ��������
		properties.load(in);
		// 4.���ض����������
		// 5.�������user��password��4���ַ���
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String driver = properties.getProperty("driver");
		// 6.�������ݿ��������򣨶�Ӧ��driverʵ��������ע��ľ�̬����飩
		Class.forName(driver);
		// 7.ͨ��driverMansger��getConnection����������ȡ���ݿ�����
		return (Connection) DriverManager.getConnection(jdbcUrl, user, password);
	}
}
