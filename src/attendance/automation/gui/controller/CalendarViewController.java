/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.bll.AAManager;
import attendance.automation.dal.DALException;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


/**
 * FXML Controller class
 *
 * @author RebelStoke
 */
public class CalendarViewController implements Initializable {

  private URL url;
  private ResourceBundle rb;
  @FXML
  private GridPane GridCalendar;
  @FXML
  private Label labelDate;
  private AAManager manager;
  private Date date = new Date();
  private Calendar calendar;
  private Calendar today;
  private Calendar firstDay;
  private List<Date> attendance = new ArrayList<>();
  private List<Date> dateList = new ArrayList<>();
  private String buttonColor;
  private StudentMainViewController SMWC;
  private Student student;
  private Map<Integer, java.sql.Date> map;
  private double redButtons = 0;
  private double greenButtons = 0;
  private Calendar cal;
  CalendarViewController(StudentMainViewController studentController, Person student) {
    SMWC = studentController;
    this.student = (Student) student;
    map = new HashMap<>();

  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    try {
        manager = new AAManager();
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
  private void pressButtonPreviousMonth(ActionEvent event) throws DALException {
     attendance = student.getAttendance();
    GridCalendar.getChildren().clear();
    calendar.add(Calendar.MONTH, -1);
    attendanceUnitToCalendarList();
    setMonthlyCalendar(calendar);
  }

  @FXML
  private void pressButtonNextMonth(ActionEvent event) throws DALException {
     attendance = student.getAttendance();
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
  for (Date date : attendance) {
       dateList.add(date);
      }
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
        addButton(x, y, i, "Green");
        greenButtons++;
        map.put(i, datePass(cal));
      } else if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
          && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.before(today) && cal
          .after(firstDay)) {

        addButton(x, y, i, "Red");
        redButtons++;
        map.put(i, datePass(cal));
      } else {

        addButton(x, y, i, "Grey");
        map.put(i, datePass(cal));
      }

      cal.add(Calendar.DAY_OF_MONTH, 1);
      x++;
    }
    setMonthAttendanceLabel(this.cal,greenButtons,redButtons);
  }

  public void setMonthAttendanceLabel(Calendar cal, double greenButtons, double redButtons){
          labelDate.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + "/" + cal
                  .get(Calendar.YEAR) + " <" + (int) ((greenButtons / (greenButtons+redButtons)) * 100)
                  + "%>");
          labelDate.setAlignment(Pos.TOP_RIGHT);
  }

  private java.sql.Date datePass(Calendar cal) {
    return new java.sql.Date(cal.getTimeInMillis());
  }

  private void addButton(int x, int y, int i, String color)  {
    JFXButton butt = new JFXButton();
    buttonColor =
        "-fx-background-color:" + color + "; -fx-font-size: 13px; -fx-background-radius: 0";
    butt.setText("" + i);
    butt.setStyle(buttonColor);
    butt.setMinSize(36, 36);
    butt.setMaxSize(36, 36);
    butt.setOnAction(e -> {
      if (SMWC == null && !color.equals("Grey")) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
            "Do you want to change student attendance for this day?", ButtonType.YES,
            ButtonType.NO);
        a.showAndWait();
        if (a.getResult() == ButtonType.YES) {
            if (color.equals("Green")) {
            GridCalendar.getChildren().remove(butt);
            addButton(x, y, i, "Red");
            manager.changeAttendance(student.getId(), map.get(i), "Delete attendance");
            greenButtons--;
            redButtons++;
                setMonthAttendanceLabel(this.cal,greenButtons,redButtons);
          } else if (color.equals("Red")) {
            GridCalendar.getChildren().remove(butt);
            addButton(x, y, i, "Green");
            manager.changeAttendance(student.getId(), map.get(i), "Change attendance");
                greenButtons++;
                redButtons--;
                setMonthAttendanceLabel(this.cal,greenButtons,redButtons);
          }
            try {
                student.getAttendanceAfterChanges();

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
