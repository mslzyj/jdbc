package jdbc03;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.mysql.jdbc.Connection;

/**
 * ʹ��QueryRunner�ṩ�����ʵ�� Title:JDBCDAOImpl
 * 
 * @author zyj
 * @date2017��7��11������10:54:12
 * @param<T>:�����贫��ķ�������
 */

public class JDBCDAOImpl<T> implements DAOFinaly<T> {
	private QueryRunner queryRunner = null;
    private Class<T> type;		
	public JDBCDAOImpl() {
		queryRunner = new QueryRunner();
		//type=ReflectionUtils.getSuperGenericType(getClass()); ��Ҫ���һ��ReflectionUtils��
	}

	@Override
	public void batch(Connection connection, String sql, Object[]... args) {
		// TODO Auto-generated method stub

	}

	@Override
	public <E> E getForValue(Connection connection, String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getForList(Connection connection, String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T get(Connection connection, String sql, Object... args) throws SQLException {
		
		return queryRunner.query(connection, sql,
				new BeanHandler<T>(type),args);
	}

	@Override
	public void update(Connection connection, String sql, Object... args) {
		// TODO Auto-generated method stub

	}

}
