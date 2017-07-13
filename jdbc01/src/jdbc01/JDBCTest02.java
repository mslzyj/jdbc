package jdbc01;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
/*
 * 通过jdbc向数据库表中插入一条语句 1.Ststement:用于执行SQL语句的对象
 * 2.通过executeUpdate（sql）来执行sql语句 3.通过Connection（）的createStatement（）方法来获取
 * 4.出入SQL可以是Insert，update或delete，但不能是select
 * 5.Connection,Statement都是应用程序的数据库服务器的连接资源，使用后要关闭
 * 6.为了保证在出现异常的情况下两个对象能够正常关闭，需要在finally中关闭上述两个对象
 * 7.关闭的顺序：先关闭statement再关闭Connect
 */
public class JDBCTest02 {
	@Test
	public void testStatement() throws Exception {
		String sql =null;
		//添加数据
		//sql="INSERT INTO `table`(id,name,email,birth)" + "VALUES ('8','g','123@qq.com','2000-10-30')";
		//删除数据 
		sql="DELETE FROM `table` WHERE id=8";
		//修改数据
		// sql="UPDATE `table` SET name='lisi2'"+"Where id=6";
		 UpdatableResultSet(sql);
		
	}
	/*
	 * 通用更新数据的方法：包括INSERT,UPDATE,DELETE
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






