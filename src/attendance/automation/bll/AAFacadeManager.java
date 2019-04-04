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

    boolean checkLogin(String login, String password) throws DALException;

    void setUser();

    boolean isTeacher();


    Person getPerson();

    boolean markAttendance(int studentID) throws DALException;

    double attendanceRate(Student student) throws DALException;

    void changeAttendance(int studentID, java.sql.Date date, String distinguisher);

    List<Class> loadTeacherContent(int teacherID) throws DALException, IOException;

    List<Date> loadStudentContent(int studentID) throws DALException;

    List<Student> loadClassContent(String className) throws IOException, DALException;
}
