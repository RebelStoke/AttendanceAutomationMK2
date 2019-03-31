/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.WindowOpener;
import attendance.automation.be.Class;
import attendance.automation.be.Person;
import attendance.automation.be.Student;
import attendance.automation.be.Teacher;
import attendance.automation.gui.model.AAModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

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
 // private AAManager manager;
  private AAModel aamodel;
  private List<Class> listOfClasses;
  private ObservableList<Class> observableClasses;
  @FXML
  private JFXButton btnExit;
  @FXML
  private JFXButton btnMinimize;
  @FXML
  private ImageView pic;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    try {
      aamodel = AAModel.getInstance();

      te = (Teacher) aamodel.getPerson();
      welcomeLabel.setText("Welcome, " + te.getName() + "!");
      listOfClasses = te.getClassesList();
      observableClasses = FXCollections.observableArrayList(listOfClasses);
      selectClass.setItems(observableClasses);
      selectClass.setPromptText(observableClasses.get(0).getName());
      setTableView();
      loadDataToTable(
          FXCollections.observableArrayList(observableClasses.get(0).getStudentsList()));

      Person toCalendar = observableClasses.get(0).getStudentsList().get(0);

      loadCalendar(toCalendar);
    } catch (IOException e) {
      e.printStackTrace();
    }

    fadeIn(btnExit);
    fadeIn(btnMinimize);
    fadeIn(pic);

  }

  private void setTableView() {
    JFXTreeTableColumn<Student, String> studentName = new JFXTreeTableColumn<>("Student");
    JFXTreeTableColumn<Student, String> studentAbsence = new JFXTreeTableColumn<>("Absence");
    studentName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
    studentAbsence
        .setCellValueFactory(new TreeItemPropertyValueFactory<>("absenceOfStudent"));

    tableView.getColumns().addAll(studentName, studentAbsence);
    studentSearch.textProperty().addListener((o, oldVal, newVal) -> {
      tableView.setPredicate(student ->
          String.valueOf(student.getValue().getName()).toLowerCase().contains(newVal.toLowerCase())
              || student.getValue().getAttendanceOfStudent().contains(newVal)
      );
    });

  }



  private void loadDataToTable(ObservableList<Student> list) {
    TreeItem<Student> root;
    root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
    tableView.setRoot(root);
    tableView.setShowRoot(false);
  }

  @FXML
  private void comboBoxOnAction(ActionEvent event) {
    loadDataToTable(FXCollections
        .observableArrayList(selectClass.getSelectionModel().getSelectedItem().getStudentsList()));
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
  private void selectStudent(MouseEvent event) throws IOException {
    if(tableView.getSelectionModel().getSelectedItem()!=null) {
      Person selectedStudentCalendar = tableView.getSelectionModel().getSelectedItem().getValue();
      loadCalendar(selectedStudentCalendar);
    }
  }

  private void loadCalendar(Person student) throws IOException {
    CalendarViewController calendarController = new CalendarViewController(null, student);
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/CalendarView.fxml"));
    loader.setController(calendarController);
    Pane pane = loader.load();
    paneCalendar.getChildren().clear();
    paneCalendar.getChildren().add(pane);
  }


  public void logOut(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/LoginView.fxml"));
    WindowOpener opener = new WindowOpener(loader);
    Stage stage = (Stage) welcomeLabel.getScene().getWindow();
    stage.close();
  }

}
