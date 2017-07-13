package jdbc03;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.Scanner;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * ͨ������̨�����ݿ���е����ݽ��в���
 * ����preparedStatement��String sql =....
 * 1.����preparedStatement��sexXXX ������int index,Object val������ռλ����ֵ
 * 2.ִ��SQL��䣬executeQuery()��executeUpdate() ִ��ʱ����Ҫ����SQL���
 * ��Ч�ķ�ֹSQLע��
 * sqlע�룺����ĳЩϵͳû�ж��û���������ݽ��г�ֵļ�飬�����û�����������ע��Ƿ���sql���λ����x�Ӷ������ϵͳ��SQL
 * ������ɶ�����Ϊ������������java���ԣ�Ҫ��ֹsqlע�룬��ֻҪ����PreParedStatementȡ��Statement���ɡ�
 * ��preparedStatement��Statement���ӽӿ�
 *Title:JDBCTest
 *@author zyj
 *@date2017��6��22������3:36:54 
 */
public class JDBCTest {
	
	/**
	 * SQlע��
	 * 
	 */
	@Test
	public void testSQLInjection(){
		
		String username="Tom";
		String pwd = "123456";
		/**
		 * ���¶�����뷽ʽҲ�ܵ�¼�ɹ�����SQLע��
		 * String username ="a' OR PWD=";
		 * String pwd="OR '1'='1";
		 * ʹ��preParedStatment����������������
		 */
		String sql="SELECT * FROM user WHERE username='"+username+"' AND "+"pwd='"+pwd+"'";
	    System.out.println(sql);
	    Connection connection=null;
	    Statement statement=null;
	    ResultSet resultSet=null;
	    try {
	    	connection=JDBCTools.getConnection();
	    	statement=(Statement) connection.createStatement();
	    	resultSet=statement.executeQuery(sql);
	    	if(resultSet.next()){
	    		System.out.println("��¼�ɹ�");
	    	}else{
	    		System.out.println("�û��������벻ƥ��");
	    	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			JDBCTools.release(statement, connection, resultSet);
		}
	}
	
	
	
	
	/**
	 * ��preparedStatement
	 */
	@Test
	public void testpreparedStatement(){
		Connection connection=null;
	    PreparedStatement preparedStatement=null;
	    try{
	    	connection=JDBCTools.getConnection();
	    	String sql = "INSERT INTO `table`(name,email,birth) "+"VALUES(?,?,?)";
	    	preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
	    	preparedStatement.setString(1, "h");
	    	preparedStatement.setString(2, "163@qq.con");
	    	//����ı�����sql��date����ʹ�����·�ʽ
	    	preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
	    	preparedStatement.executeUpdate();
	    			
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally {
			JDBCTools.release(preparedStatement, connection, null);
		}
	}
	
	// ��ȡ���ݿ��е���Ϣ
	@Test
	public void testGetStudent() {
		// 1.�õ���ѯ������
		int searchType = getSearchTypeFromConsole();
		// 2.�����ѯѧ����Ϣ
		Student student = searchStudent(searchType);
		// 3.��ӡѧ����Ϣ
		printStudent(student);

	}
    /**
     * ��ӡѧ����Ϣ:��ѧ�����ڣ���ӡ������Ϣ���������ڣ����޴���
     * @param student
     */
	private void printStudent(Student student) {
		
		if(student!=null){
			System.out.println(student);
		}else{
			System.out.println("���޴��ˣ�");
		}

	}
    /**
     * �����ѯѧ����Ϣ
     * @param searchType
     * @return
     */
	private Student searchStudent(int searchType) {
		String sql = "SELECT flowid,type,idcard,examcard,studentname,location,grado "+"FROM examstudent "+"WHERE  ";
		Scanner scanner = new Scanner(System.in);
		// ���������searchType����ʾ�û�������Ϣ��
		//��SearchTypeΪ1����ʾ�����������֤�ţ���Ϊ2��������׼��֤��
		//����searchTypeȷ��SQL
		if(searchType == 1){
			System.out.println("������׼��֤�ţ�");
			String examCard = scanner.next();
			sql= sql+" examcard="+examCard+" ";
			
		}else{
			System.out.println("���������֤�ţ�");
			
			String examCard = scanner.next();
			sql= sql+" idcard="+examCard+" ";
		}
		//ִ�в�ѯ
		Student student = getStudent(sql);
		//�����ڲ�ѯ������ɲ�ѯ�����װ��һ��ΪStudent����
		return student;
	}
   /**
     * ���ݴ����sql����Student����
     * @param sql
     * @return
     */
	private Student getStudent(String sql) {
		Student stu = null;
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		try {
			connection=JDBCTools.getConnection();
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				stu=new Student(resultSet.getInt(1),
						resultSet.getInt(2),
						resultSet.getString(3),
						resultSet.getString(4),
						resultSet.getString(5),
						resultSet.getString(6),
						resultSet.getInt(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(statement, connection, resultSet);
		}
		return stu;
	}
	/**
	 * �ӿ���̨����һ��������ȷ��Ҫ��ѯ������ 1.�����֤��ѯ 2.��׼��֤��ѯ��������Ч������ʾ���û���������
	 * 
	 * @return
	 */
	private int getSearchTypeFromConsole() {
		System.out.println(".........��ѯ��Ϣ.........");
		System.out.println("�������ѯ���ͣ�");
		Scanner scanner = new Scanner(System.in);
		int type=scanner.nextInt();
		if(type!=1 && type!=2){
			System.out.println("���������룺");
			//throw new RuntimeException();//ʹ�����ж�
			type=scanner.nextInt();
		}
		return type;
	}

	
    //����Ϊ�������ݴ���
	@Test
	public void testAddNewStudent() {
		Student student = getStudentFromConsole();
		addNewStudent2(student);
	}

	/**
	 * �ӿ���̨����ѧ������Ϣ
	 * 
	 * @return
	 */
	private Student getStudentFromConsole() {
		Scanner scanner = new Scanner(System.in);
		Student student = new Student();
        System.out.println("...........��������...........");
		System.out.print("FlowID: ");
		student.setFlowID(scanner.nextInt());
		System.out.print("Type: ");
		student.setType(scanner.nextInt());
		System.out.print("IdCard: ");
		student.setIdCard(scanner.next());
		System.out.print("ExamCard: ");
		student.setExamCard(scanner.next());
		System.out.print("StudentName: ");
		student.setStudentName(scanner.next());
		System.out.print("Location: ");
		student.setLocation(scanner.next());
		System.out.print("Grade: ");
		student.setGrade(scanner.nextInt());

		return student;
	}

	public void addNewStudent(Student student) {
		// 1.׼��һ��SQL���
		String sql = "INSERT INTO examstudent  " + " VALUES(" + student.getFlowID() + "," + student.getType() + ",'"
				+ student.getIdCard() + "','" + student.getExamCard() + "','" + student.getStudentName() + "','"
				+ student.getLocation() + "'," + student.getGrade() + ")";
		// 2.����JDBCTools���update��sql������ִ�в������
		JDBCTools.UpdatableResultSet(sql);
	}
	
	public void addNewStudent2(Student student){
	  String sql="INSERT INTO examstudent(flowid,type,idcard,examcard,studentname,location,grade) "+"VALUES(?,?,?,?,?,?,?)";
	  JDBCTools.UpdatableResultSet(sql,student.getFlowID(),student.getType(),student.getIdCard(),student.getExamCard(),student.getStudentName(),
			      student.getLocation(),student.getGrade());
	}
}





