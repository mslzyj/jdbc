package jdbc01;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
/*
 * ͨ��jdbc�����ݿ���в���һ����� 1.Ststement:����ִ��SQL���Ķ���
 * 2.ͨ��executeUpdate��sql����ִ��sql��� 3.ͨ��Connection������createStatement������������ȡ
 * 4.����SQL������Insert��update��delete����������select
 * 5.Connection,Statement����Ӧ�ó�������ݿ��������������Դ��ʹ�ú�Ҫ�ر�
 * 6.Ϊ�˱�֤�ڳ����쳣����������������ܹ������رգ���Ҫ��finally�йر�������������
 * 7.�رյ�˳���ȹر�statement�ٹر�Connect
 */
public class JDBCTest02 {
	@Test
	public void testStatement() throws Exception {
		String sql =null;
		//�������
		//sql="INSERT INTO `table`(id,name,email,birth)" + "VALUES ('8','g','123@qq.com','2000-10-30')";
		//ɾ������ 
		sql="DELETE FROM `table` WHERE id=8";
		//�޸�����
		// sql="UPDATE `table` SET name='lisi2'"+"Where id=6";
		 UpdatableResultSet(sql);
		
	}
	/*
	 * ͨ�ø������ݵķ���������INSERT,UPDATE,DELETE
	 * */
	public void UpdatableResultSet(String sql){
		Connection connection=null;
		Statement statement =null;
		try {
			connection=JDBCTest02Toos.getConnection();
			statement=(Statement) connection.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTest02Toos.release(statement, connection, null);
	   }
	}
	
	@Test
	public void testGetConnection() throws Exception {
		System.out.println(JDBCTest02Toos.getConnection());
	}
}






