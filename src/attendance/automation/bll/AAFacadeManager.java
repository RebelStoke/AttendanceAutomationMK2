package attendance.automation.bll;

import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.dal.DALException;
import attendance.automation.be.Class;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface AAFacadeManager {

    boolean checkLogin(String login, String password) throws BLLException, IOException;

    void setUser();

    boolean isTeacher();


    Person getPerson();

    boolean markAttendance(int studentID) throws BLLException;

    double attendanceRate(Student student) throws BLLException;

    void changeAttendance(int studentID, java.sql.Date date, String distinguisher) throws BLLException;

    List<Class> loadTeacherContent(int teacherID) throws BLLException, IOException;

    List<Date> loadStudentContent(int studentID) throws BLLException;

    List<Student> loadClassContent(String className) throws IOException, BLLException;
}
