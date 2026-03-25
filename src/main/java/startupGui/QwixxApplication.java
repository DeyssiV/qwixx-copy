package startupGui;

import domein.DomeinController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class QwixxApplication extends Application {

  public static DomeinController dc;

  public QwixxApplication() {}

  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new Pane(), 320, 240);
    stage.setTitle("Qwixx");
    stage.setScene(scene);
    stage.show();
  }
}