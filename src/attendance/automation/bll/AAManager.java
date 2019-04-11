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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private List<Date> listOfAttendance;

    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;


    private AAManager() throws IOException {
        monday = 0;
        tuesday = 0;
        wednesday = 0;
        thursday = 0;
        friday = 0;
        ud = new UserDAO();
        dd = new DateDAO();
        cd = new ClassDAO();
        sd = new StudentDAO();
        td = new TeacherDAO();
    }

    public static AAManager getInstance() throws IOException {
        if (manager == null) {
            manager = new AAManager();
        }
        return manager;
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
        } catch (DALException e) {
            throw new BLLException(e);
        }
    }

    @Override
    public double attendanceRate(Student student) throws BLLException {
        try
        {
            return dd.getAttendancesForThisMonth(student.getId()) / calcSchoolDays(student);
        } catch (DALException ex)
        {
            throw new BLLException(ex);
        }  
    }
    
    protected double calcSchoolDays(Student student) throws BLLException
    {
        double schoolDays = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = (Calendar) c1.clone();
        Date date = student.getAttendance().get(0);
        Date maybeAbsent;
        c2.setTime(date);
        while (c2.before(c1) || c2.equals(c1)) {
            if (c2.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                int year = c2.get(Calendar.YEAR);
                int month = c2.get(Calendar.MONTH);
                int day = c2.get(Calendar.DAY_OF_MONTH);
                maybeAbsent = new GregorianCalendar(year, month, day).getTime();
                if (!checkIfDayAbsent(maybeAbsent))
                    calculateAbsentDays(c2.get(Calendar.DAY_OF_WEEK));
                schoolDays++;
            }
            c2.add(Calendar.DATE, 1);
        }
        return schoolDays;
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
            listOfAttendance = sd.loadStudentContent(studentID);
            return listOfAttendance;
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


    private boolean checkIfDayAbsent(Date maybeAbsent) {
        boolean b = false;
        for (Date day : listOfAttendance) {
            if (day.equals(maybeAbsent))
                b = true;
        }
        return b;
    }

    private void calculateAbsentDays(int dayofWeek) {
        if (dayofWeek == Calendar.MONDAY) monday++;
        if (dayofWeek == Calendar.TUESDAY) tuesday++;
        if (dayofWeek == Calendar.WEDNESDAY) wednesday++;
        if (dayofWeek == Calendar.THURSDAY) thursday++;
        if (dayofWeek == Calendar.FRIDAY) friday++;
    }

    @Override
    public String setMostAbsent() {
        List<Integer> indexes = new ArrayList<>();
        List<Integer> weekDays = Arrays.asList(monday, tuesday, wednesday, thursday, friday);
        int index=-1;
        int dayAbsenceCount=0;
        String theMostAbsent = "";

        for (int i = 0; i < 5; i++) {
        if(weekDays.get(i)>dayAbsenceCount) {
            indexes.clear();
            dayAbsenceCount=weekDays.get(i);
            index = i;
            indexes.add(index);
        }else if(weekDays.get(i)==dayAbsenceCount && indexes.size()<=1 && weekDays.get(i)!=0)
            indexes.add(i);
        }

        if(!indexes.isEmpty()){
        if(indexes.contains(0)) theMostAbsent+="Monday ";
        if(indexes.contains(1)) theMostAbsent+="Tuesday ";
        if(indexes.contains(2)) theMostAbsent+="Wednesday ";
        if(indexes.contains(3)) theMostAbsent+="Thursday ";
        if(indexes.contains(4)) theMostAbsent+="Friday ";}
        else theMostAbsent="None";
        return theMostAbsent;
    }

}
