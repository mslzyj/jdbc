package jdbc03;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * ResuktSetMetaData:����ResultSet��Ԫ���ݶ��󣬼����п��Ի�ȡ����������ж����У�������ʲô
 * 1.�õ�ResuktSetMetaData���󣺵���ResuleSet��getMetaData�������� 2.int
 * getColumnCount������SQL����а�����Щ�� 3.String getColumnLable��int
 * column������ȡָ���еı���������������1��ʼ Title:JDBCTest03
 * 
 * @author zyj
 * @date2017��6��24������10:41:39
 */
public class JDBCTest03 {

	@Test
	public void testResultSetMetaData() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT flow_id,type,id_card,exam_card,student_name,location,grade "
					+ " FROM examstudent WHERE  flow_id=?";
			connection = JDBCTools.getConnection();
			preparedStatement = (PreparedStatement) connection.prepareCall(sql);
			preparedStatement.setInt(1, 5);
			resultSet = preparedStatement.executeQuery();
			// 1.�õ�ResuktSetMetaData����
			ResultSetMetaData r = resultSet.getMetaData();
			// 2.����һ��Map<String,Object>����
			Map<String, Object> values = new HashMap<String, Object>();
			while (resultSet.next()) {
				// 2.��ӡÿһ�е�����
				for (int i = 0; i < r.getColumnCount(); i++) {
					String co = r.getColumnLabel(i + 1);
					System.out.println(co);
					Object columunvalue = resultSet.getObject(co);
					values.put(co, columunvalue);
				}
			}
			System.out.println(values);
			System.out.println("..............");
			// 3.��������������ResuktSetMetaData���3��Ӧ��Map����
			Class clazz = Student.class;
			Object object = clazz.newInstance();
			// 4.��map��Ϊ�գ����÷��䴴��Clazz����Ķ���
			for (Map.Entry<String, Object> entry : values.entrySet()) {
				// 5.����map�������÷����class����Ķ�Ӧ�����Ը�ֵ
				String filedName = entry.getKey();
				Object filedValue = entry.getValue();
				System.out.println(filedName + ":" + filedValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(preparedStatement, connection, resultSet);
		}
	}
}
