package attendance.automation.dal;

import attendance.automation.be.Class;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    private final ConnectionProvider cp;



    public TeacherDAO() throws IOException {
        cp = new ConnectionProvider();
    }


    public List<Class> loadTeacherContent(String userName) throws DALException, IOException {
        List<Class> listOfClasses = new ArrayList<>();
        try{
            Connection con = cp.getConnection();
            Statement statement = con.createStatement();
            String str = "SELECT Class.ID, ClassName FROM Teacher, TeachersClass, Class WHERE Teacher.ID=TeachersClass.TeacherID AND TeachersClass.ClassID = Class.ID AND UserName='"+userName+"'";
            ResultSet rs = statement.executeQuery(str);
            while(rs.next()){
                String name1 = rs.getString("ClassName");
                int number = rs.getInt("ID");
                listOfClasses.add(new Class(name1,number));}
        }

        catch (SQLException ex) {
            throw new DALException (ex);
        }
        return listOfClasses;
    }
}
