/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.bll;

import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.be.Teacher;
import attendance.automation.be.Class;
import attendance.automation.dal.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Revy
 */
public class AAManager implements AAFacadeManager {

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

    @Override
    public boolean checkLogin(String login, String password) throws BLLException, IOException {
        try {
            return ud.checkLogin(login, password);
        } catch (DALException e) {
            throw new BLLException(e);
        }
    }

    @Override
    public void setUser() {
        person = ud.getPerson();
    }

    @Override
    public boolean isTeacher() {
        return person.getClass() == Teacher.class;

    }

    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public boolean markAttendance(int studentID) throws BLLException {
        try {
            LocalDate localDate = LocalDate.now();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return dd.markAttendance(studentID, date);
        } catch (DALException e)
        {
            throw new BLLException(e);
        }
    }

    @Override
    public double attendanceRate(Student student) throws BLLException {
        try {
            double schoolDays = 0;
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = (Calendar) c1.clone();
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
        } catch (DALException e)
        {
            throw new BLLException(e);
        }
    }

    @Override
    public void changeAttendance(int studentID, java.sql.Date date, String distinguisher) throws BLLException {
        try {
            dd.changeAttendance(studentID, date, distinguisher);
        } catch (DALException e) {
            throw new BLLException(e);
        }
    }

    @Override
    public List<Class> loadTeacherContent(int teacherID) throws BLLException, IOException {
        try {
            return td.loadTeacherContent(teacherID);
        } catch (DALException e) {
            throw new BLLException(e);
        }
    }

    @Override
    public List<Date> loadStudentContent(int studentID) throws BLLException {
        try {
            return sd.loadStudentContent(studentID);
        } catch (DALException e) {
            throw new BLLException(e);
        }
    }

    @Override
    public List<Student> loadClassContent(String className) throws IOException, BLLException {
        try {
            return cd.loadClassContent(className);
        } catch (DALException e) {
            throw new BLLException(e);
        }
    }


}
