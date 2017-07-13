package jdbc02;

import java.sql.ResultSet;
import java.util.Scanner;

import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * 通过控制台对数据库表中的数据进行操作
 * Title:JDBCTest
 * @author zyj
 * @date2017年6月18日下午1:24:46
 */
public class JDBCTest {
	// 获取数据库中的信息
	@Test
	public void testGetStudent() {
		// 1.得到查询的类型
		int searchType = getSearchTypeFromConsole();
		// 2.具体查询学生信息
		Student student = searchStudent(searchType);
		// 3.打印学生信息
		printStudent(student);

	}
    /**
     * 打印学生信息:若学生存在，打印具体信息，若不存在：查无此人
     * @param student
     */
	private void printStudent(Student student) {
		
		if(student!=null){
			System.out.println(student);
		}else{
			System.out.println("查无此人！");
		}

	}
    /**
     * 具体查询学生信息
     * @param searchType
     * @return
     */
	private Student searchStudent(int searchType) {
		String sql = "SELECT flowid,type,idcard,examcard,studentname,location,grado "+"FROM examstudent "+"WHERE  ";
		Scanner scanner = new Scanner(System.in);
		// 根据输入的searchType，提示用户输入信息：
		//若SearchType为1，提示：请输入身份证号，若为2，请输入准考证号
		//根据searchType确定SQL
		if(searchType == 1){
			System.out.println("请输入准考证号：");
			String examCard = scanner.next();
			sql= sql+" examcard="+examCard+" ";
			
		}else{
			System.out.println("请输入身份证号：");
			
			String examCard = scanner.next();
			sql= sql+" idcard="+examCard+" ";
		}
		//执行查询
		Student student = getStudent(sql);
		//若存在查询结果，吧查询结果封装在一个为Student对象
		return student;
	}
   /**
     * 根据传入的sql返回Student对象
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
	 * 从控制台读入一个整数，确定要查询的类型 1.用身份证查询 2.用准考证查询，其他无效，并提示请用户重新输入
	 * 
	 * @return
	 */
	private int getSearchTypeFromConsole() {
		System.out.println(".........查询信息.........");
		System.out.println("请输入查询类型：");
		Scanner scanner = new Scanner(System.in);
		int type=scanner.nextInt();
		if(type!=1 && type!=2){
			System.out.println("请重新输入：");
			//throw new RuntimeException();//使程序中断
			type=scanner.nextInt();
		}
		return type;
	}

	
    //以下为插入数据代码
	@Test
	public void testAddNewStudent() {
		Student student = getStudentFromConsole();
		addNewStudent(student);
	}

	/**
	 * 从控制台输入学生的信息
	 * 
	 * @return
	 */
	private Student getStudentFromConsole() {
		Scanner scanner = new Scanner(System.in);
		Student student = new Student();
        System.out.println("...........插入数据...........");
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
		// 1.准备一条SQL语句
		String sql = "INSERT INTO examstudent  " + " VALUES(" + student.getFlowID() + "," + student.getType() + ",'"
				+ student.getIdCard() + "','" + student.getExamCard() + "','" + student.getStudentName() + "','"
				+ student.getLocation() + "'," + student.getGrade() + ")";
		// 2.调用JDBCTools类的update（sql）方法执行插入操作
		JDBCTools.UpdatableResultSet(sql);
	}

}





