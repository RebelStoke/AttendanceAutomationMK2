package attendance.automation.dal;

import attendance.automation.be.BEException;
import attendance.automation.be.Student;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    private final ConnectionProvider cp;
    private final UserDAO uDAO;

    public ClassDAO() throws IOException {
        cp = new ConnectionProvider();
        uDAO = new UserDAO();
    }

    public List<Student> loadClassContent(String className) throws IOException, DALException {
        List<Student> listOfStudents = new ArrayList<>();
        try{
            Connection con = cp.getConnection();
            String str = "SELECT UserName, ClassNum, Student.ID FROM Student, Class WHERE Class.ID=Student.ClassNum AND ClassName=?";
            PreparedStatement ppst = con.prepareStatement(str);
            ppst.setString(1,className);
            ResultSet rs = ppst.executeQuery();
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
        } catch (BEException e) {
            e.printStackTrace();
        }
        return listOfStudents;
    }


}
