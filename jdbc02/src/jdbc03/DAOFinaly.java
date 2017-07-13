package jdbc03;

import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Connection;

/**
 * �������ݵ�DAO�ӿ�
 * ��߶���÷������ݱ�ĸ��ַ���
 * 
 *Title:DAOFinaly
 *@author zyj
 *@date2017��7��11������10:41:43
 *T:DAO�����ʵ����ĵ�����
 */
public interface DAOFinaly<T> {
	/**
	 * ��������ķ���
	 * @param connection
	 * @param sql
	 * @param args�����ռλ����Object[],���Ϳɱ����
	 */
    void batch(Connection connection,String sql,Object[]...args);
	/**
    * ����һ�������ֵ�����磺��������ƽ�����ʵ�
    * @param connection
    * @param sql
    * @param args
    * @return
    */
	<E> E getForValue(Connection connection,String sql,Object...args);
	/**
     * ����T��һ������	
     * @param connection
     * @return
     */
	List <T> getForList(Connection connection,String sql,Object...args);
	/**
	 * ����һ��T�Ķ���
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	T get(Connection connection,String sql,Object ... args) throws SQLException;
	/**
	 * INSERT,UPDATE,DELECT
	 * @param connection;���ݿ�����
	 * @param sql��SQL���
	 * @param args�����ռλ��
	 */
     void update(Connection connection,String sql,Object ... args );
}
