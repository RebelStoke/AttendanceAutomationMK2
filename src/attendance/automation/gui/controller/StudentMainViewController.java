/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.be.Student;
import attendance.automation.bll.AAManager;
import attendance.automation.dal.DALException;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Tothko
 */
public class StudentMainViewController implements Initializable {

    @FXML
    private Label welcomeLabel;
    private AAManager manager;
    private Student st;
    private Label dateLabel;
    @FXML
    private Label attendanceLabel;
    @FXML
    private Label attendanceRate;
    Calendar mCalendar;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnLogin;
    private double xOffset = 0;
    private double yOffset = 0;
    private FXMLLoader loader;
    @FXML
    private AnchorPane paneCalendar;

    
    @FXML
    private JFXButton btnMinimize;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            manager = AAManager.getInstance();
            st = manager.getStudent();
            welcomeLabel.setText("Welcome " + st.getName());
            if(st.getAttendance().size()>0)
            calculateAttendanceRate();

            fadeIn(btnExit);
            fadeIn(btnLogin);

            loadCalendar();
        } catch (DALException | IOException ex) {
            Logger.getLogger(StudentMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void closeButton(ActionEvent event) {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void attendanceButton(ActionEvent event) throws DALException, SQLException, IOException {
        if (mCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && mCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            checkAttendance();
        } else {
            attendanceLabel.setText("Today is weekend!");
        }

    }

    public void calculateAttendanceRate() throws DALException {
        String string = "Total attendance " + (int) (manager.attendanceRate(st) * 100) + "%";
        attendanceRate.setText(string);
    }

    public void checkAttendance() throws DALException, SQLException, IOException {
        if (manager.markAttendance(st.getId())) {
            attendanceLabel.setText("Attendance marked successfully!");
            calculateAttendanceRate();
            manager.setStudent(st.getId());
            manager.setUser();
            st = manager.getStudent();
            loadCalendar();
        } else {
            attendanceLabel.setText("Attendance already marked!");
        }
    }

    private void fadeIn(Node node) {
        FadeTransition exitFade = new FadeTransition(Duration.seconds(2), node);
        exitFade.setFromValue(0);
        exitFade.setToValue(1);
        exitFade.play();
    }

    public Student getStudent() {
        return st;
    }
    public void loadCalendar() throws IOException{
            CalendarViewController kokot = new CalendarViewController(this, null, st);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/CalendarView.fxml"));
            loader.setController(kokot);
            Pane kokotina = new Pane();
            kokotina = loader.load();

            paneCalendar.getChildren().clear();
            paneCalendar.getChildren().add(kokotina);
    }

    @FXML
    private void minimizeButton(ActionEvent event) {
        Stage stage = (Stage)welcomeLabel.getScene().getWindow();
        stage.setIconified(true);
    }

}
