package startupCui;

import domein.DomeinController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QwixxCui {
    private final DomeinController dc;
    Scanner invoer = new Scanner(System.in);
    List<String> gekozenNamen;

    public QwixxCui(DomeinController dc) {
        this.dc = dc;
    }

    public void start() {
        List<String> bestaandeSpelers = dc.geefGeregistreerdeSpelers();
        if (bestaandeSpelers.size() < 2) {
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
        while (!stoppen) {
            try {
                System.out.println("Speler registreren:");
                System.out.print("Geef gebruikersnaam in (of 'stop' als je wilt stoppen): ");
                String gebrNaam = invoer.next();
                if (gebrNaam.equalsIgnoreCase("stop")) {
                    stoppen = true;
                } else {
                    System.out.print("Geef geboortejaar in: ");
                    int gebJaar = invoer.nextInt();
                    dc.registreerSpeler(gebrNaam, gebJaar);
                    System.out.println("Speler is geregistreerd in het systeem!");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                invoer.nextLine();
            }
        }
    }

    private void startQwixx() {
        boolean stoppen = false;
        System.out.print("Wil je Qwixx spelen? Y(es)/N(o): ");
        String antwoord = invoer.next();
        while (!stoppen) {
            if (antwoord.equalsIgnoreCase("y")) {
                List<String> beschikbareNamen = dc.geefGeregistreerdeSpelers();
                gekozenNamen = new ArrayList<>();
                System.out.println("Beschikbare spelers:");
                for (String naam : beschikbareNamen) {
                    System.out.println(beschikbareNamen.indexOf(naam) + " " + naam);
                }
                for (int i = 1; i <= beschikbareNamen.size(); i++) {
                    if (i >= 2) {
                        System.out.print("Wil je nog spelers toevoegen? Y(es)/N(o): ");
                        String keuzeYN = invoer.next();
                        if (keuzeYN.equalsIgnoreCase("n")) break;
                    }
                    if (i >= 5) break;
                    System.out.print("Kies jouw " + i + "e speler: ");
                    int keuzeSpeler = invoer.nextInt();
                    gekozenNamen.add(beschikbareNamen.get(keuzeSpeler));
                }
                System.out.println("Ok, spelers zijn gekozen!");
                System.out.println("Dit zijn je spelers: " + gekozenNamen);
                stoppen = true;
            } else {
                System.out.println("Ok, fijne dag nog!");
                stoppen = true;
            }
        }
    }

    public void speelBeurt() {
        for (String gekozenNaam : gekozenNamen) {
            System.out.println("Speler: " + gekozenNaam + " is aan de beurt.");
            dc.startBeurt();
        }
    }
}