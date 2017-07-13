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
 * 获取数据表中的某一行的两种方法 Title:JDBCTest02
 * 
 * @author zyj
 * @date2017年6月23日下午2:28:11
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

	// 通用方法
	public <T> T get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.得到resultSet对象
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			// 2.得到ResultSetMetaData对象
			ResultSetMetaData r = (ResultSetMetaData) resultSet.getMetaData();
			// 3.创建一个Map<String,OBject>对象，键：SQL查询到的别名，值:列的值
			Map<String, Object> values = new HashMap<>();
			// 4.处理结果集，利用ResultSetMetaData填充3对应的Map对象
			if (resultSet.next()) {
				for (int i = 0; i < r.getColumnCount(); i++) {
					String co = r.getColumnLabel(i + 1);
					Object coVlues = resultSet.getObject(i + 1);
					values.put(co, coVlues);
				}
			}
			// 5.若Map不为空，利用反射创建clszz对应的对象
			if (values.size() > 0) {
				entity = clazz.newInstance();
				// 6.遍历Map对象，利用反射未clazz对象的对应的属性赋值
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
