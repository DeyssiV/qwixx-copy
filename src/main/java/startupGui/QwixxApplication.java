package startupGui;

import domein.DomeinController;
import domein.Speler;
import domein.SpelerRepository;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Scanner;

public class QwixxApplication extends Application {

    @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new Pane(), 320, 240);
    stage.setTitle("Hello World!");
    stage.setScene(scene);
    stage.show();
  }

  private final DomeinController dc;
  public QwixxApplication(DomeinController dc) {this.dc = dc;}

  public void start(){
    registreerSpelers();
  }

  private void registreerSpelers() {
    Scanner invoer = new Scanner(System.in);
    System.out.println("Welkom bij Qwixx! Momenteel under construction...");

    boolean stoppen = false;

    while(!stoppen){
      try{
        System.out.println("Speler registreren:");

        // Voor demo in console gebruiker maken met optie om het te beëindigen
        System.out.print("Geef gebruikersnaam in (of 'stop' als je wilt stoppen):"); //gebruikersnaam vragen en laten invullen in console met SCANNER
        String gebrNaam = invoer.next();

        if (gebrNaam.equalsIgnoreCase("stop")) // elke mogelijke combi voor stop zoals STOP of Stop of stop door equalsIgnoreCase
          stoppen = true;

        System.out.print("Geef geboortejaar in:"); //geboortejaar vragen en laten invullen in console met SCANNER INDIEN STOP NIET INGEVULD IS
        int gebJaar = invoer.nextInt();

        //Bij geen enkele fout worden de waarden voor speler doorverwezen naar de DomeinController om speler aan te maken.
        dc.registreerSpeler(gebrNaam, gebJaar);
        System.out.println("Speler is geregisteerd in het systeem!");

      }catch (Exception e){
        //Indien de Speler klasse een fout geeft bij incorrecte waarden of te jonge leeftijd
        System.out.println("ERROR: " + e.getMessage());
        invoer.nextLine(); //buffer leegmaken
      }
    }
  }


}
