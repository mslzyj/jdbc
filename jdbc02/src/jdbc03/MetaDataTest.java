package jdbc03;

import java.sql.ResultSet;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

public class MetaDataTest {

	/**
	 * ResultSetMethData:�����������Ԫ����
	 * ���Եõ�������еĻ�����Ϣ�������������Щ�У��������еı�����
	 * ��Ϸ���д��ͨ�õĲ�ѯ����
	 */
	@Test
	 public void testResultSetMetaData(){
		 Connection connection =null;
		 PreparedStatement preparedStatement  =null;
		 ResultSet resultSet=null;
		 try {
			connection = JDBCTools.getConnection();
			String sql = "SELECT id,name customerName,email,birth "+" FROM  `table`";
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			//�õ�ResultSetMetaData����
			ResultSetMetaData rsmd =  (ResultSetMetaData) resultSet.getMetaData();
			//�õ��еĸ���
			int columCount = rsmd.getColumnCount();
			System.out.println(columCount);
			for(int i=0;i<columCount;i++){
				
				//�õ�����
				String columnName = rsmd.getCatalogName(i+1);
				//�õ��еı���
				String columnLable = rsmd.getColumnLabel(i+1);
				System.out.println(columnName+" ,"+columnLable);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(preparedStatement, connection, resultSet);
		}
	 }
	/**
	 * DatabaseMetadata ���������ݿ��Ԫ�ص����ݶ��� ������connection�õ� �˽�
	 */
	@Test
	public void testMetaData() {
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = JDBCTools.getConnection();
			DatabaseMetaData data = (DatabaseMetaData) connection.getMetaData();
			// ���Եõ����ݿⱾ���һЩ��Ϣ
			// �õ����ݿ�İ汾��
			int version = data.getDatabaseMajorVersion();
			System.out.println(version);
			// �õ��������ݿ���û���
			String user = data.getUserName();
			System.out.println(user);
			// �õ�mysql������Щ���ݿ�
			resultSet = data.getCatalogs();
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(null, connection, resultSet);
		}
	}
	/**
	 * 5 mysql�汾
	 * root@localhost  �û���/ip
	 * information_schema  ���ݿ�
	 * mysql
	 * performance_schema 
	 * sakila 
	 * test
	 * test0 world
	 * 
	 */

}
