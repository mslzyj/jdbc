package jdbc01;

import java.sql.Date;
import java.sql.ResultSet;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class JDBCTest03 {
	/**
	 * ResultSet�����������װ��ʹ��JDBC���в�ѯ�Ľ��
	 * 1.����Statement�����executeQuery��sql�����Եõ������
	 * 2.ResultSet���ص���һ�����ݱ���һ��ָ��ָ�����ݱ�ĵ�һ�е�ǰ��
	 * ���Ե���next�������������һ���Ƿ���Ч������Ч�÷�������true����ָ�����ƣ��൱��Iterator�����hasnext������next���������Ľ����
	 * 3.��ָ���λ��һ��ʱ������ͨ����һ��ũgetxxx()������ȡÿһ�е�ֵ��ResultSet��Ҫ���йر�
	 * */
	@Test
	public void testResultSet(){
		//��ȡid=4��customers���ݱ�ļ�¼����ӡ
		Connection connection=null;
		Statement statement=null;
		ResultSet rs=null;
		try {
			//��ȡConnection
			connection=JDBCTest02Toos.getConnection();
			//��ȡStatement
			statement=(Statement) connection.createStatement();
			String  sql="select id,name,email,birth "+" from `table`  ";
			//ִ�в�ѯ���õ�ResultSet
			rs=statement.executeQuery(sql);
			//����ResultSet
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
			//�ر����ݿ���Դ
			JDBCTest02Toos.release(statement, connection, rs);
		}
		
	}
}
