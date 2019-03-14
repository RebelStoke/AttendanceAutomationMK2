/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.be;

import java.util.Date;

/**
 *
 * @author Revy
 */
@SuppressWarnings("PublicMethodNotExposedInInterface")
public class AttendanceUnit {
    private int studentID;
    private Date attendanceDate;
    
    public AttendanceUnit(int studentID, Date attendanceDate){
        this.attendanceDate=attendanceDate;
        this.studentID=studentID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @Override
    public String toString() {
        return "AttendanceUnit{" + "studentID=" + studentID + ", attendanceDate=" + attendanceDate + '}';
    }
    
    
}
