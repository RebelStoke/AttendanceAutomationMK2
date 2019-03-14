/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.WindowOpener;
import attendance.automation.bll.AAManager;
import attendance.automation.dal.DALException;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author RebelStoke
 */
public class LoginViewController implements Initializable {

  @FXML
  private TextField loginField;
  @FXML
  private TextField passwordField;
  private AAManager manager;
  private Label loginFailed;
  private Preferences preferences;
  private WindowOpener opener;
  @FXML
  private ImageView pic;
  @FXML
  private JFXButton btnLogin;
  @FXML
  private JFXButton btnExit;
  @FXML
  private AnchorPane loginWindow;
  @FXML
  private CheckBox rememberUsernameCheckBox;
  @FXML
  private JFXButton btnMinimize;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    preferences = Preferences.userNodeForPackage(LoginViewController.class);
    rememberPassword();
    try {
      manager = AAManager.getInstance();
    } catch (IOException ex) {
      Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
    }

    fadeIn(btnExit);
    fadeIn(btnLogin);
    fadeIn(pic);

  }

  @FXML
  private void closeWindow(ActionEvent event) {
    System.exit(0);
  }


  @FXML
  private void loginMethod(ActionEvent event)
      throws DALException, IOException, BackingStoreException {
    String login = loginField.getText();
    String password = passwordField.getText();
    login(login, password);

  }

  private void rememberPassword() {

    if (preferences.get("loginField", null) != null) {
      loginField.setText(preferences.get("loginField", null));
      passwordField.setText(preferences.get("passwordField", null));
      rememberUsernameCheckBox.setSelected(true);
    }


  }

  private void login(String login, String password) throws DALException, IOException, BackingStoreException {
    if (manager.checkLogin(login, password)) {
      manager.setUser();

      if (rememberUsernameCheckBox.isSelected()) {
        preferences.put("loginField", login);
        preferences.put("passwordField", password);
      } else {
        preferences.clear();
      }

      if (!manager.isTeacher()) {
        studentMainViewInitialization();
      } else {
        teacherMainViewInitialization();
      }

      Stage stage = (Stage) loginField.getScene().getWindow();
      stage.close();
    } else {
      loginFailed.setText("Login failed!");
    }
  }

  private void studentMainViewInitialization() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("/attendance/automation/gui/view/StudentMainView.fxml"));
    new WindowOpener(fxmlLoader);
  }

  private void teacherMainViewInitialization() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("/attendance/automation/gui/view/TeacherMainView.fxml"));
    new WindowOpener(fxmlLoader);
  }

  @FXML
  private void forgotPasswordButt(ActionEvent event) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getResource("/attendance/automation/gui/view/ForgotPasswordView.fxml"));
    new WindowOpener(fxmlLoader);
    Stage stage = (Stage) loginField.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void loginStudent(ActionEvent event)
      throws DALException, IOException, BackingStoreException {
    login("JanToth", "1234");
  }

  @FXML
  private void loginTeacher(ActionEvent event)
      throws DALException, IOException, BackingStoreException {
    login("MarekStancik", "cplusplus");
  }

  private void fadeIn(Node node) {
    FadeTransition exitFade = new FadeTransition(Duration.seconds(2), node);
    exitFade.setFromValue(0);
    exitFade.setToValue(1);
    exitFade.play();
  }


  @FXML
  private void minimizeButton(ActionEvent event) {
    Stage stage = (Stage) loginField.getScene().getWindow();
    stage.setIconified(true);
  }

}
