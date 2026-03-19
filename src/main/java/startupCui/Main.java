package startupCui;

import domein.DomeinController;
import startupGui.QwixxApplication;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welkom bij Qwixx! Momenteel under construction...");
    QwixxApplication app = new QwixxApplication(new DomeinController());
    app.start();


  }


}

