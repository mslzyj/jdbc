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
	 * 重构之后的JDBCToos，获取连接直接从数据库连接池中获取即可，即getConnection2()
	 * @throws Exception
	 */
	public void testJDBCToos() throws Exception{
		Connection connection = JDBCTools.getConnection2();
		System.out.println(connection);
	}
	/**
	 * 1.创建c3p0-config.xml文件
	 * 2.创建ComboPooledDataSource实例；
	 * 3.从ComboPooledDataSource实例中获取数据库连接
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
	 * 1.加载dbcp的properties配置文件：配置文件中的健需要来自BasicDataSource的属性
	 * 2.调用BasicDataSourceFactory的createDataSource方法创建createDataSource实例
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
	 * 使用DBCP数据连接池
	 * @throws SQLException 
	 * 1.创建数据库连接池
	 * 2.位数据源实例指定必须的属性
	 * 3.从数据源中获取数据库连接
	 * 
	 */
	@Test
	public void testDBCP() throws SQLException{
		//1.创建DBCP数据源实例
		BasicDataSource dataSource = null;
		//2.为数据源实例指定必须的属性
		dataSource = new BasicDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		//3.指定数据源的一些可选的属性
		//(1)指定数据库连接池中初始化连接数的个数
		dataSource.setInitialSize(10);
		//（2）指定最大连接数:可以同时向数据库申请的连接数
		dataSource.setMaxActive(50);
		//(3)指定最小连接数：在数据库连接池中保存的最少的连接数量
		dataSource.setMinIdle(5);
		//（4）等待数据库连接池都分配连接的最长时间，单位为毫秒，超出则抛出异常
		dataSource.setMaxWait(1000*5);
		
		//4.从数据源中获取数据库连接
		java.sql.Connection connection1 = dataSource.getConnection();
		System.out.println(connection1.getClass());
		
	}
	/**
	 * 数据批量处理
	 * 向数据库中的table数据表中插入10万条记录
	 * 测试如何插入，用时最短
	 * 相比下面两种方法，速度最快
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
				//”积攒“SQL
				preparedStatement.addBatch();
				//当积攒到一定程度，就执行一次，并且清空当前的sql
				if((i+1)%300==0){
					preparedStatement.executeUpdate();
					preparedStatement.clearBatch();
				}
			}
			//若总条数不是批量数值的整数倍，则还需要再执行一次
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
	 * 向数据库中的table数据表中插入10万条记录
	 * 测试如何插入，用时最短
	 * 1.使用PreparedStatement
	 * 只需要向数据库发送一条数据，所以速度比用statement快
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
	 * 向数据库中的table数据表中插入10万条记录
	 * 测试如何插入，用时最短
	 * 1.使用statement
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
