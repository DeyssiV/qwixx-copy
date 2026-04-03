package startupGui;

import domein.DomeinController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.util.Properties;

import java.util.Locale;
import java.util.ResourceBundle;

public class QwixxApplication extends Application {

  public static DomeinController dc;

  public QwixxApplication() {}

  @Override
  public void start(Stage stage) throws Exception {
    if (dc == null) {
      dc = new DomeinController();
    }


    //
    Locale localisatie = new Locale("nl");
    Locale.setDefault(localisatie);
    ResourceBundle bundel = ResourceBundle.getBundle("localisatie", localisatie);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/startupGui/StartPagina.fxml"));

    loader.setResources(bundel);
    Scene scene = new Scene(loader.load());
    stage.setTitle("Qwixx");
    stage.setScene(scene);

    stage.setMinWidth(800);
    stage.setMinHeight(600);

    stage.show();
  }
}