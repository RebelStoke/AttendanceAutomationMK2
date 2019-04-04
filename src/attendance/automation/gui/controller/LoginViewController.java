/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.WindowOpener;
import attendance.automation.dal.DALException;
import attendance.automation.gui.model.AAModel;
import attendance.automation.gui.model.ModelException;
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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    private AAModel aamodel;
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
    private CheckBox rememberUsernameCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            preferences = Preferences.userNodeForPackage(LoginViewController.class);
            aamodel = AAModel.getInstance();
            setRemeberedPassword();
        } catch (ModelException e) {
            e.printStackTrace();
        }
        fadeIn(btnExit);
        fadeIn(btnLogin);
        fadeIn(pic);

    }

    @FXML
    private void closeWindow() {
        System.exit(0);
    }


    @FXML
    private void loginMethod() {
        String login = loginField.getText();
        String password = passwordField.getText();
        login(login, password);
    }

    private void setRemeberedPassword() {
        if (preferences.get("loginField", null) != null && preferences.get("passwordField", null) != null) {
            loginField.setText(preferences.get("loginField", null));
            passwordField.setText(preferences.get("passwordField", null));
            rememberUsernameCheckBox.setSelected(true);
        }
    }

    private void rememberPassword() throws BackingStoreException {
        if (rememberUsernameCheckBox.isSelected()) {
            preferences.put("loginField", loginField.getText());
            preferences.put("passwordField", passwordField.getText());
        } else preferences.clear();


    }


    private void login(String login, String password) {
        btnLogin.setVisible(false);
        btnLogin.setDisable(true);
        spinner.setVisible(true);

        Thread t = new Thread(() -> {

            try {
                if (aamodel.checkLogin(login, password)) {
                    aamodel.setUser();
                    Platform.runLater(() -> {
                        try {
                            openUserView();
                            rememberPassword();
                        } catch (IOException | BackingStoreException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        loginField.clear();
                        passwordField.clear();
                    });
                    failedPromptText();
                    spinner.setVisible(false);
                    btnLogin.setVisible(true);
                    btnLogin.setDisable(false);
                    Thread.sleep(1000);

                }
            } catch (ModelException | InterruptedException ex) {
                System.out.println("Error throw");
                Platform.runLater(this::connectionUnsuccessful);
                spinner.setVisible(false);
                btnLogin.setVisible(true);
                btnLogin.setDisable(false);
                alertMessage(ex);
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
    private void forgotPasswordButt() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/attendance/automation/gui/view/ForgotPasswordView.fxml"));
        new WindowOpener(fxmlLoader);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginStudent() {
        loginField.setText("JanToth");
        passwordField.setText("1234");
    }

    @FXML
    private void loginTeacher() {
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
    private void minimizeButton() {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setIconified(true);
    }


    private void openUserView() throws IOException {
        if (aamodel.isTeacher()) {
            teacherMainViewInitialization();
        } else {
            studentMainViewInitialization();
        }
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }

    private void connectionUnsuccessful() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Connection failed");
        errorAlert.setContentText("Check connection to the school database.");
        errorAlert.showAndWait();
    }

    private void failedPromptText() {
        loginField.setPromptText("Wrong login/password");
        loginField.setAlignment(Pos.BASELINE_LEFT);
        loginField.setStyle("-fx-prompt-text-fill: red");
    }

    public void defaultPromptText() {
        loginField.setPromptText("Login/Email");
        loginField.setAlignment(Pos.CENTER);
        loginField.setStyle("-fx-prompt-text-fill: grey;");
    }

    @FXML
    private void enterNext(KeyEvent event)
    {
           if (event.getCode() == KeyCode.ENTER)
           {
               passwordField.requestFocus();
           }
    }

    @FXML
    private void enterLogin(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            String login = loginField.getText();
            String password = passwordField.getText();
            login(login, password);
        }
    }

    private void alertMessage(Exception ex)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }
}
