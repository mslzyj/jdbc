package jdbc03;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class TranscationTest {
	/**
	 * ��������ĸ��뼶�� ��JDBC�����п���ͨ��COnnection��setTransactionIsolation�����ø�������뼶��
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
		// 1.�õ���������ý����Ӧ��ֻ��һ�У���ֻ��һ��
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1.�õ������
			connection = JDBCTools.getConnection();
			// ��ȡδ�ύ������
			//connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			//��ȡ�Ѿ��ύ������
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
	 * ���ݿ����� Tom���������500Ԫ �������� 1.����������ÿ������ʹ�õ����Լ��ĵ��������ӣ����޷���֤����
	 * 2.����ύ���ɹ����ύ������������쳣���򷵻ص��޸�֮ǰ���� ���岽�裺 1.����ʼ֮ǰ����ʼ����ȡ��Connection��Ĭ���ύ��Ϊ
	 * connection��setAutoCommit��false���� 2.�������Ĳ������ɹ������ύ��connection.commit();
	 * 3.�ع������������쳣������catch���лع�����
	 */
	@Test
	public void testTranscationTest() {
		/*
		 * ����������ÿ������ʹ�õ����Լ��ĵ��������ӣ����޷���֤���� DAO dao = new DAO(); String sql =
		 * "UPDATE `user` SET blance= "+"blance-500 WHERE id=1";
		 * dao.update(sql); sql =
		 * "UPDATE `user` SET blance= "+"blance+500 WHERE id=2";
		 * dao.update(sql);
		 */
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			System.out.println(connection.getAutoCommit());
			// ��ʼ����ȡ��Ĭ���ύ
			connection.setAutoCommit(false);
			String sql = "UPDATE `user` SET blance= " + "blance-500 WHERE id=1";
			update(connection, sql);
			int i = 10 / 0;// �쳣
			System.out.println(i);
			sql = "UPDATE `user` SET blance= " + "blance+500 WHERE id=2";
			update(connection, sql);
			// �ύ����
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// �����쳣��ʱ��ع�����
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
