package startupGui;

import domein.DomeinController;
import domein.Speler;
import domein.SpelerRepository;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
  Scanner invoer = new Scanner(System.in);
  public QwixxApplication(DomeinController dc) {this.dc = dc;}

  public void start(){
    List<String> bestaandeSpelers = dc.geefGeregistreerdeSpelers();

    if (bestaandeSpelers.size()<2) {
      System.out.println("Er zijn te weinig spelers in de database. Je moet eerst spelers registreren.");
      registreerSpelers();
      startQwixx();
    } else {
      System.out.printf("Er zijn al %d spelers bekend in het systeem.%n", bestaandeSpelers.size());
      System.out.print("Wil je nieuwe spelers registreren Y(es)/N(o): ");
      String keuze = invoer.next();

      if (keuze.equalsIgnoreCase("y")) {
        registreerSpelers();

      }
      startQwixx();
    }


  }

  private void registreerSpelers() {
    boolean stoppen = false;
    while(!stoppen){
      try{
        System.out.println("Speler registreren:");
        // Voor demo in console gebruiker maken met optie om het te beëindigen
        System.out.print("Geef gebruikersnaam in (of 'stop' als je wilt stoppen): "); //gebruikersnaam vragen en laten invullen in console met SCANNER

        String gebrNaam = invoer.next();

        if (gebrNaam.equalsIgnoreCase("stop")) {// elke mogelijke combi voor stop zoals STOP of Stop of stop door equalsIgnoreCase
          stoppen = true;
        }else{
          System.out.print("Geef geboortejaar in: "); //geboortejaar vragen en laten invullen in console met SCANNER INDIEN STOP NIET INGEVULD IS
          int gebJaar = invoer.nextInt();

          //Bij geen enkele fout worden de waarden voor speler doorverwezen naar de DomeinController om speler aan te maken.
          dc.registreerSpeler(gebrNaam, gebJaar);
          System.out.println("Speler is geregistreerd in het systeem!");
        }
      }catch (Exception e){
        //Indien de Speler klasse een fout geeft bij incorrecte waarden of te jonge leeftijd
        System.out.println("ERROR: " + e.getMessage());
        invoer.nextLine(); //buffer leegmaken
      }
    }
  }

  private void startQwixx(){
    boolean stoppen = false;
    System.out.print("Wil je Qwixx spelen? Y(es)/N(o): ");
    String antwoord = invoer.next();

    while (!stoppen) {

      if (antwoord.equalsIgnoreCase("y")) {
        List<String> beschikbareNamen = dc.geefGeregistreerdeSpelers();
        List<String> gekozenNamen = new ArrayList<>();

        /*if (beschikbareNamen.size()<2){
          //exception
        }*/

        System.out.println("Beschikbare spelers:");
        for (String naam : beschikbareNamen) {
          System.out.println(beschikbareNamen.indexOf(naam) + " " + naam);
        }
        for (int i = 1; i <= beschikbareNamen.size(); i++){
          if (i>=2){
            System.out.print("Wil je nog spelers toevoegen? Y(es)/N(o): ");
            String keuzeYN = invoer.next();
            if (keuzeYN.equalsIgnoreCase("n")) {

              break;
            }
          }
          if (i == 5){
            break;
          }
          System.out.print("Kies jouw "+ i +"e speler: ");
          int keuzeSpeler = invoer.nextInt();
          gekozenNamen.add(beschikbareNamen.get(keuzeSpeler));

        }
        System.out.println("Ok, spelers zijn gekozen!");
        System.out.println("Dit zijn je spelers: " + gekozenNamen);
        stoppen = true;
      } else if (antwoord.equalsIgnoreCase("n")) {
        System.out.println("Ok, fijne dag nog!");
        stoppen = true;
      } else {
        System.out.println("Foutive invoer, probeer opnieuw!");
      }
    }

  }


}
