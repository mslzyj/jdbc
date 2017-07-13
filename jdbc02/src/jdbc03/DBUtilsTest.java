package jdbc03;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.mysql.jdbc.Connection;


public class DBUtilsTest {
	/**
	 * ScalarHandler:把结果集转为一个数值（可以是任何基本数据类型和字符串）返回
	 */
	@Test
	public void testScannerhandler(){
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT name " + "FROM `table` WHERE id=?";
		
			Object result = 
					 queryRunner.query(connection, sql,new ScalarHandler(),5);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}
	/**
	 * 重点掌握
	 * MapListHandler:将结果集转为一个map的List
	 * Map对应查询一条记录：键：SQL：查询的列名（不是列的别名）值，列的值
	 * 而MapListHandler：返回多条记录对应的map集合
	 */
	@Test
	public void testMapListhandler(){
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth " + "FROM `table`";
		
			List<Map<String,Object>> result =
					(List<Map<String, Object>>) queryRunner.query(connection, sql,new MapListHandler());
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}
	/**
	 * 重点掌握
	 * Maphandler:返回SQL对应的第一条记录对应的Map对象
	 * 键：SQL：查询的列名（不是列的别名）值，列的值
	 */
	@Test
	public void testMapHandler(){
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth " + "FROM `table`";
		
			Map<String,Object> result =
					(Map) queryRunner.query(connection, sql,new MapHandler());
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}
	/**
	 * 重点掌握
	 * 作用：把结果集转为一个List，该List不为null，但可能是空集合（Size（）方法返回0）
	 * 若sql御酒的确能够查询到记录，List中存放创建BeanListHandler传入的Class对象对应的对象
	 */
	@Test
	public void testBeanListHandler() {
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth " + "FROM `table`";
		    java.util.List<Customer> customers =
		    		(java.util.List<Customer>) queryRunner.query(connection, sql, new BeanListHandler(Customer.class));
			System.out.println(customers);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}

	/**
	 * 重点掌握
	 */
	@Test
	public void testbeanhandler() {
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth " + "FROM `table` WHERE id=?";
			Customer customer = (Customer) queryRunner.query(connection, sql, new BeanHandler(Customer.class), 5);
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}

	QueryRunner queryRunner = new QueryRunner();// 线程安全的,可放于方法体外

	class MyResultSethandler implements ResultSetHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			// System.out.println("handle...");
			// return "test";
			java.util.List<Customer> customers = new ArrayList<>();
			while (resultSet.next()) {
				Integer id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				Date birth = resultSet.getDate(4);
				Customer customer = new Customer(id, name, email, birth);
				customers.add(customer);

			}
			return customers;
		}
	}

	/**
	 * QueryRunner的query方法的返回值取决于ResultSethandler参数的handle方法的返回值
	 */
	@Test
	public void testQuery() {
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth " + " FROM `table` ";
			Object obj = queryRunner.query(connection, sql, new MyResultSethandler());
			System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}

	/**
	 * 测试QueryRunner类的update方法 该方法可用于增删改查
	 */
	@Test
	public void testUpdate() {
		// 1.创建QueryRunner的实现类
		QueryRunner queryRunner = new QueryRunner();
		// 2.使用期update方法
		String sql = "DELETE FROM `table` " + " WHERE id IN(?,?) ";
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			queryRunner.update(connection, sql, 4, 5);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}

}
