/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.be;

import attendance.automation.dal.ConnectionProvider;
import attendance.automation.dal.DALException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Revy
 */
public class Class {
    private String name;
    private int id;
    private final List<Student> listOfStudents;
    private final ConnectionProvider cp;
    
    public Class(String name, int id) throws IOException, DALException{
        this.name=name;
        this.id=id;
        listOfStudents= new ArrayList<>();
        cp = new ConnectionProvider();
        loadClassContent();
    }
    public void loadClassContent() throws DALException, IOException{
        try{
        Connection con = cp.getConnection();
        Statement statement = con.createStatement();
        String str = "SELECT UserName, ClassNum, Student.ID FROM Student, Class WHERE Class.ID=Student.ClassNum AND ClassName='"+name+"'";
        ResultSet rs = statement.executeQuery(str);
        while(rs.next()){
            String userName = rs.getString("UserName");
            System.out.println(userName);
            int classNum = rs.getInt("ClassNum");
            int studentID = rs.getInt("ID");
            Student student = new Student(userName,classNum,studentID);
            student.setClassName(name);
            listOfStudents.add(student);
        }
       }   
        catch (SQLException ex) {
            throw new DALException(ex);
        }
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
    
    public void setStudentsList(List<Student> list){
        listOfStudents.addAll(list);
    }
    public List<Student> getStudentsList(){
         return listOfStudents;
    }
    @Override
    public String toString(){
        return name;
    }
}
