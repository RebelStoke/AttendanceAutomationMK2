/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.be;

import attendance.automation.gui.model.AAModel;
import attendance.automation.gui.model.ModelException;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Revy
 */
public class Student extends RecursiveTreeObject<Student> implements Person {

    private String name;
    private int classNum;
    private int id;
    private String className;
    private List<Date> listOfAttendance;
    private String attendanceOfStudent;
    private String absenceOfStudent;
    private AAModel aamodel;
    private int absenceComp;
    private String theMostAbsent;

    public Student(String name, int classNum, int id) throws BEException {
        try {
            this.name = name;
            this.classNum = classNum;
            this.id = id;
            listOfAttendance = new ArrayList<>();
            aamodel = AAModel.getInstance();
            loadStudentContent();
            setAttendanceOfStudent();
        } catch (ModelException e) {
            throw new BEException(e);
        }
    }

    public List<Date> getAttendance() {
        return listOfAttendance;
    }

    public void loadStudentContent() throws BEException {
        try {
            listOfAttendance.clear();
            listOfAttendance.addAll(aamodel.loadStudentContent(this.id));
        } catch (ModelException e) {
            throw new BEException(e);
        }
    }


    public void setAttendanceOfStudent() throws BEException {
        try {
            int attendance = (int) (aamodel.attendanceRate(this) * 100);

            attendanceOfStudent = attendance + "%";
            absenceOfStudent = 100 - attendance + "%";
            absenceComp = attendance;
        } catch (ModelException e) {
            throw new BEException(e);
        }
    }

    public String getAttendanceOfStudent() {
        return attendanceOfStudent;
    }

    public String getAbsenceOfStudent() {
        return absenceOfStudent;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAbsenceComp() {
        return absenceComp;
    }

    public int compareAbsence(Student absent) {
        return this.getAbsenceComp() - absent.getAbsenceComp();
    }

    public String getTheMostAbsent() {
        return theMostAbsent;
    }

    public void setTheMostAbsent(String theMostAbsent) {
        this.theMostAbsent = theMostAbsent;
    }
}