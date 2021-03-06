/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.WindowOpener;
import attendance.automation.be.*;
import attendance.automation.be.Class;
import attendance.automation.gui.model.AAModel;
import attendance.automation.gui.model.ModelException;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Revy
 */
public class TeacherMainViewController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private AnchorPane paneCalendar;
    @FXML
    private JFXComboBox<Class> selectClass;
    @FXML
    private JFXTextField studentSearch;
    @FXML
    private JFXTreeTableView<Student> tableView;
    private Teacher te;
    private AAModel aamodel;
    private List<Class> listOfClasses;
    private ObservableList<Class> observableClasses;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnMinimize;
    @FXML
    private ImageView pic;

    private CalendarViewController calendarController;
    private FXMLLoader loader;
    private WindowOpener opener;
    private Integer actualClassIndex;
    int lastSelectedStudentIndex;
    Student lastSelectedStudent;
    Person toCalendar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            aamodel = AAModel.getInstance();
            te = (Teacher) aamodel.getPerson();
            actualClassIndex=0;
            welcomeLabel.setText("Welcome, " + te.getName() + "!");
            listOfClasses = te.getClassesList();
            observableClasses = FXCollections.observableArrayList(listOfClasses);
            selectClass.setItems(observableClasses);

            selectClass.setPromptText(observableClasses.get(actualClassIndex).getName());
            setTableView();
            loadDataToTable(FXCollections.observableArrayList(observableClasses.get(0).getStudentsList()));
            toCalendar = observableClasses.get(0).getStudentsList().get(0);
            loadCalendar(toCalendar);

        } catch (IOException | ModelException e) {
            alertMessage(e);
        }

        fadeIn(btnExit);
        fadeIn(btnMinimize);
        fadeIn(pic);

    }

    private void setTableView() {
        JFXTreeTableColumn<Student, String> studentName = new JFXTreeTableColumn<>("Student");
        JFXTreeTableColumn<Student, String> studentAbsence = new JFXTreeTableColumn<>("Absence");
        JFXTreeTableColumn<Student, String> theMostAbsent = new JFXTreeTableColumn<>("The most absent");

        studentName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        studentAbsence.setCellValueFactory(new TreeItemPropertyValueFactory<>("absenceOfStudent"));
        theMostAbsent.setCellValueFactory(new TreeItemPropertyValueFactory<>("theMostAbsent"));

        tableView.getColumns().addAll(studentName, studentAbsence, theMostAbsent);
        studentSearch.textProperty().addListener((o, oldVal, newVal) -> {
            tableView.setPredicate(student ->
                    String.valueOf(student.getValue().getName()).toLowerCase().contains(newVal.toLowerCase())
                            || student.getValue().getAttendanceOfStudent().contains(newVal)
                            || student.getValue().getTheMostAbsent().toLowerCase().contains(newVal.toLowerCase())
            );
        });

    }

    private void loadDataToTable(ObservableList<Student> list) {
        TreeItem<Student> root;
        root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
        tableView.getSelectionModel().select(0);
    }

    @FXML
    private void comboBoxOnAction() {
        actualClassIndex = selectClass.getSelectionModel().getSelectedIndex();
        loadDataToTable(FXCollections.observableArrayList(selectClass.getSelectionModel().getSelectedItem().getStudentsList()));
    }

    @FXML
    private void exitButton(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void minimizeButton(ActionEvent event) {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setIconified(true);
    }

    private void fadeIn(Node node) {
        FadeTransition exitFade = new FadeTransition(Duration.seconds(2), node);
        exitFade.setFromValue(0);
        exitFade.setToValue(1);
        exitFade.play();
    }

    @FXML
    private void selectStudent() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            lastSelectedStudentIndex=tableView.getSelectionModel().getSelectedIndex();
            Person selectedStudentCalendar = tableView.getSelectionModel().getSelectedItem().getValue();
            loadCalendar(selectedStudentCalendar);
        }
    }

    private void loadCalendar(Person student) throws IOException {
        calendarController = new CalendarViewController();
        calendarController.setStudent(student);
        calendarController.setTeacherController(this);
        loader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/CalendarView.fxml"));
        loader.setController(calendarController);
        paneCalendar.getChildren().clear();
        paneCalendar.getChildren().add(loader.load());
    }


    public void logOut() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/LoginView.fxml"));
        new WindowOpener(loader);
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();
    }

    private void alertMessage(Exception ex)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }

    public void setTableExternally() throws BEException {
        lastSelectedStudent = tableView.getSelectionModel().getSelectedItem().getValue();
        lastSelectedStudent.loadStudentContent();
        lastSelectedStudent.setAttendanceOfStudent();
        tableView.getColumns().clear();
        setTableView();
        loadDataToTable(FXCollections.observableArrayList(observableClasses.get(actualClassIndex).getStudentsList()));
        tableView.getSelectionModel().select(lastSelectedStudentIndex);
    }


}
