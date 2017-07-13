package jdbc03;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

public class DAO {
	// INSERT,UPDATE,DELETE操作都可以包含其中
	public void update(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(preparedStatement, connection, null);
		}
	}

	// 查询一条记录，返回对应的对象
	public <T> T Get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.获取Connection
			connection = JDBCTools.getConnection();
			// 2.获取PreparedStatement对象
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			// 3.填充占位符
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			// 4.进行查询，得到ResultSet
			resultSet = preparedStatement.executeQuery();
			// 5.如果ResultSet中有记录，准备一个Map<String,OBject>键：存放列的别名 值：存放列的值
			if (resultSet.next()) {
				Map<String, Object> values = new HashMap<String, Object>();
				// 6.得到ResultSetMetaData对象
				ResultSetMetaData r = (ResultSetMetaData) resultSet.getMetaData();
				// 7.处理ResultSet，指针向下移动一个单位

				// 8.由ResultSetMetadata对象得到结果集中有多少列
				int co = r.getColumnCount();
				// 9.有ResultSetMetaData得到每一列的别名，有ResultSet得到每一列的值
				for (int i = 0; i < co; i++) {
					String colable = r.getColumnLabel(i + 1);
					Object covalue = resultSet.getObject(co);
					// 10.填充Map对象
					values.put(colable, covalue);
				}

				// 11.用反射返回Class对应的对象
				entity = clazz.newInstance();
				// 12.用反射填充对象的属性值，名：map中key，值为Map中的value
				for (Map.Entry<String, Object> entry : values.entrySet()) {
					String propertyName = entry.getKey();
					Object value = entry.getValue();
					// ReflectionUtils.setFieldValue((Field) entity,
					// propertyName, value);
					BeanUtils.setProperty(entity, propertyName, value);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			JDBCTools.release(preparedStatement, connection, resultSet);
		}

		return entity;
	}

	/*
	 * 查询多条记录，返回对应的对象的集合
	 *  public <T> List getforList(Class<T> clazz, String sql,
	 * Object... args) { List<T> list = new ArrayList(); Connection connection =
	 * null; PreparedStatement preparedStatement = null; ResultSet resultSet =
	 * null; try { connection = JDBCTools.getConnection(); preparedStatement =
	 * (PreparedStatement) connection.prepareCall(sql); for (int i = 0; i <
	 * args.length; i++) { preparedStatement.setObject(i = 1, args[i]); }
	 * resultSet = preparedStatement.executeQuery(); // 5.准备一list
	 * map<String,Object>其中一个map对象对应一条记录 java.util.List<Map<String, Object>>
	 * values = new ArrayList<>(); ResultSetMetaData r = (ResultSetMetaData)
	 * resultSet.getMetaData(); Map<String, Object> map = null; //
	 * 7.处理ResultSet,使用while循环 while (resultSet.next()) { map = new HashMap<>();
	 * for (int i = 0; i < r.getColumnCount(); i++) { String colable =
	 * r.getColumnLabel(i = 1); Object value = resultSet.getObject(i + 1);
	 * map.put(colable, value); } } // 11.把填充好的map对象放入5准备的list中 values.add(map);
	 * // 12.判断List是否为空，若不为空，则遍历List得到一个map对象，再把一个map对象转移为一个class，参数对应object对象
	 * Object bean = null; if (values.size() > 0) { for (Map<String, Object> m :
	 * values) { bean = clazz.newInstance(); for (Map.Entry<String, Object>
	 * entry : m.entrySet()) { String propertyName = entry.getKey(); Object
	 * value = entry.getValue(); BeanUtils.setProperty(bean, propertyName,
	 * value); } // 13.把Object对象放入list中 list.add((T) bean); } }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } finally {
	 * JDBCTools.release(preparedStatement, connection, resultSet); }
	 * 
	 * return (List) list; }
	 */
	// 返回某条记录的某一个字段的值或一个统计的值（一共多少条记录）
	public <E> E getForValue(String sql, Object... args) {
		// 1.得到结果集：该结果集应该只有一行，且只有一列
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.得到结果集
			connection = JDBCTools.getConnection();
			// 读取未提交的数据
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return (E) resultSet.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(preparedStatement, connection, resultSet);
		}
		return null;
	}
}
