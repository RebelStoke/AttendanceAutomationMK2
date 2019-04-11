package attendance.automation.bll;

import attendance.automation.be.Student;
import attendance.automation.dal.DateDAO;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leopo
 */
public class AAManagerTest
{
    AAManager instance;
    
    public AAManagerTest() throws IOException
    {
        instance = AAManager.getInstance();
    }

    @Test
    public void testCheckLogin() throws Exception
    {
        System.out.println("checkLogin");
        String login = "JanToth";
        String password = "1234";
        boolean result = instance.checkLogin(login, password);
        assertTrue("The login method is not working!", result);
        System.out.println("isTeacher");
        instance.setUser();
        boolean expResult = false;
        boolean resultTeacher = instance.isTeacher();
        assertEquals("The user is a teacher!", expResult, resultTeacher);
    }

    @Test
    public void testMarkAttendance() throws Exception
    {
        System.out.println("markAttendance");
        int studentID = 1;
        boolean result = instance.markAttendance(studentID);
        assertFalse(result);
    }

    @Test
    public void testAttendanceRate() throws Exception
    {
        System.out.println("attendanceRate");
        DateDAO dd = new DateDAO();
        Student student = (Student) instance.getPerson();
        student.setAttendanceOfStudent();
        double expResult = dd.getAttendancesForThisMonth(student.getId()) / instance.calcSchoolDays(student) ;
        double result = instance.attendanceRate(student);
        System.out.println(expResult + "");
        System.out.println(result + "");
        assertEquals("The attendance rate is not the same!", expResult, result, 0.0);
    }
    
}
