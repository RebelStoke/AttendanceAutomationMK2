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
import com.jfoenix.controls.JFXSpinner;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author RebelStoke
 */
public class LoginViewController implements Initializable {

  public JFXSpinner spinner;
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
  private void loginMethod(ActionEvent event) {
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

  private void login(String login, String password) {
    btnLogin.setVisible(false);
    btnLogin.setDisable(true);
    spinner.setVisible(true);
    Thread t = new Thread(() -> {
      try {
        if (manager.checkLogin(login, password)) {
          manager.setUser();
          Platform.runLater(() -> {
            try {
              openUserView();
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
        } else {
          loginField.clear();
          passwordField.clear();
          loginField.setPromptText("Wrong login/password");
          loginField.setAlignment(Pos.BASELINE_LEFT);
          loginField.setStyle("-fx-prompt-text-fill: red");
          Thread.sleep(1000);
          spinner.setVisible(false);
          btnLogin.setVisible(true);
          btnLogin.setDisable(false);
        }
      } catch (DALException | IOException | InterruptedException ex) {
        System.out.println("Error throw");
        Platform
            .runLater(this::connectionUnsuccessful);
      }
    });
    t.start();
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
    loginField.setText("JanToth");
    passwordField.setText("1234");
  }

  @FXML
  private void loginTeacher(ActionEvent event)
      throws DALException, IOException, BackingStoreException {
    loginField.setText("MarekStancik");
    passwordField.setText("cplusplus");
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

  public void clearPromptText(KeyEvent keyEvent) {
    loginField.setPromptText("Login/Email");
    loginField.setAlignment(Pos.CENTER);
    loginField.setStyle("-fx-prompt-text-fill: grey;");
  }

  private void openUserView() throws IOException {
    if (manager.isTeacher()) {
      teacherMainViewInitialization();
    } else {
      studentMainViewInitialization();
    }
    Stage stage = (Stage) loginField.getScene().getWindow();
    stage.close();
  }

  private void connectionUnsuccessful(){
    JOptionPane.showMessageDialog(null, "My Goodness, this is so concise");
    spinner.setOpacity(0);
    btnLogin.setOpacity(1);
  }


}
