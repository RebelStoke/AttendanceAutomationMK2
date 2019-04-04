package attendance.automation;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowOpener {

  private double xOffset = 0;
  private double yOffset = 0;
  public WindowOpener(FXMLLoader loader) throws IOException {
    Parent root = loader.load();

    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    Image image = new Image("\\attendance\\automation\\gui\\images\\easv-logo1.png");
    stage.getIcons().add(image);

    root.setOnMousePressed(event -> {
      xOffset = event.getSceneX();
      yOffset = event.getSceneY();
    });

    root.setOnMouseDragged(event -> {
      stage.setX(event.getScreenX() - xOffset);
      stage.setY(event.getScreenY() - yOffset);
    });
  }
}
