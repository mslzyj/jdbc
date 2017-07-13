package jdbc03;

import java.sql.ResultSet;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

public class MetaDataTest {

	/**
	 * ResultSetMethData:描述结果集的元数据
	 * 可以得到结果集中的基本信息：结果集中有哪些列，列名，列的别名等
	 * 结合反射写出通用的查询方法
	 */
	@Test
	 public void testResultSetMetaData(){
		 Connection connection =null;
		 PreparedStatement preparedStatement  =null;
		 ResultSet resultSet=null;
		 try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name customerName,email,birth "+" FROM  `table`";
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			//得到ResultSetMetaData对象
			ResultSetMetaData rsmd =  (ResultSetMetaData) resultSet.getMetaData();
			//得到列的个数
			int columCount = rsmd.getColumnCount();
			System.out.println(columCount);
			for(int i=0;i<columCount;i++){
				
				//得到列名
				String columnName = rsmd.getCatalogName(i+1);
				//得到列的别名
				String columnLable = rsmd.getColumnLabel(i+1);
				System.out.println(columnName+" ,"+columnLable);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(preparedStatement, connection, resultSet);
		}
	 }
	/**
	 * DatabaseMetadata 是描述数据库的元素的数据对象 可以有connection得到 了解
	 */
	@Test
	public void testMetaData() {
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			DatabaseMetaData data = (DatabaseMetaData) connection.getMetaData();
			// 可以得到数据库本身的一些信息
			// 得到数据库的版本号
			int version = data.getDatabaseMajorVersion();
			System.out.println(version);
			// 得到连接数据库的用户名
			String user = data.getUserName();
			System.out.println(user);
			// 得到mysql中有哪些数据库
			resultSet = data.getCatalogs();
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, resultSet);
		}
	}
	/**
	 * 5 mysql版本
	 * root@localhost  用户名/ip
	 * information_schema  数据库
	 * mysql
	 * performance_schema 
	 * sakila 
	 * test
	 * test0 world
	 * 
	 */

}
