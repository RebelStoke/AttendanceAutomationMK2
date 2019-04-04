/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.WindowOpener;
import attendance.automation.be.Student;
import attendance.automation.dal.DALException;
import attendance.automation.gui.model.AAModel;
import attendance.automation.gui.model.ModelException;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tothko
 */
public class StudentMainViewController implements Initializable {

    @FXML
    private Label welcomeLabel;
    private AAModel aamodel;
    private Student st;
    @FXML
    private Label attendanceLabel;
    @FXML
    private Label attendanceRate;
    private Calendar mCalendar;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private AnchorPane paneCalendar;
    private CalendarViewController calendarController;
    private FXMLLoader loader;
    private WindowOpener opener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            aamodel = AAModel.getInstance();
            st = (Student) aamodel.getPerson();
            welcomeLabel.setText("Welcome " + st.getName());

            if (st.getAttendance().size() > 0) {
                calculateAttendanceRate();
            }

            fadeIn(btnExit);
            fadeIn(btnLogin);
            mCalendar = Calendar.getInstance();
            loadCalendar();
        } catch (ModelException | IOException | DALException ex) {
            alertMessage(ex);
        }

    }

    @FXML
    private void closeButton() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void attendanceButton() throws DALException, IOException {
        if (mCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && mCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            checkAttendance();
        } else {
            attendanceLabel.setText("Today is weekend!");
        }
    }

    private void calculateAttendanceRate() throws DALException {
        try {
            String string = "Total attendance " + (int) (aamodel.attendanceRate(st) * 100) + "%";
            attendanceRate.setText(string);
        } catch (ModelException e) {
            alertMessage(e);
        }
    }

    private void checkAttendance() throws DALException, IOException {
        try {
            if (aamodel.markAttendance(st.getId())) {
                attendanceLabel.setText("Attendance marked successfully!");
                calculateAttendanceRate();
                loadCalendar();
            } else {
                attendanceLabel.setText("Attendance already marked!");
            }
        } catch (ModelException e) {
            alertMessage(e);
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

    private void loadCalendar() throws IOException {
        calendarController = new CalendarViewController();
        calendarController.setStudent(st);
        loader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/CalendarView.fxml"));
        loader.setController(calendarController);
        paneCalendar.getChildren().clear();
        paneCalendar.getChildren().add(loader.load());
    }

    @FXML
    private void minimizeButton() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setIconified(true);
    }

    public void logOut() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/LoginView.fxml"));
        opener = new WindowOpener(loader);
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
    }

    private void alertMessage(Exception ex)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }
}
