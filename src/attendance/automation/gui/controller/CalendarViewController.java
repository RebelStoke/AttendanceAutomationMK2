/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.be.AttendanceUnit;
import attendance.automation.be.Student;
import attendance.automation.bll.AAManager;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Tothko
 */
public class CalendarViewController implements Initializable {

    private URL url;
    private ResourceBundle rb;
    @FXML
    private GridPane GridCalendar;
    @FXML
    private Label labelDate;
    private Label monday;
    private Label tuesday;
    private Label wednesday;
    private Label thursday;
    private Label friday;
    private Label saturday;
    private Label sunday;
    private AAManager manager;
    private Date date = new Date();
    private Date dateToPassFinal;
    private int x;
    private int y;
    private Calendar calendar;
    private Calendar today;
    private Calendar firstDay;
    private List<AttendanceUnit> attendance = new ArrayList<>();
    private List<Date> dateList = new ArrayList<>();
    private String buttonColor;
    private StudentMainViewController SMWC;
    private TeacherMainViewController TMWC;
    private Student student;
    private HashMap<Integer, String> map;
    private HashMap<Integer, Date> map2;
    private String distinguisher;

    CalendarViewController(StudentMainViewController studentController, TeacherMainViewController teacherController, Student student) {
        SMWC = studentController;
        TMWC = teacherController;
        this.student = student;
        map = new HashMap<>();
        map2 = new HashMap<>();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            manager = new AAManager();
            calendar = Calendar.getInstance();
            today = (Calendar) calendar.clone();
            firstDay = (Calendar) calendar.clone();
            firstDay.setTime(student.getAttendance().get(0).getAttendanceDate());
            attendance = student.getAttendance();
            attendanceUnitToCalendarList();
            setMonthlyCalendar(calendar);
        } catch (IOException ex) {
            Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pressButtonPreviousMonth(ActionEvent event) {
        GridCalendar.getChildren().clear();
        calendar.add(Calendar.MONTH, -1);
        setMonthlyCalendar(calendar);
    }

    @FXML
    private void pressButtonNextMonth(ActionEvent event) {
        GridCalendar.getChildren().clear();
        calendar.add(Calendar.MONTH, 1);
        setMonthlyCalendar(calendar);
    }

    public void setStudent(Student student) {
        this.student = student;

    }

    private void loadDaysLabel() {
        monday = new Label();
        GridCalendar.add(monday, 0, 0);
        monday.setText("Mo");
        tuesday = new Label();
        GridCalendar.add(tuesday, 1, 0);
        tuesday.setText("Tu");
        wednesday = new Label();
        GridCalendar.add(wednesday, 2, 0);
        wednesday.setText("We");
        thursday = new Label();
        GridCalendar.add(thursday, 3, 0);
        thursday.setText("Th");
        friday = new Label();
        GridCalendar.add(friday, 4, 0);
        friday.setText("Fr");
        saturday = new Label();
        GridCalendar.add(saturday, 5, 0);
        saturday.setText("Sa");
        sunday = new Label();
        GridCalendar.add(sunday, 6, 0);
        sunday.setText("Su");

    }

    private void attendanceUnitToCalendarList() {
        for (AttendanceUnit attendanceUnit : attendance) {
            System.out.println(attendanceUnit.getAttendanceDate());
            dateList.add(attendanceUnit.getAttendanceDate());
        }

    }

    private void setMonthlyCalendar(Calendar cal2) {
        double redButtons = 0;
        double greenButtons = 0;

        Calendar cal = (Calendar) cal2.clone();
        loadDaysLabel();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int daysInTheMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int z = cal.get(Calendar.DAY_OF_WEEK);
        if (z == 1) {
            x = 6;
        } else {
            x = z - 2;
        }

        y = 1;
        for (int i = 1; i <= daysInTheMonth; ++i) {

            if (x == 7) {
                x = 0;
                y++;
            }

            if (checkDateList(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))) {
                datePass(cal);


                buttonColor = "-fx-background-color: Green; -fx-font-size: 13px; -fx-background-radius: 0";
                addButton(x, y, i);
                greenButtons++;
                map.put(i, "Green");
                map2.put(i, dateToPassFinal);
            } else if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.before(today) && cal.after(firstDay)) {
                datePass(cal);

                buttonColor = "-fx-background-color: Red; -fx-font-size: 13px; -fx-background-radius: 0";
                addButton(x, y, i);
                redButtons++;
                map.put(i, "Red");
                map2.put(i, dateToPassFinal);
            } else {
                datePass(cal);

                buttonColor = "-fx-background-color: Grey; -fx-font-size: 13px; -fx-background-radius: 0";
                addButton(x, y, i);
                map.put(i, "Grey");
                map2.put(i, dateToPassFinal);
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
            x++;
        }
        labelDate.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + "/" + cal.get(Calendar.YEAR) + " <" + (int) ((greenButtons / (greenButtons + redButtons)) * 100) + "%>");
        labelDate.setAlignment(Pos.TOP_RIGHT);
    }

    private void datePass(Calendar cal) {
        //noinspection deprecation
        dateToPassFinal = new java.sql.Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

    }

    private void addButton(int x, int y, int i) {
        JFXButton butt = new JFXButton();
        butt.setText("" + i);
        butt.setStyle(buttonColor);
        butt.setMinSize(36, 36);
        butt.setMaxSize(36, 36);
        butt.setOnAction(e -> {
            System.out.println(map2.get(i));
            if (TMWC != null && !map.get(Integer.parseInt(butt.getText())).equals("Grey")) {
               Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to change student attendance for this day?", ButtonType.YES,ButtonType.NO);
               a.showAndWait();
               if (a.getResult() == ButtonType.YES) {
                    if (map.get(Integer.parseInt(butt.getText())).equals("Green")) {
                        buttonColor = "-fx-background-color: #ff2a2a; -fx-font-size: 13px; -fx-background-radius: 0";
                        map.put(i, "Red");
                        butt.setStyle(buttonColor);
                        distinguisher = "Delete attendance";
                        manager.changeAttendance(student.getId(), map2.get(i), distinguisher);
                        System.out.println("Tuuu: " + student.getId());
                    } else if (map.get(Integer.parseInt(butt.getText())).equals("Red")) {
                        buttonColor = "-fx-background-color: Green; -fx-font-size: 13px; -fx-background-radius: 0";
                        map.put(i, "Green");
                        butt.setStyle(buttonColor);
                        distinguisher = "Change attendance";
                        manager.changeAttendance(student.getId(), map2.get(i), distinguisher);
                        System.out.println("Tuuu: " + student.getId());
                    }
                }
            }
        });
        GridCalendar.add(butt, x, y);
    }

    private boolean checkDateList(int day, int month, int year) {
        Date date2 = new GregorianCalendar(year, month, day).getTime();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2);
        for (Date date : dateList) {
            if (date.equals(date2)) {
                return true;
            }
        }
        return false;
    }

}
