/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.be;

import attendance.automation.bll.AAManager;
import attendance.automation.dal.ConnectionProvider;
import attendance.automation.dal.DALException;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
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
  private AAManager manager;

  public Student(String name, int classNum, int id) throws IOException, DALException {
    this.name = name;
    this.classNum = classNum;
    this.id = id;
    listOfAttendance = new ArrayList<>();
    manager = AAManager.getInstance();
    loadStudentContent();
    setAttendanceOfStudent();
  }

  public List<Date> getAttendance() {
    return listOfAttendance;
  }


  public void loadStudentContent() throws DALException {
    listOfAttendance.clear();
    listOfAttendance.addAll(manager.loadStudentContent(this.name));
  }


  private void setAttendanceOfStudent() throws DALException {
    attendanceOfStudent = (int) (manager.attendanceRate(this) * 100) + "%";

  }

  public String getAttendanceOfStudent() {
    return attendanceOfStudent;
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
}
