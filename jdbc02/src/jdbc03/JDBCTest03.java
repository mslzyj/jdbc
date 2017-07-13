package jdbc03;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * ResuktSetMetaData:描述ResultSet的元数据对象，即从中可以获取到结果集中有多少列，列名是什么
 * 1.得到ResuktSetMetaData对象：调用ResuleSet的getMetaData（）方法 2.int
 * getColumnCount（）；SQL语句中包含那些列 3.String getColumnLable（int
 * column）：获取指定列的别名，其中索引从1开始 Title:JDBCTest03
 * 
 * @author zyj
 * @date2017年6月24日上午10:41:39
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
			// 1.得到ResuktSetMetaData对象
			ResultSetMetaData r = resultSet.getMetaData();
			// 2.创建一个Map<String,Object>对象，
			Map<String, Object> values = new HashMap<String, Object>();
			while (resultSet.next()) {
				// 2.打印每一列的列名
				for (int i = 0; i < r.getColumnCount(); i++) {
					String co = r.getColumnLabel(i + 1);
					System.out.println(co);
					Object columunvalue = resultSet.getObject(co);
					values.put(co, columunvalue);
				}
			}
			System.out.println(values);
			System.out.println("..............");
			// 3.处理结果集，利用ResuktSetMetaData填充3对应的Map对象
			Class clazz = Student.class;
			Object object = clazz.newInstance();
			// 4.若map不为空，利用反射创建Clazz对象的对象
			for (Map.Entry<String, Object> entry : values.entrySet()) {
				// 5.遍历map对象，利用反射对class对象的对应的属性赋值
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
