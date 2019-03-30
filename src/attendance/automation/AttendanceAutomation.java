/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation;

import attendance.automation.dal.DALException;
import attendance.automation.gui.model.AAModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 *
 * @author Revy
 */
public class AttendanceAutomation extends Application {


    @Override
    public void start(Stage stage) throws Exception {


   //     FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/view/LoginView.fxml"));
  //      WindowOpener opener = new WindowOpener(loader);


////
        stage.initStyle(StageStyle.UNDECORATED);
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/view/LoginView.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root);
        stage.setScene(scene);
        */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/view/LoginView.fxml"));
        WindowOpener opener = new WindowOpener(loader);
        stage.setMinWidth(324);
        stage.setMinHeight(400);
       // stage.minWidthProperty().bind(scene.heightProperty().multiply(1.5));
       // stage.minHeightProperty().bind(scene.widthProperty().divide(1.5));
      //  stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DALException, IOException {
        launch(args);
        AAModel aamodel = new AAModel();
        System.out.println(aamodel.checkLogin("JanToth","1234"));
    }
    
}
