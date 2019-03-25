package attendance.automation.dal;

import attendance.automation.be.Student;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    private final ConnectionProvider cp;
    private final UserDAO uDAO;



    public ClassDAO() throws IOException, IOException {
        cp = new ConnectionProvider();
        uDAO = new UserDAO();
    }



    public List<Student> loadClassContent(String className) throws DALException, IOException{
        List<Student> listOfStudents = new ArrayList<>();
        try{
            Connection con = cp.getConnection();
            Statement statement = con.createStatement();
            String str = "SELECT UserName, ClassNum, Student.ID FROM Student, Class WHERE Class.ID=Student.ClassNum AND ClassName='"+className+"'";
            ResultSet rs = statement.executeQuery(str);
            while(rs.next()){
                String userName = rs.getString("UserName");
                int classNum = rs.getInt("ClassNum");
                int studentID = rs.getInt("ID");
                Student student = new Student(userName,classNum,studentID);
                student.setClassName(className);
                listOfStudents.add(student);
            }
        }
        catch (SQLException ex) {
            throw new DALException(ex);
        }
        return listOfStudents;
    }


}
