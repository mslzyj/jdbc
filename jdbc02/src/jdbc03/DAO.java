package jdbc03;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

public class DAO {
	// INSERT,UPDATE,DELETE���������԰�������
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

	// ��ѯһ����¼�����ض�Ӧ�Ķ���
	public <T> T Get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.��ȡConnection
			connection = JDBCTools.getConnection();
			// 2.��ȡPreparedStatement����
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			// 3.���ռλ��
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			// 4.���в�ѯ���õ�ResultSet
			resultSet = preparedStatement.executeQuery();
			// 5.���ResultSet���м�¼��׼��һ��Map<String,OBject>��������еı��� ֵ������е�ֵ
			if (resultSet.next()) {
				Map<String, Object> values = new HashMap<String, Object>();
				// 6.�õ�ResultSetMetaData����
				ResultSetMetaData r = (ResultSetMetaData) resultSet.getMetaData();
				// 7.����ResultSet��ָ�������ƶ�һ����λ

				// 8.��ResultSetMetadata����õ���������ж�����
				int co = r.getColumnCount();
				// 9.��ResultSetMetaData�õ�ÿһ�еı�������ResultSet�õ�ÿһ�е�ֵ
				for (int i = 0; i < co; i++) {
					String colable = r.getColumnLabel(i + 1);
					Object covalue = resultSet.getObject(co);
					// 10.���Map����
					values.put(colable, covalue);
				}

				// 11.�÷��䷵��Class��Ӧ�Ķ���
				entity = clazz.newInstance();
				// 12.�÷��������������ֵ������map��key��ֵΪMap�е�value
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
	 * ��ѯ������¼�����ض�Ӧ�Ķ���ļ���
	 *  public <T> List getforList(Class<T> clazz, String sql,
	 * Object... args) { List<T> list = new ArrayList(); Connection connection =
	 * null; PreparedStatement preparedStatement = null; ResultSet resultSet =
	 * null; try { connection = JDBCTools.getConnection(); preparedStatement =
	 * (PreparedStatement) connection.prepareCall(sql); for (int i = 0; i <
	 * args.length; i++) { preparedStatement.setObject(i = 1, args[i]); }
	 * resultSet = preparedStatement.executeQuery(); // 5.׼��һlist
	 * map<String,Object>����һ��map�����Ӧһ����¼ java.util.List<Map<String, Object>>
	 * values = new ArrayList<>(); ResultSetMetaData r = (ResultSetMetaData)
	 * resultSet.getMetaData(); Map<String, Object> map = null; //
	 * 7.����ResultSet,ʹ��whileѭ�� while (resultSet.next()) { map = new HashMap<>();
	 * for (int i = 0; i < r.getColumnCount(); i++) { String colable =
	 * r.getColumnLabel(i = 1); Object value = resultSet.getObject(i + 1);
	 * map.put(colable, value); } } // 11.�����õ�map�������5׼����list�� values.add(map);
	 * // 12.�ж�List�Ƿ�Ϊ�գ�����Ϊ�գ������List�õ�һ��map�����ٰ�һ��map����ת��Ϊһ��class��������Ӧobject����
	 * Object bean = null; if (values.size() > 0) { for (Map<String, Object> m :
	 * values) { bean = clazz.newInstance(); for (Map.Entry<String, Object>
	 * entry : m.entrySet()) { String propertyName = entry.getKey(); Object
	 * value = entry.getValue(); BeanUtils.setProperty(bean, propertyName,
	 * value); } // 13.��Object�������list�� list.add((T) bean); } }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } finally {
	 * JDBCTools.release(preparedStatement, connection, resultSet); }
	 * 
	 * return (List) list; }
	 */
	// ����ĳ����¼��ĳһ���ֶε�ֵ��һ��ͳ�Ƶ�ֵ��һ����������¼��
	public <E> E getForValue(String sql, Object... args) {
		// 1.�õ���������ý����Ӧ��ֻ��һ�У���ֻ��һ��
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.�õ������
			connection = JDBCTools.getConnection();
			// ��ȡδ�ύ������
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
