package attendance.automation.gui.model;

import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.be.Class;
import attendance.automation.bll.AAFacadeManager;
import attendance.automation.bll.AAManager;
import attendance.automation.bll.BLLException;
import attendance.automation.dal.DALException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AAModel {
    public static AAModel model;
    AAFacadeManager aaManager;
    public AAModel() throws IOException {
            aaManager = new AAManager();
    }

    public static AAModel getInstance() throws ModelException {
        try {
            if (model == null) {
                model = new AAModel();
                return model;
            } else {
                return model;
            }
        } catch (IOException e)
        {
            throw new ModelException(e);
        }
    }

    public boolean checkLogin(String login, String password) throws ModelException {
        try {
            return aaManager.checkLogin(login, password);
        } catch (IOException | BLLException e) {
            throw new ModelException(e);
        }
    }

    public void setUser() {
        aaManager.setUser();
    }


    public boolean isTeacher() {
        return aaManager.isTeacher();
    }

    public Person getPerson() {
        return aaManager.getPerson();
    }


    public boolean markAttendance(int studentID) throws ModelException {
        try {
            return aaManager.markAttendance(studentID);
        } catch (BLLException e) {
            throw new ModelException(e);
        }
    }

    public double attendanceRate(Student student) throws ModelException {
        try {
            return aaManager.attendanceRate(student);
        } catch (BLLException e) {
            throw new ModelException(e);
        }
    }

    public void changeAttendance(int studentID, java.sql.Date date, String distinguisher) throws ModelException {
        try {
            aaManager.changeAttendance(studentID, date, distinguisher);
        } catch (BLLException e) {
            throw new ModelException(e);
        }
    }

    public List<Class> loadTeacherContent(int teacherID) throws IOException, ModelException {
        try {
            return aaManager.loadTeacherContent(teacherID);
        } catch (BLLException e) {
            throw new ModelException(e);
        }
    }

    public List<Date> loadStudentContent(int studentID) throws ModelException {
        try {
            return aaManager.loadStudentContent(studentID);
        } catch (BLLException e) {
            throw new ModelException(e);
        }
    }

    public List<Student> loadClassContent(int classID) {
        return loadClassContent(classID);
    }


}
