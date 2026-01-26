package startupGui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class QwixxApplication extends Application {
  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new Pane(), 320, 240);
    stage.setTitle("Hello World!");
    stage.setScene(scene);
    stage.show();
  }
}
