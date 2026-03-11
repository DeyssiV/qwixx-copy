package startupCui;

import domein.DomeinController;
import startupGui.QwixxApplication;

public class Main {
  public static void main(String[] args) {
    QwixxApplication app = new QwixxApplication(new DomeinController());
    app.start();


  }


}

