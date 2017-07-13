package jdbc03;

import static org.junit.Assert.fail;

import java.awt.List;
import java.sql.Date;

import org.junit.Test;

public class DAOTest {
 
	DAO dao = new DAO();
	
	@Test
	public void testUpdate() {
		String sql = "INSERT INTO `table`(name,email,birth) VALUES(?,?,?)";
		dao.update(sql, "’≈Àƒ","x@qq.com",new Date(new java.util.Date().getTime()));
	}

	@Test
	public void testGet() {
		String sql = "SELECT flow_id flowID,type,exam_card examCard,id_card idCard,student_name studentName,location,grade  FROM `examstudent` WHERE flow_id=?";
		Student student = dao.Get(Student.class, sql, 4);
		System.out.println(student);
	}

	@Test
	public void testGetforList() {
		String sql = "SELECT flow_id flowID,type,exam_card examCard,id_card idCard,student_name studentName,location,grade  FROM `examstudent`";
		//java.util.List<Student> student=(java.util.List<Student>) dao.getforList(Student.class, sql);
	   // System.out.println(student);
	}

	@Test
	public void testGetForValue() {
		fail("Not yet implemented");
	}

}
