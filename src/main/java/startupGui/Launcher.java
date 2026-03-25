package startupGui;

import domein.DomeinController;
import javafx.application.Application;

public class Launcher {
  public static void main(String[] args) {
    QwixxApplication.dc = new DomeinController();
    Application.launch(QwixxApplication.class, args);
  }
}