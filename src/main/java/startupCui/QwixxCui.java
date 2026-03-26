package startupCui;

import domein.DomeinController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QwixxCui {
    private final DomeinController dc;
    Scanner invoer = new Scanner(System.in);
    List<String> gekozenSpelers;
    List<String> gekozenNamen;
    String scoreBlad = "-------------------------------------------------------------\n" +
            "  RED  |  2 |  3 |  4 |  5 |  6 |  7 |  8 |  9 | 10 | 11 | 12 | (L) |\n" +
            "       -------------------------------------------------------------\n" +
            "  YLW  |  2 |  3 |  4 |  5 |  6 |  7 |  8 |  9 | 10 | 11 | 12 | (L) |\n" +
            "       -------------------------------------------------------------\n" +
            "  GRN  | 12 | 11 | 10 |  9 |  8 |  7 |  6 |  5 |  4 |  3 |  2 | (L) |\n" +
            "       -------------------------------------------------------------\n" +
            "  BLU  | 12 | 11 | 10 |  9 |  8 |  7 |  6 |  5 |  4 |  3 |  2 | (L) |\n" +
            "       -------------------------------------------------------------\n" +
            "\n" +
            "  X's:    1x  2x  3x  4x  5x  6x  7x  8x  9x  10x  11x  12x   Miss: -5\n" +
            "  Pts:    1   3   6   10  15  21  28  36  45   55   66   78   [ ][ ][ ][ ]\n" +
            "\n" +
            "  SCORE: [ R ] + [ Y ] + [ G ] + [ B ] - [ Miss ] = [ TOTAL ]";

    public QwixxCui(DomeinController dc) {
        this.dc = dc;
    }

    public void start() {
        List<String> bestaandeSpelers = dc.geefGeregistreerdeSpelers();
        toonHoofdMenu();
        /*if (bestaandeSpelers.size() < 2) {
            System.out.println("Er zijn te weinig spelers geregistreerd.");
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
        }*/
    }

    private void toonHoofdMenu(){
        boolean geldig;
        do {
            System.out.printf("--- Hoofdmenu ---%n\t 1. Registreer nieuwe speler%n\t 2. Start nieuw spel%n\t 3. Afsluiten%nMaak je keuze (1-2-3): ");
            int keuze = invoer.nextInt();
            switch (keuze) {
                case 1 -> {
                    geldig = false;
                    registreerSpelers();
                }
                case 2 -> {
                    geldig = false;
                    opstartQwixx();
                }
                case 3 -> {
                    geldig = false;
                    afsluiten();
                }
                default -> {
                    System.out.println("Je moet 1-2-3 kiezen!");
                    geldig = true;
                }
            }
        }while(geldig);
    }

    private void registreerSpelers() {
            try {
                System.out.println("\nSpeler registreren:");
                System.out.print("Geef gebruikersnaam in: ");
                String gebrNaam = invoer.next();
                System.out.print("Geef geboortejaar in: ");
                int gebJaar = invoer.nextInt();
                dc.registreerSpeler(gebrNaam, gebJaar);
                System.out.println("\nSpeler is geregistreerd in het systeem!\n");
            } catch (Exception e) { //TODO exception bij al een bestaande speler met dezelfde gebruikersnaam
                System.out.println("ERROR: " + e.getMessage());
                invoer.nextLine();
            }
            toonHoofdMenu();
    }
    private void opstartQwixx() {
        List<String> beschikbareNamen = dc.geefGeregistreerdeSpelers();
        gekozenSpelers = new ArrayList<>();
        if (beschikbareNamen.size()<4){
            System.out.println("ERROR: Te weining spelers geristreerd");
            toonHoofdMenu();
        }
        //TODO leeftijd moet ook getoond worden
        System.out.println("\n--- Beschikbare spelers om te spelen ---\n");
        for (String naam : beschikbareNamen) {
            int leeftijd = dc.geefLeeftijd(naam);
            System.out.println(beschikbareNamen.indexOf(naam) + " " + naam + " ( " + leeftijd + " jaar )");
        }
        for (int i = 1; i <= beschikbareNamen.size(); i++) {
            //TODO exception catchen bij index out of bounds
            System.out.print("Kies je speler: ");
            int keuzeSpeler = invoer.nextInt();
            gekozenSpelers.add(beschikbareNamen.get(keuzeSpeler));
            if (i >= 2) {
                System.out.print("Wil je nog spelers toevoegen? J(a)/N(ee): ");
                String keuze = invoer.next();
                if(keuze.equalsIgnoreCase("n")){
                    startSpel();
                }
            }
            if (i >= 5) {
                System.out.println("Je hebt de max aantal spelers gekozen nl. 5!");
                startSpel();
            }
        }
    }

    public void startSpel(){
        System.out.println("\nJe spelers zijn gekozen: " + gekozenSpelers);
        System.out.println("\nElke speler krijgt scoreblad, er zijn ook 6 dobbelstenen beschikbaar (ROOD, GEEL, GROEN, BLAUW, WIT)");

        //dc.startNieuwSpel(gekozenSpelers);

        gekozenSpelers.forEach(dc::voegSpelerToeAanSpel);
        dc.startNieuwSpel();
        List<String> speelVolgorde = dc.geefSpelersInVolgorde();

        System.out.println("De speelvolgorde (volgens de DR) is:");
        for (int i = 0; i < speelVolgorde.size(); i++) {
            System.out.println((i + 1) + ". " + speelVolgorde.get(i));
        }
        speelBeurt();
    }



    public void speelBeurt() {
        System.out.println("\n--- Het spel is gestart ---\n");
        boolean isEindeSpel = false;

        while(!isEindeSpel){
            for (String actieveSpeler : gekozenSpelers) {
                System.out.print("\n--- " + actieveSpeler + " is aan de beurt ---\n");

                System.out.print(actieveSpeler + " typ 'gooi' en klik op enter om dobbelstenen te gooien: ");
                invoer.next();

                System.out.println("\n...dobbelstenen zijn gerold...\n");
                dc.startBeurt();

                List<String> geroldeDobbelstenen = dc.geefDetailsDobbelstenen();

                System.out.println("\n--- Resultaat van worp ---\n");
                for (String dobbelsteen : geroldeDobbelstenen) System.out.println("[" + dobbelsteen + "]");

                //HIER VOLGENDE METHODE
                rondeActieveSpeler(actieveSpeler, dc.geefWitteSom());
                rondeAndereSpelers(actieveSpeler, dc.geefWitteSom());
            }
            System.out.println("\n--- Einde van ronde ---\n");
            isEindeSpel = true;
        }
        afsluiten();
    }

    public void toonMogelijkCombinaties(){
        List<String> combinaties = dc.geefMogelijkeCombinaties();


        System.out.println("\n--- Mogelijke combinaties ---");
        System.out.println("0. Mislukte worp == overslaan");

        for (int i = 0; i < combinaties.size(); i++) {
            System.out.printf("%d. %s%n", (i+1), combinaties.get(i));
        }
    }

    private void rondeActieveSpeler(String naam, int witteSom) {

        System.out.println("\n--- OPTIE: wit ---"); // de witte-combinatie
        toonScoreBord(naam);
        System.out.print(naam + ", wil je de witte som " + witteSom + " aankruisen? (j/n): ");
        if (invoer.next().equalsIgnoreCase("j")) {
            System.out.print("In welke rij? (rood, geel, groen, blauw): ");
            String kleur = invoer.next();
            boolean succes = dc.kiesRij(naam, kleur.toUpperCase(), witteSom);
            while(!succes){
                System.out.println("Hier mag je geen kruis zetten!");
                System.out.print("In welke rij? (rood, geel, groen, blauw): ");
                kleur = invoer.next();
                succes = dc.kiesRij(naam, kleur.toUpperCase(), witteSom);
            }
            System.out.println("Kruisje gezet!");
            //toonScoreBord(naam);

        }

        System.out.println("\n--- OPTIE: gekleurde ---"); // de kleur-combinaties
        toonMogelijkCombinaties();
        //toonScoreBord(naam);
        System.out.print(naam + ", maak je keuze (1-8) of 0 voor om over te slaan: ");
        int keuze = invoer.nextInt();

        if (keuze != 1) {
            String resultaat = dc.kiesCombinatie(naam, keuze);
            System.out.println(resultaat);
        }
        //toonScoreBord(naam);
    }

    private void rondeAndereSpelers(String actieveSpeler, int witteSom) {
        System.out.println("\n--- OPTIE: wit (ANDERE SPELERS) ---");

        for (String naam : gekozenSpelers) {
            if (!naam.equals(actieveSpeler)) {
                System.out.println("\nBeurt voor andere speler: " + naam);
                toonScoreBord(naam);

                System.out.print(naam + ", wil je de witte som " + witteSom + " aankruisen? (j/n): ");
                if (invoer.next().equalsIgnoreCase("j")) {
                    boolean succes = false;
                    while (!succes) {
                        System.out.print("In welke rij? (rood, geel, groen, blauw): ");
                        String kleur = invoer.next();

                        succes = dc.kiesRij(naam, kleur, witteSom);
                        if (succes) {
                            System.out.println("Kruisje gezet!");
                            //toonScoreBord(naam);
                        } else {
                            System.out.println("Hier mag je geen kruis zetten!");
                        }
                    }
                } else {
                    System.out.println(naam + " slaat beurt over");
                }
            }
        }
    }

    private void toonScoreBord(String naam) {
        int[][] matrix = dc.geefStatus(naam);
        String[] kleuren = {"ROOD ", "GEEL ", "GROEN", "BLAUW"};

        System.out.println("\nScoreblad van " + naam + ":");
        System.out.println("------------------------------------------------------------------------");

        for (int r = 0; r < 4; r++) {
            //kleur tonen in begin
            System.out.print(kleuren[r] + " | ");

            for (int v = 0; v < 12; v++) {
                //checken of we bij laatste vakje zijn (lock)
                if (v == 11) {
                    if (matrix[r][v] == 1) {
                        //als in matrix 1 staat, dan is vakje gekruist
                        System.out.print("[ X ] (L) ");
                    } else {
                        System.out.print("[   ] (L) ");
                    }
                } else {
                    //welke getal begin (bij rood-geel eerst 2-12....)
                    int getal = (r < 2) ? (v + 2) : (12 - v);
                    if (matrix[r][v] == 1) {
                        System.out.print("[ X ] ");
                    } else {
                        //elke vakje neemt evenveel plaats zoals 1 en 11
                        System.out.printf("[%2d ] ", getal);
                    }
                }
            }
            System.out.println("|");
        }
        System.out.println("------------------------------------------------------------------------");
    }

    public void afsluiten() {
        System.out.println("Dit was Qwixx! Fijne dag verder!");
    }
}