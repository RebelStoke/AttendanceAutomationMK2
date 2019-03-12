/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Tothko
 */
public class LoginViewController implements Initializable {
    
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    private AAManager manager;
    private double xOffset = 0;
    private double yOffset = 0;
    private Label loginFailed;
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
    
    Preferences preferences;
    @FXML
    private JFXButton btnMinimize;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void loginMethod(ActionEvent event) throws DALException, IOException, InterruptedException, BackingStoreException {
        String login = loginField.getText();
        String password = passwordField.getText();
        login(login, password);
        
    }
    
    private void rememberPassword(){
     preferences = Preferences.userNodeForPackage(LoginViewController.class);
        
        if (preferences != null) {
            
            if (preferences.get("loginField", null) != null || preferences.get("loginPassword", null) != null) {
                loginField.setText(preferences.get("loginField", null));
                passwordField.setText(preferences.get("passwordField", null));
                rememberUsernameCheckBox.setSelected(true);
            } 
            else if (preferences.get("loginField", null) == null || preferences.get("loginPassword", null) == null) {
                rememberUsernameCheckBox.setSelected(false);
            }
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
                preferences.clear();
            }
            if (!manager.isTeacher()) {
                studentMainViewInitialization();
            } else {
                teacherMainViewInitialization();
            }
            
            Stage stage2 = (Stage) loginField.getScene().getWindow();
            stage2.close();
        } else {
            loginFailed.setText("Login failed!");
        }
    }
    
    private void studentMainViewInitialization() throws IOException {
        Parent root1;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/StudentMainView.fxml"));
        root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        
        Scene scene = new Scene(root1);
        
        stage.setScene(scene);
        stage.show();
        
        root1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root1.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        
    }
    
    private void teacherMainViewInitialization() throws IOException {
        Parent root1;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/TeacherMainView.fxml"));
        root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        
        Scene scene = new Scene(root1);
        
        stage.setScene(scene);
        stage.show();
        
        root1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root1.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }
    
    @FXML
    private void loginStudent(ActionEvent event) throws DALException, IOException, BackingStoreException {
        login("JanToth", "1234");
    }
    
    @FXML
    private void loginTeacher(ActionEvent event) throws DALException, IOException, BackingStoreException {
        login("MarekStancik", "cplusplus");
    }
    
    private void fadeIn(Node node) {
        FadeTransition exitFade = new FadeTransition(Duration.seconds(2), node);
        exitFade.setFromValue(0);
        exitFade.setToValue(1);
        exitFade.play();
    }
    
    @FXML
    private void forgotPasswordButt(ActionEvent event) throws IOException {
        Parent root1;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/ForgotPasswordView.fxml"));
        root1 = (Parent) fxmlLoader.load();
        Stage stage = (Stage) loginWindow.getScene().getWindow();
        stage.close();
        
        Stage stage2 = new Stage();
        stage2.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root1);
        stage2.setScene(scene);
        stage2.show();
        
        root1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root1.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    @FXML
    private void minimizeButton(ActionEvent event) {
        Stage stage = (Stage)loginField.getScene().getWindow();
        stage.setIconified(true);
    }
    
}
