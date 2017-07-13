package jdbc03;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.mysql.jdbc.Connection;

/**
 * 使用QueryRunner提供其具体实现 Title:JDBCDAOImpl
 * 
 * @author zyj
 * @date2017年7月11日上午10:54:12
 * @param<T>:子类需传入的泛型类型
 */

public class JDBCDAOImpl<T> implements DAOFinaly<T> {
	private QueryRunner queryRunner = null;
    private Class<T> type;		
	public JDBCDAOImpl() {
		queryRunner = new QueryRunner();
		//type=ReflectionUtils.getSuperGenericType(getClass()); 需要添加一个ReflectionUtils类
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
