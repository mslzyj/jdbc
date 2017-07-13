package jdbc03;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class TranscationTest {
	/**
	 * 测试事务的隔离级别 在JDBC程序中可以通过COnnection的setTransactionIsolation来设置隔事务的离级别
	 */
	@Test
	public void testTranscationIsolationUpdate() {
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			connection.setAutoCommit(false);
			String sql = "UPDATE `user` SET blance= " + "blance-500 WHERE id=1";
			update(connection, sql);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// JDBCTools.release(statement, connection, rs);
		}
	}

	@Test
	public void testTranscationIsolationRead() {
      String sql = "SELECT blance FROM `user` WHERE  id = 1";
      Integer blance = getForValue(sql);
      System.out.println(blance);
	}

	public <E> E getForValue(String sql, Object... args) {
		// 1.得到结果集：该结果集应该只有一行，且只有一列
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.得到结果集
			connection = JDBCTools.getConnection();
			// 读取未提交的数据
			//connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			//读取已经提交的数据
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
			JDBCTools.release((Statement) preparedStatement, connection, resultSet);
		}
		return null;
	}

	/**
	 * 数据库事务 Tom给张三汇款500元 关于事务： 1.如果多个操作每个操作使用的是自己的单独的连接，则无法保证事务
	 * 2.如果提交都成功，提交事务，如果出现异常，则返回到修改之前数据 具体步骤： 1.事务开始之前，开始事务，取消Connection的默认提交行为
	 * connection。setAutoCommit（false）； 2.如果事务的操作都成功，则提交事connection.commit();
	 * 3.回滚事务：若出现异常，则在catch块中回滚事务。
	 */
	@Test
	public void testTranscationTest() {
		/*
		 * 如果多个操作每个操作使用的是自己的单独的连接，则无法保证事务 DAO dao = new DAO(); String sql =
		 * "UPDATE `user` SET blance= "+"blance-500 WHERE id=1";
		 * dao.update(sql); sql =
		 * "UPDATE `user` SET blance= "+"blance+500 WHERE id=2";
		 * dao.update(sql);
		 */
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			System.out.println(connection.getAutoCommit());
			// 开始事务：取消默认提交
			connection.setAutoCommit(false);
			String sql = "UPDATE `user` SET blance= " + "blance-500 WHERE id=1";
			update(connection, sql);
			int i = 10 / 0;// 异常
			System.out.println(i);
			sql = "UPDATE `user` SET blance= " + "blance+500 WHERE id=2";
			update(connection, sql);
			// 提交事务
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// 出现异常的时候回滚事务
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			JDBCTools.release(null, connection, null);
		}
	}

	public void update(Connection connection, String sql, Object... args) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release((Statement) preparedStatement, null, null);
		}
	}

}
