package startupCui;

import domein.DomeinController;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welkom bij Qwixx! Momenteel under construction...");
    QwixxCui cui = new QwixxCui(new DomeinController());
    cui.start();
  }
}