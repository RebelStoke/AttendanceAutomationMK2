package attendance.automation.gui.model;

import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.be.Class;
import attendance.automation.bll.AAFacadeManager;
import attendance.automation.bll.AAManager;
import attendance.automation.dal.DALException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AAModel {

   public static AAModel model;
    AAFacadeManager aaManager;

    public AAModel() throws IOException {
      aaManager=new AAManager();
    }

    public static AAModel getInstance() throws IOException {
        if (model == null) {
            model = new AAModel();
            return model;
        } else {
            return model;
        }
    }

    public boolean checkLogin(String login, String password) throws DALException {
        return aaManager.checkLogin(login,password);
    }
    public void setUser(){
        aaManager.setUser();
    }
    public boolean isTeacher(){
        return aaManager.isTeacher();
    }
    public void setStudent(int studentID) throws DALException, IOException, SQLException {
        aaManager.setStudent(studentID);
    }
    public Person getPerson(){
        return aaManager.getPerson();
    }
    public boolean markAttendance(int studentID) throws DALException {
        return  aaManager.markAttendance(studentID);
    }
    public double attendanceRate(Student student) throws DALException {
        return aaManager.attendanceRate(student);
    }
    public void changeAttendance(int studentID, java.sql.Date date, String distinguisher){
        aaManager.changeAttendance(studentID,date,distinguisher);
    }
    public List<Class> loadTeacherContent(String userName) throws IOException, DALException {
        return aaManager.loadTeacherContent(userName);
    }
    public List<Date> loadStudentContent(String userName, List<Date> listOfAttendance){
        return  loadStudentContent(userName,listOfAttendance);
    }
    public List<Student> loadClassContent(String className){
        return  loadClassContent(className);
    }




}
