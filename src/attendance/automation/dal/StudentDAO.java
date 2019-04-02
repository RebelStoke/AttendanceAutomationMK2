package attendance.automation.dal;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentDAO {

    private final ConnectionProvider cp;



    public StudentDAO() throws IOException {
        cp = new ConnectionProvider();
    }

    public List<Date> loadStudentContent(String userName) throws DALException {
        List<Date> listOfAttendance = new ArrayList<>();
        try {
            Connection con = cp.getConnection();
            Statement statement = con.createStatement();
            String str =
                    "SELECT * FROM Student, StudentAttendance WHERE Student.ID=StudentID AND UserName='"
                            + userName + "' ORDER BY Date ASC";
            ResultSet rs = statement.executeQuery(str);
            while (rs.next()) {
                Date date = rs.getDate("Date");
                listOfAttendance.add(date);
            }
        } catch (SQLException ex) {
            throw new DALException(ex);
        }
        return listOfAttendance;
    }


}
