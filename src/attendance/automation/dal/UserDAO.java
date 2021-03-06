/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.dal;

import attendance.automation.be.BEException;
import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.be.Teacher;
import attendance.automation.bll.BLLException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author Revy
 */
public class UserDAO {

    private final ConnectionProvider cp;
    private Person person;
    private Connection con;


    public UserDAO() throws IOException {
        cp = new ConnectionProvider();
    }

    public boolean checkLogin(String login, String password) throws DALException, IOException {
        try {
            person = null;
            con = cp.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Teacher");
            while (rs.next()) {
                String name = rs.getString("UserName");
                int id = rs.getInt("ID");
                if (login.equals(name)) {
                    if (password.equals(rs.getString("UserPassword"))) {
                        person = new Teacher(name, id);
                        return true;
                    }
                }
            }
            if (person == null) {
              return checkIfStudent(login, password, con);
            }
        } catch (DALException | SQLException ex) {
            throw new DALException(ex);
        } catch (BLLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean checkIfStudent(String login, String password, Connection conection) throws IOException, DALException {
        try {
            Connection con = conection;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Student");
            while (rs.next()) {
                String name = rs.getString("UserName");
                int id = rs.getInt("ID");
                int classNum = rs.getInt("ClassNum");
                if (login.equals(name)) {
                    if (password.equals(rs.getString("UserPassword"))) {
                        person = new Student(name, classNum, id);
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException | BEException ex)
        {
            throw new DALException(ex);
        }
    }


    public Person getPerson() {
        return person;
    }


}

