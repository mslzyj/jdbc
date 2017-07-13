package jdbc03;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class JDBCTest05 {
	/**
	 * �ع�֮���JDBCToos����ȡ����ֱ�Ӵ����ݿ����ӳ��л�ȡ���ɣ���getConnection2()
	 * @throws Exception
	 */
	public void testJDBCToos() throws Exception{
		Connection connection = JDBCTools.getConnection2();
		System.out.println(connection);
	}
	/**
	 * 1.����c3p0-config.xml�ļ�
	 * 2.����ComboPooledDataSourceʵ����
	 * 3.��ComboPooledDataSourceʵ���л�ȡ���ݿ�����
	 * @throws SQLException
	 */
	@Test
	public void testC3p0ConfigFile() throws SQLException{
	  ComboPooledDataSource dataSource = new ComboPooledDataSource("helloc3p0");
	  System.out.println(dataSource.getConnection());
	  ComboPooledDataSource comboPooledDataSource = dataSource;
	  System.out.println(comboPooledDataSource.getMaxStatements());
	}
	
	/**
	 * @throws PropertyVetoException 
	 * @throws SQLException 
	 * 
	 */
	@Test
	public void  testC3pp0() throws PropertyVetoException, SQLException{
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver");
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
		cpds.setUser("root");
		cpds.setPassword("123456");
		System.out.println(cpds.getConnection());
	}
	
	/**
	 * 1.����dbcp��properties�����ļ��������ļ��еĽ���Ҫ����BasicDataSource������
	 * 2.����BasicDataSourceFactory��createDataSource��������createDataSourceʵ��
	 * @throws Exception
	 */
	@Test
	public void testDBCPWithSourceFactory() throws Exception{
	   Properties properties = new Properties();
	   InputStream inputStream = JDBCTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
	   properties.load(inputStream);
		javax.sql.DataSource dataSource =
				BasicDataSourceFactory.createDataSource(properties);
		System.out.println(dataSource.getConnection());
		BasicDataSource basicDataSource = (BasicDataSource) dataSource;
		System.out.println(basicDataSource.getMaxWait());
	}
	
	/**
	 * ʹ��DBCP�������ӳ�
	 * @throws SQLException 
	 * 1.�������ݿ����ӳ�
	 * 2.λ����Դʵ��ָ�����������
	 * 3.������Դ�л�ȡ���ݿ�����
	 * 
	 */
	@Test
	public void testDBCP() throws SQLException{
		//1.����DBCP����Դʵ��
		BasicDataSource dataSource = null;
		//2.Ϊ����Դʵ��ָ�����������
		dataSource = new BasicDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		//3.ָ������Դ��һЩ��ѡ������
		//(1)ָ�����ݿ����ӳ��г�ʼ���������ĸ���
		dataSource.setInitialSize(10);
		//��2��ָ�����������:����ͬʱ�����ݿ������������
		dataSource.setMaxActive(50);
		//(3)ָ����С�������������ݿ����ӳ��б�������ٵ���������
		dataSource.setMinIdle(5);
		//��4���ȴ����ݿ����ӳض��������ӵ��ʱ�䣬��λΪ���룬�������׳��쳣
		dataSource.setMaxWait(1000*5);
		
		//4.������Դ�л�ȡ���ݿ�����
		java.sql.Connection connection1 = dataSource.getConnection();
		System.out.println(connection1.getClass());
		
	}
	/**
	 * ������������
	 * �����ݿ��е�table���ݱ��в���10������¼
	 * ������β��룬��ʱ���
	 * ����������ַ������ٶ����
	 */
	@Test
	public void testBatch(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		try {
			connection = JDBCTools.getConnection();
			JDBCTools.begintx(connection);
			sql = "INSERT INTO `customers0` VALUES(?,?,?)";
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			Date date = new Date(new java.util.Date().getTime());
			long begin = System.currentTimeMillis();
			for(int i=0;i<100000;i++){
				preparedStatement .setInt(1, i+1);
				preparedStatement.setString(2, "name_"+i);
				preparedStatement.setDate(3, date);
				//�����ܡ�SQL
				preparedStatement.addBatch();
				//�����ܵ�һ���̶ȣ���ִ��һ�Σ�������յ�ǰ��sql
				if((i+1)%300==0){
					preparedStatement.executeUpdate();
					preparedStatement.clearBatch();
				}
			}
			//������������������ֵ��������������Ҫ��ִ��һ��
			if(100000%300!=0){
				preparedStatement.executeUpdate();
				preparedStatement.clearBatch();
			}
			long end = System.currentTimeMillis();
			System.out.println("Time0= "+(end-begin));//Time0= 1278
             JDBCTools.commit(connection);
		} catch (Exception e) {
			JDBCTools.rollback(connection);
           e.printStackTrace();
		}finally{
			JDBCTools.release(preparedStatement, connection, null);
		}
	}
	
	/**
	 * �����ݿ��е�table���ݱ��в���10������¼
	 * ������β��룬��ʱ���
	 * 1.ʹ��PreparedStatement
	 * ֻ��Ҫ�����ݿⷢ��һ�����ݣ������ٶȱ���statement��
	 */
	@Test
	public void testBatchWithPreparedStatement() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		try {
			connection = JDBCTools.getConnection();
			JDBCTools.begintx(connection);
			sql = "INSERT INTO `customers1` VALUES(?,?,?)";
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			Date date = new Date(new java.util.Date().getTime());
			long begin = System.currentTimeMillis();
			for(int i=0;i<100000;i++){
				preparedStatement .setInt(1, i+1);
				preparedStatement.setString(2, "name_"+i);
				preparedStatement.setDate(3, date);
				preparedStatement.executeUpdate();
			}
			long end = System.currentTimeMillis();
			System.out.println("Time1= "+(end-begin));//Time1= 17747
             JDBCTools.commit(connection);
		} catch (Exception e) {
			JDBCTools.rollback(connection);
           e.printStackTrace();
		}finally{
			JDBCTools.release(preparedStatement, connection, null);
		}
	}
	/**
	 * �����ݿ��е�table���ݱ��в���10������¼
	 * ������β��룬��ʱ���
	 * 1.ʹ��statement
	 */
	@Test
	public void testBatchWithStatement() {
		Connection connection = null;
	    Statement statement = null;
		String sql = null;
		try {
			connection = JDBCTools.getConnection();
			JDBCTools.begintx(connection);
			statement = (Statement) connection.createStatement();
			long begin = System.currentTimeMillis();
			for(int i=0;i<100000;i++){
				sql = "INSERT INTO `customers2` VALUES("+(i+1)+",'name_"+i+"','10-7-17')";
			   statement.executeUpdate(sql);
			}
			long end = System.currentTimeMillis();
			System.out.println("Time2= "+(end-begin));//Time2= 15448
			JDBCTools.commit(connection);
		} catch (Exception e) {
           e.printStackTrace();
           JDBCTools.rollback(connection);
		}finally{
			JDBCTools.release(statement, connection, null);
		}
	}

}
