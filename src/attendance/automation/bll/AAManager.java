/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.bll;

import attendance.automation.be.Class;
import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.be.Teacher;
import attendance.automation.dal.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Revy
 */
public class AAManager {

  private static AAManager manager;
  private UserDAO ud;
  private DateDAO dd;
  private ClassDAO cd;
  private TeacherDAO td;
  private StudentDAO sd;
  private Person person;

  public AAManager() throws IOException {
    ud = new UserDAO();
    dd = new DateDAO();
    cd = new ClassDAO();
    sd = new StudentDAO();
    td = new TeacherDAO();
  }

  public static AAManager getInstance() throws IOException {
    if (manager == null) {
      manager = new AAManager();
      return manager;
    } else {
      return manager;
    }
  }

  public boolean checkLogin(String login, String password) throws DALException, IOException {
    return ud.checkLogin(login, password);
  }

  public void setUser() {
    person = ud.getPerson();
  }

  public boolean isTeacher() {
    return person.getClass() == Teacher.class;

  }


  public void setStudent(int studentID) throws DALException, SQLException, IOException {
    ud.setStudent(studentID);
  }


  public Person getPerson() {
    return person;
  }

  public boolean markAttendance(int studentID) throws DALException {
    LocalDate localDate = LocalDate.now();
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    return dd.markAttendance(studentID, date);
  }

  public double attendanceRate(Student student) throws DALException {
    double schoolDays = 0;
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = (Calendar) c1.clone();
   // Date date = student.getAttendance().get(0).getAttendanceDate();
      Date date = student.getAttendance().get(0);
    c2.setTime(date);
    while (c2.before(c1) || c2.equals(c1)) {
      if (c2.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
          && c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
        schoolDays++;
      }
      c2.add(Calendar.DATE, 1);
    }
    return dd.getAttendancesForThisMonth(student.getId()) / schoolDays;
  }

  public void changeAttendance(int studentID, java.sql.Date date, String distinguisher) {
    dd.changeAttendance(studentID, date, distinguisher);
  }


  public List<Class> loadTeacherContent(String userName) throws DALException, IOException {
    return td.loadTeacherContent(userName);
  }

public List<Date> loadStudentContent(String userName, List<Date> listOfAttendance) throws DALException {
    return sd.loadStudentContent(userName,listOfAttendance);
}

  public List<Student> loadClassContent(String className) throws IOException, DALException {
    return cd.loadClassContent(className);
  }

}
