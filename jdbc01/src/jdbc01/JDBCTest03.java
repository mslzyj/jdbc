package jdbc01;

import java.sql.Date;
import java.sql.ResultSet;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class JDBCTest03 {
	/**
	 * ResultSet：结果集，封装了使用JDBC进行查询的结果
	 * 1.调用Statement对象的executeQuery（sql）可以得到结果集
	 * 2.ResultSet返回的是一张数据表，有一个指针指向数据表的第一行的前面
	 * 可以调用next（）方法检测下一行是否有效，若有效该方法返回true，且指针下移，相当于Iterator对象的hasnext（）和next（）方法的结合体
	 * 3.当指针对位到一行时，可以通过掉一批农getxxx()方法获取每一列的值。ResultSet需要进行关闭
	 * */
	@Test
	public void testResultSet(){
		//获取id=4的customers数据表的记录并打印
		Connection connection=null;
		Statement statement=null;
		ResultSet rs=null;
		try {
			//获取Connection
			connection=JDBCTest02Toos.getConnection();
			//获取Statement
			statement=(Statement) connection.createStatement();
			String  sql="select id,name,email,birth "+" from `table`  ";
			//执行查询，得到ResultSet
			rs=statement.executeQuery(sql);
			//处理ResultSet
			while(rs.next()){
				int id=rs.getInt(1);
				String name=rs.getString(2);
				String email=rs.getString("email");
				Date birth=rs.getDate(4);
				System.out.println(id+" "+name+" "+email+" "+birth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭数据库资源
			JDBCTest02Toos.release(statement, connection, rs);
		}
		
	}
}
