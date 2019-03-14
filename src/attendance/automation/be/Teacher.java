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

/**
 *
 * @author Revy
 */
public class Teacher implements Person {
    private String name;
    private int id;
    private List<Class> listOfClasses;
    private final ConnectionProvider cp;
    
    public Teacher(String name, int id) throws IOException, DALException{
        this.name=name;
        this.id=id;
        listOfClasses = new ArrayList<>();
        cp = new ConnectionProvider();
        loadTeacherContent();
    }
    
    public void loadTeacherContent() throws DALException, IOException{
        
        try{
        Connection con = cp.getConnection();
        Statement statement = con.createStatement();
        String str = "SELECT Class.ID, ClassName FROM Teacher, TeachersClass, Class WHERE Teacher.ID=TeachersClass.TeacherID AND TeachersClass.ClassID = Class.ID AND UserName='"+name+"'";
        ResultSet rs = statement.executeQuery(str);
        while(rs.next()){
             String name1 = rs.getString("ClassName");
             System.out.println(name1);
             int number = rs.getInt("ID");
             listOfClasses.add(new Class(name1,number));}
        }
        
        catch (SQLException ex) {
            throw new DALException (ex);
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
    
    public void setClassesList(List<Class> list){
        listOfClasses.addAll(list);
    }
    public List<Class> getClassesList(){
         return listOfClasses;
    }
}
