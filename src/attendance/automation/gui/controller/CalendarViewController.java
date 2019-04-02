/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.dal.DALException;
import attendance.automation.gui.model.AAModel;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author RebelStoke
 */
public class CalendarViewController implements Initializable {


    @FXML
    private GridPane GridCalendar;
    @FXML
    private Label labelDate;

    private AAModel aamodel;
    private Calendar calendar;
    private Calendar today;
    private Calendar firstDay;
    private List<Date> attendance = new ArrayList<>();
    private List<Date> dateList = new ArrayList<>();
    private String buttonColor;
    private Student student;
    private Map<Integer, java.sql.Date> map = new HashMap<>();
    private int redButtons = 0;
    private int greenButtons = 0;
    private Calendar cal;
    private String green;
    private String red;

    CalendarViewController(Person student) {
        this.student = (Student) student;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            green = "#65d355";
            red = "#e75757";
            aamodel = AAModel.getInstance();
            calendar = Calendar.getInstance();
            today = (Calendar) calendar.clone();
            firstDay = (Calendar) calendar.clone();
            firstDay.setTime(student.getAttendance().get(0));
            attendance = student.getAttendance();
            attendanceUnitToCalendarList();
            setMonthlyCalendar(calendar);
        } catch (IOException ex) {
            Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pressButtonPreviousMonth() {
        GridCalendar.getChildren().clear();
        calendar.add(Calendar.MONTH, -1);
        attendanceUnitToCalendarList();
        setMonthlyCalendar(calendar);
    }

    @FXML
    private void pressButtonNextMonth() {
        GridCalendar.getChildren().clear();
        calendar.add(Calendar.MONTH, 1);
        attendanceUnitToCalendarList();
        setMonthlyCalendar(calendar);
    }

    public void setStudent(Person student) {
        this.student = (Student) student;

    }

    private void attendanceUnitToCalendarList() {
        dateList.clear();
        dateList.addAll(attendance);
    }

    private void setMonthlyCalendar(Calendar calendar) {
        int x;
        int y = 0;
        redButtons = 0;
        greenButtons = 0;

        cal = (Calendar) calendar.clone();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int daysInTheMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int z = cal.get(Calendar.DAY_OF_WEEK);
        if (z == 1) {
            x = 6;
        } else {
            x = z - 2;
        }

        for (int i = 1; i <= daysInTheMonth; ++i) {

            if (x == 7) {
                x = 0;
                y++;
            }

            if (checkDateList(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH),
                    cal.get(Calendar.YEAR))) {
                addButton(x, y, i, green);
                greenButtons++;
                map.put(i, datePass(cal));
            } else if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.before(today) && cal
                    .after(firstDay)) {

                addButton(x, y, i, red);
                redButtons++;
                map.put(i, datePass(cal));
            } else {

                addButton(x, y, i, "Grey");
                map.put(i, datePass(cal));
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
            x++;
        }
        setMonthAttendanceLabel(this.cal, greenButtons, redButtons);
    }

    private void setMonthAttendanceLabel(Calendar cal, int greenButtons, int redButtons) {
        labelDate.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + "/" + cal
                .get(Calendar.YEAR) + " - (" + (greenButtons +" / "+ (greenButtons + redButtons) )
                + ")");
        labelDate.setAlignment(Pos.TOP_RIGHT);
    }

    private java.sql.Date datePass(Calendar cal) {
        return new java.sql.Date(cal.getTimeInMillis());
    }

    private void addButton(int x, int y, int i, String color) {
        JFXButton butt = new JFXButton();
        buttonColor =
                "-fx-background-color:" + color + "; -fx-font-size: 13px; -fx-background-radius: 0";
        butt.setText("" + i);
        butt.setStyle(buttonColor);
        butt.setMinSize(36, 36);
        butt.setMaxSize(36, 36);
        butt.setOnAction(e -> {
            if (aamodel.isTeacher() && !color.equals("Grey")) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                        "Do you want to change student attendance for this day?", ButtonType.YES,
                        ButtonType.NO);
                a.showAndWait();
                if (a.getResult() == ButtonType.YES) {
                    if (color.equals("Green")) {
                        GridCalendar.getChildren().remove(butt);
                        addButton(x, y, i, "Red");
                        aamodel.changeAttendance(student.getId(), map.get(i), "Delete attendance");
                        greenButtons--;
                        redButtons++;
                        setMonthAttendanceLabel(this.cal, greenButtons, redButtons);
                    } else if (color.equals("Red")) {
                        GridCalendar.getChildren().remove(butt);
                        addButton(x, y, i, "Green");
                        aamodel.changeAttendance(student.getId(), map.get(i), "Change attendance");
                        greenButtons++;
                        redButtons--;
                        setMonthAttendanceLabel(this.cal, greenButtons, redButtons);
                    }
                    try {
                        student.loadStudentContent();

                    } catch (DALException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        GridCalendar.add(butt, x, y);
    }

    private boolean checkDateList(int day, int month, int year) {
        Date date2 = new GregorianCalendar(year, month, day).getTime();
        for (Date date : dateList) {
            if (date.equals(date2)) {
                return true;
            }
        }
        return false;
    }

}