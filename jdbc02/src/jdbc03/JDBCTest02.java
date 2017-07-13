package jdbc03;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

/**
 * ��ȡ���ݱ��е�ĳһ�е����ַ��� Title:JDBCTest02
 * 
 * @author zyj
 * @date2017��6��23������2:28:11
 */
public class JDBCTest02 {

	@Test
	public void testGet() {
		String sql = "SELECT id,name,email,birth " + " FROM  `table`  WHERE id=?";
		Customer customer = get(Customer.class, sql, 5);
		System.out.println(customer);
		
		
		sql ="SELECT flow_id flowID,type,exam_card examCard,id_card idCard,student_name studentName,location,grade  FROM `examstudent` WHERE flow_id=?";
		Student stu = get(Student.class, sql, 4);
		System.out.println(stu);
	}

	// ͨ�÷���
	public <T> T get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.�õ�resultSet����
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			// 2.�õ�ResultSetMetaData����
			ResultSetMetaData r = (ResultSetMetaData) resultSet.getMetaData();
			// 3.����һ��Map<String,OBject>���󣬼���SQL��ѯ���ı�����ֵ:�е�ֵ
			Map<String, Object> values = new HashMap<>();
			// 4.��������������ResultSetMetaData���3��Ӧ��Map����
			if (resultSet.next()) {
				for (int i = 0; i < r.getColumnCount(); i++) {
					String co = r.getColumnLabel(i + 1);
					Object coVlues = resultSet.getObject(i + 1);
					values.put(co, coVlues);
				}
			}
			// 5.��Map��Ϊ�գ����÷��䴴��clszz��Ӧ�Ķ���
			if (values.size() > 0) {
				entity = clazz.newInstance();
				// 6.����Map�������÷���δclazz����Ķ�Ӧ�����Ը�ֵ
				for (Map.Entry<String, Object> entry : values.entrySet()) {
					String fieldName = entry.getKey();
					Object value = entry.getValue();
				    //ReflectionUtils.setFieldValue(entity, fieldName, value);
				    BeanUtils.setProperty(entity, fieldName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(preparedStatement, connection, resultSet);
		}
		return entity;
	}

	@Test
	public void testGet1() {
		String sql = "SELECT id,name,email,birth " + " FROM  `table`  WHERE id=?";
		Customer customer = getCustomer(sql, 6);
		System.out.println(customer);
	}

	public Customer getCustomer(String sql, Object... args) {
		Customer customer = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				customer = new Customer();
				customer.setId(resultSet.getInt(1));
				customer.setName(resultSet.getString(2));
				customer.setEmail(resultSet.getString(3));
				customer.setBirth(resultSet.getDate(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(preparedStatement, connection, resultSet);
		}
		return customer;
	}
}
