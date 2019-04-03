package attendance.automation.dal;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentDAO {

    private final ConnectionProvider cp;


    public StudentDAO() throws IOException {
        cp = new ConnectionProvider();
    }

    public List<Date> loadStudentContent(int stuID) throws DALException {
        List<Date> listOfAttendance = new ArrayList<>();
        try {
            Connection con = cp.getConnection();
            String str =
                    "SELECT StudentID,Date from StudentAttendance WHERE StudentID=?  ORDER BY Date ASC";
            PreparedStatement ppst = con.prepareStatement(str);
            ppst.setInt(1, stuID);
            ResultSet rs = ppst.executeQuery();
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
