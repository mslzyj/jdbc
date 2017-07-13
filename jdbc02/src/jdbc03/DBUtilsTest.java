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
	 * ScalarHandler:�ѽ����תΪһ����ֵ���������κλ����������ͺ��ַ���������
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
	 * �ص�����
	 * MapListHandler:�������תΪһ��map��List
	 * Map��Ӧ��ѯһ����¼������SQL����ѯ�������������еı�����ֵ���е�ֵ
	 * ��MapListHandler�����ض�����¼��Ӧ��map����
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
	 * �ص�����
	 * Maphandler:����SQL��Ӧ�ĵ�һ����¼��Ӧ��Map����
	 * ����SQL����ѯ�������������еı�����ֵ���е�ֵ
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
	 * �ص�����
	 * ���ã��ѽ����תΪһ��List����List��Ϊnull���������ǿռ��ϣ�Size������������0��
	 * ��sql���Ƶ�ȷ�ܹ���ѯ����¼��List�д�Ŵ���BeanListHandler�����Class�����Ӧ�Ķ���
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
	 * �ص�����
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

	QueryRunner queryRunner = new QueryRunner();// �̰߳�ȫ��,�ɷ��ڷ�������

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
	 * QueryRunner��query�����ķ���ֵȡ����ResultSethandler������handle�����ķ���ֵ
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
	 * ����QueryRunner���update���� �÷�����������ɾ�Ĳ�
	 */
	@Test
	public void testUpdate() {
		// 1.����QueryRunner��ʵ����
		QueryRunner queryRunner = new QueryRunner();
		// 2.ʹ����update����
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
