/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.dal;

import attendance.automation.be.Student;
import attendance.automation.be.Teacher;
import attendance.automation.bll.AAManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Revy
 */
public class UserDAO {
    
    private final ConnectionProvider cp;
    private Student st;
    private Teacher te;
     public UserDAO() throws IOException {
        cp = new ConnectionProvider();
        st = null;
        te = null;
    }
     
  public boolean checkLogin(String login, String password) throws DALException, IOException{
    try (Connection con = cp.getConnection()) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Teacher");
            while (rs.next()) {
               String name = rs.getString("UserName");
               int id = rs.getInt("ID");
                if(login.equals(name)){
                    if(password.equals(rs.getString("UserPassword"))){
                        te = new Teacher(name,id);
                        return true;}
                    }
                }
            rs = statement.executeQuery("SELECT * FROM Student");
            while (rs.next()) {
               String name = rs.getString("UserName");
               int id = rs.getInt("ID");
               int classNum = rs.getInt("ClassNum");
                if(login.equals(name)){
                    if(password.equals(rs.getString("UserPassword"))){
                        st = new Student(name,classNum,id);
                        return true;}
                    }
                }
            return false;
            } catch (SQLException ex) {
            throw new DALException(ex);
        }
            } 
  
  public Student getStudent(){
      return st;
  }
  public Teacher getTeacher(){
      return te;
  }
  public void setStudent(int studentID) throws DALException, SQLException, IOException{
      
         Connection con = cp.getConnection();
         Statement statement = con.createStatement();
         ResultSet rs = statement.executeQuery("SELECT * FROM Student WHERE ID="+studentID);
         
         while (rs.next()) {
         String name = rs.getString("UserName");
         int id = rs.getInt("ID");
         int classNum = rs.getInt("ClassNum");
         st = new Student(name,classNum,id);
         }
         this.st=st;
}
  
                
  
  
  
  
    }

