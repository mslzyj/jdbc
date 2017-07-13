package jdbc03;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.mysql.jdbc.Blob;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class JDBCTest04 {

	/**
	 * ��ȡBlob���� 1.ʹ��getBlob����������ȡ��Blob����
	 * 2��ʹ��Blob��getBinaryStream���������õ�����������ʹ��IO����
	 */
	@Test
	public void readBlob() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name,email,birth,picture  " + " FROM  `table`  WHERE id=22";
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				System.out.println(id + "," + name + "," + email);
				Blob picture = (Blob) resultSet.getBlob(5);
				InputStream in = picture.getBinaryStream();
				System.out.println(in.available());

				FileOutputStream out = new FileOutputStream("a.jpg");
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				out.close();
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release((Statement) preparedStatement, connection, resultSet);
		}
	}

	/**
	 * ����BLOB���͵����ݱ���ʹ��PreparedStatement,��ΪBLOB���͵��������޷�ʹ���ַ���ƴд�ġ� ����setBlob��int
	 * index,InputStream inputstream��
	 */
	@Test
	public void testTnsertBlob() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "INSERT INTO `table`(name,email,birth,picture) " + " VALUES(?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, "ABCDE");
			preparedStatement.setString(2, "abcde@qq.com");
			preparedStatement.setDate(3, new java.sql.Date(new java.util.Date().getTime()));

			InputStream inputStream = new FileInputStream("56d0045032cba.jpg");
			preparedStatement.setBlob(4, inputStream);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release((Statement) preparedStatement, connection, null);
		}
	}

	/**
	 * ȡ�����ݿ��Զ����ɵ�����
	 */
	@Test
	public void testGetKeyValue() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "INSERT INTO `table`(name,email,birth) " + " VALUES(?,?,?)";
			// preparedStatement = connection.prepareStatement(sql);
			// ��������ֵ��ʹ�����ص�prepareStatement��SQL��flag������������PreparedStatement����
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, "ABCDE");
			preparedStatement.setString(2, "abcde@qq.com");
			preparedStatement.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
			preparedStatement.executeUpdate();
			// ͨ��getGeneratedKeys()��ȡ����������������ResultSet����
			// ��ResultSet��ֻ��һ��GENERATED_KEY�����ڴ�������ɵ�����ֵ
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				System.out.println(rs.getObject(1));
			}
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				System.out.println(rsmd.getColumnName(i + 1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release((Statement) preparedStatement, connection, null);
		}
	}

}
