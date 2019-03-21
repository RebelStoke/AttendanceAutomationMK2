/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.gui.controller;

import attendance.automation.WindowOpener;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Szymon
 */
public class ForgotPasswordViewController implements Initializable {

    @FXML
    private ImageView pic;
    @FXML
    private JFXButton btnExit;
    @FXML
    private AnchorPane resetWindow;
    @FXML
    private JFXButton sendLink;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fadeIn(btnExit);
        fadeIn(sendLink);
        fadeIn(pic);
    }    


    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) resetWindow.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void goBackToLogInWindow(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/attendance/automation/gui/view/LoginView.fxml"));
        new WindowOpener(fxmlLoader);
        Stage stage = (Stage) resetWindow.getScene().getWindow();
        stage.close();
    }
    
private void fadeIn(Node node)
    {
        FadeTransition exitFade = new FadeTransition(Duration.seconds(2), node);
        exitFade.setFromValue(0);
        exitFade.setToValue(1);
        exitFade.play();
    }
    
}
