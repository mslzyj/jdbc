package jdbc03;

import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Connection;

/**
 * 访问数据的DAO接口
 * 里边定义好访问数据表的各种方法
 * 
 *Title:DAOFinaly
 *@author zyj
 *@date2017年7月11日上午10:41:43
 *T:DAO处理的实体类的的类型
 */
public interface DAOFinaly<T> {
	/**
	 * 批量处理的方法
	 * @param connection
	 * @param sql
	 * @param args：填充占位符的Object[],类型可变参数
	 */
    void batch(Connection connection,String sql,Object[]...args);
	/**
    * 返回一个具体的值，例如：总人数，平均工资等
    * @param connection
    * @param sql
    * @param args
    * @return
    */
	<E> E getForValue(Connection connection,String sql,Object...args);
	/**
     * 返回T的一个集合	
     * @param connection
     * @return
     */
	List <T> getForList(Connection connection,String sql,Object...args);
	/**
	 * 返回一个T的对象
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	T get(Connection connection,String sql,Object ... args) throws SQLException;
	/**
	 * INSERT,UPDATE,DELECT
	 * @param connection;数据库连接
	 * @param sql：SQL语句
	 * @param args：填充占位符
	 */
     void update(Connection connection,String sql,Object ... args );
}
