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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Revy
 */
public class Student extends RecursiveTreeObject<Student>{
    
    private String name;
    private int classNum;
    private int id;

    public List<AttendanceUnit> getAttendance() {
        return listOfAttendance;
    }
    private String className;
    private ConnectionProvider cp;
    private List<AttendanceUnit> listOfAttendance;
    private String attendanceOfStudent;
    private AAManager manager;
    
    public Student(String name, int classNum, int id) throws IOException, DALException{
        this.name=name;
        this.classNum=classNum;
        this.id=id;
        cp = new ConnectionProvider();
        listOfAttendance = new ArrayList<>();
        manager=AAManager.getInstance();
        loadStudentContent();
        setAttendanceOfStudent();
        System.out.println("attendance of student with id:"+id+": "+getAttendanceOfStudent());
    }
    private void loadStudentContent() throws DALException {
    try{
        Connection con = cp.getConnection();
        Statement statement = con.createStatement();
        String str = "SELECT * FROM Student, StudentAttendance WHERE Student.ID=StudentID AND UserName='"+name+"' ORDER BY Date ASC";
        ResultSet rs = statement.executeQuery(str);
        while(rs.next()){
             Date date1 = rs.getDate("Date");
             int number = rs.getInt("StudentID");
             listOfAttendance.add(new AttendanceUnit(number,date1));}
        }
        
        catch (SQLException ex) {
            throw new DALException (ex);
        }
    
    }

    private void setAttendanceOfStudent() throws DALException {
        attendanceOfStudent = (int) (manager.attendanceRate(this) * 100) +"%";
                
    }
    
    public String getAttendanceOfStudent(){
    return attendanceOfStudent;
    }
    
    
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    public int getClassNum()
    {
        return classNum;
    }

    public void setClassNum(int classNum)
    {
        this.classNum = classNum;
    }
    
    void setClassName(String className){
        this.className=className;
    }
    public String getClassName(){
        return className;
    }
}
