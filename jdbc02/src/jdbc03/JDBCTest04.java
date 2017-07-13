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
	 * 读取Blob数据 1.使用getBlob（）方法读取到Blob对象
	 * 2，使用Blob的getBinaryStream（）方法得到输入流，在使用IO操作
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
	 * 插入BLOB类型的数据必须使用PreparedStatement,因为BLOB类型的数据是无法使用字符串拼写的。 调用setBlob（int
	 * index,InputStream inputstream）
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
	 * 取得数据库自动生成的主键
	 */
	@Test
	public void testGetKeyValue() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "INSERT INTO `table`(name,email,birth) " + " VALUES(?,?,?)";
			// preparedStatement = connection.prepareStatement(sql);
			// 返回主键值，使用重载的prepareStatement（SQL，flag）方法来生成PreparedStatement对象
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, "ABCDE");
			preparedStatement.setString(2, "abcde@qq.com");
			preparedStatement.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
			preparedStatement.executeUpdate();
			// 通过getGeneratedKeys()获取包含新生的主键的ResultSet对象
			// 在ResultSet中只有一列GENERATED_KEY，用于存放新生成的主健值
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
