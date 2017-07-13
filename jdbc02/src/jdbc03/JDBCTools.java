package jdbc03;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class JDBCTools {
	// �������ݿ�����
		public static void commit(Connection connection) {
			if (connection != null) {
				try {
					connection.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	   //�ع�����
		public static void rollback(Connection connection) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	  //��ʼ����
		public static void begintx(Connection connection) {
			if (connection != null) {
				try {
					connection.setAutoCommit(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * ���ݿ����ӳ�ֻӦ����ʼ��һ�Σ�getConnection2()������getConnection()�ĸĽ�
		 */
		private static ComboPooledDataSource dataSource=null;
		static{
			dataSource=new ComboPooledDataSource("helloc3p0");
		}
		public static Connection getConnection2()throws Exception{
			return (Connection) dataSource.getConnection();
		}

	/**
	 * ִ��sql���
	 * ʹ��preparedStatement
	 * @param sql
	 * @param args ��дsqlռλ���Ŀɱ����
	 */
	public static void UpdatableResultSet(String sql,Object ... args){//�ɱ����
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		try {
			connection=JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++){
				preparedStatement.setObject(i+1, args[i]);
			}
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(preparedStatement, connection, null);
		}
	}
	
	
	
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
			try {//ʹ�����ݿ����ӳؾ������ݿ����ӣ�
				//���ݿ����ӳص�Connection�������closeʱ��
				//�����ǽ��йرգ����ǰѸ����ݿ����ӹ黹�����ݿ����ӳ���
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
