package startupCui;

import domein.DomeinController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QwixxCui {
    private final DomeinController dc;
    private final Scanner invoer = new Scanner(System.in);
    List<String> gekozenSpelers;

    private static final String[] KLEUREN = {"ROOD", "GEEL", "GROEN", "BLAUW"};

    public QwixxCui(DomeinController dc) {
        this.dc = dc;
    }

    //hoofdmenu en setup

    public void start() {
        //hoofdmenu tonen
        int keuze;
        do {
            toonMenuTekst();
            keuze = leesInvoer("Maak je keuze: ", 1, 3);
            switch (keuze) {
                case 1 -> registreerSpelers();
                case 2 -> opstartQwixx();
                case 3 -> afsluiten();
            }
        } while (keuze != 3);
    }

    //achterliggende info
    private void toonMenuTekst() {
        System.out.printf("%n--- Hoofdmenu ---%n");
        System.out.println("1. Registreer nieuwe speler");
        System.out.println("2. Start nieuw spel");
        System.out.println("3. Afsluiten");
    }

    private int leesInvoer(String keuze, int min, int max) {
        int getal = -1;
        boolean geldig = false;

        while (!geldig) {
            System.out.print(keuze);
            if (invoer.hasNextInt()) {
                getal = invoer.nextInt();
                invoer.nextLine();

                if (getal >= min && getal <= max) {
                    geldig = true;
                } else {
                    System.out.printf("Kies een getal tussen %d en %d.%n", min, max);
                }
            } else {
                System.out.println("Fout: Voer een geldig getal in.");
                invoer.nextLine();
            }
        }
        return getal;
    }

    //spel info
    public void startSpel() {
        System.out.println("\nJe spelers zijn gekozen: " + gekozenSpelers);
        gekozenSpelers.forEach(dc::voegSpelerToeAanSpel);
        dc.startNieuwSpel();

        List<String> speelVolgorde = dc.geefSpelersInVolgorde();
        System.out.println("De speelvolgorde is: " + speelVolgorde);

        boolean isEindeSpel = false;
        while (!isEindeSpel) {
            for (String actieveSpeler : gekozenSpelers) {
                isEindeSpel = speelBeurt(actieveSpeler);
                if (isEindeSpel) break;
            }
        }
        toonResultaat();
    }

    private boolean speelBeurt(String actieveSpeler) {
        System.out.printf("%n--- %s is aan de beurt ---%n", actieveSpeler);

        System.out.print(actieveSpeler + " typ ENTER om te dobbelen: ");
        invoer.nextLine();

        dc.startBeurt();
        toonWorp();

        rondeAndereSpelers(actieveSpeler, dc.geefWitteSom());
        rondeActieveSpeler(actieveSpeler, dc.geefWitteSom());

        return dc.isEindeSpelDoorMislukteWorpen(actieveSpeler) || dc.isEindeSpelDoorRijDoorkruising();
    }

    private void toonWorp() {
        System.out.println("\n--- Resultaat van worp ---");
        List<String> geroldeDobbelstenen = dc.geefDetailsDobbelstenen();
        for (String dobbelsteen : geroldeDobbelstenen) {
            System.out.println("[" + dobbelsteen + "]");
        }
    }

    private void verwerkRijKeuze(String naam, int som) {
        boolean gelukt = false;
        while (!gelukt) {
            System.out.print("In welke rij? (ROOD, GEEL, GROEN, BLAUW): ");
            String keuze = invoer.next().toUpperCase();

            try {
                gelukt = dc.kiesRij(naam, keuze, som);

                if (gelukt) {
                    System.out.println("Kruisje gezet!");
                    rijSluiten(naam, keuze, som);
                } else {
                    System.out.println("Hier mag je geen kruis zetten! (Check de volgorde of de witte som).");
                    return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ongeldige kleur. Het moet een van deze kleuren zijn! ROOD, GEEL, GROEN, BLAUW.");
            }
        }
    }

    private void rijSluiten(String naam, String kleur, int som) {
        /*if (dc.magRijSluiten(naam, kleur, som)) {
            System.out.print("Wil je de rij " + kleur + " SLUITEN voor iedereen? J/N: ");
            if (invoer.next().equalsIgnoreCase("j")) {
                dc.sluitRij(utils.Kleur.valueOf(kleur));
            }
        }*/

        if (dc.rijSluitenToegestaan(naam, kleur, som)) {
            System.out.print("Je mag deze rij sluiten. Wilt u dit doen? J/N: ");
            String antwoord = invoer.next();
            invoer.nextLine();

            if (antwoord.equalsIgnoreCase("j")) {
                dc.sluitRij(utils.Kleur.valueOf(kleur.toUpperCase()));
                System.out.println("De rij " + kleur + " is nu gesloten voor iedereen!");
            }
        }
    }

    /*private void toonHoofdMenu(){
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
    }*/

    private String bepaalKleurNaam(int keuze) {
        return switch (keuze) {
            case 1, 2 -> "ROOD";
            case 3, 4 -> "GEEL";
            case 5, 6 -> "GROEN";
            case 7, 8 -> "BLAUW";
            default -> "";
        };
    }

    private void registreerSpelers() {

            try {

                System.out.print("\n--- Speler registreren: ---\n");
                System.out.print("Geef gebruikersnaam in: ");
                String gebrNaam = invoer.next();
                System.out.print("Geef geboortejaar in: ");
                int gebJaar = invoer.nextInt();
                invoer.nextLine();

                dc.registreerSpeler(gebrNaam, gebJaar);
                System.out.println("\n--> Speler is geregistreerd in het systeem!\n");

            } catch (Exception e) { //TODO exception bij al een bestaande speler met dezelfde gebruikersnaam
                System.out.println("ERROR: " + e.getMessage());
                invoer.nextLine();
            }
    }

    private void opstartQwixx() {
        List<String> beschikbareNamen = dc.geefGeregistreerdeSpelers();
        gekozenSpelers = new ArrayList<>();

        if (beschikbareNamen.size()<4){
            System.out.println("ERROR: Te weining spelers geristreerd");
            return;
        }
        System.out.println("\n--- Beschikbare spelers ---\n");
        for (String naam : beschikbareNamen) {
            int leeftijd = dc.geefLeeftijd(naam);
            System.out.println(beschikbareNamen.indexOf(naam) + " " + naam + " ( " + leeftijd + " jaar )");
        }
        for (int i = 1; i <= 5; i++) {
            try {
                int keuzeSpeler = leesInvoer("Kies speler " + i + " : ", 0, beschikbareNamen.size() - 1);
                String gekozenNaam = beschikbareNamen.get(keuzeSpeler);

                if (gekozenSpelers.contains(gekozenNaam)) {
                    throw new exceptions.GebruikersnaamInGebruikException("Speler '" + gekozenNaam + "' is al toegevoegd aan dit spel!");
                }

                gekozenSpelers.add(gekozenNaam);
                System.out.println("--> " + gekozenNaam + " toegevoegd.");

                if (i >= 2 && i < 5) {
                    System.out.print("Wil je nog een speler toevoegen? J(a)/N(ee): ");
                    if (invoer.next().equalsIgnoreCase("n")) {
                        invoer.nextLine();
                        startSpel();
                        return;
                    }
                    invoer.nextLine();
                }

            } catch (exceptions.GebruikersnaamInGebruikException e) {
                System.out.println("FOUT: " + e.getMessage());
                i--;
            }
        }
    }

  /*  public void startSpel(){
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
    }*/



    /*public void speelBeurt() {
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

                rondeAndereSpelers(actieveSpeler, dc.geefWitteSom());
                rondeActieveSpeler(actieveSpeler, dc.geefWitteSom());
            }
            if(dc.isEindeSpelDoorMislukteWorpen(dc.getSpelerAanDeBeurt()) || dc.isEindeSpelDoorRijDoorkruising()){
                isEindeSpel=true;
            }

        }
        toonResultaat();
    }*/

    private void toonResultaat() {
        System.out.println("\n--- Einde van het spel ---");

        String winnaar = "";
        int hoogsteScore = -100;

        for (String naam : gekozenSpelers) {
            int score = dc.geefTotaleScore(naam);
            System.out.printf("%-15s : %d punten%n", naam, score);

            if (score > hoogsteScore) {
                hoogsteScore = score;
                winnaar = naam;
            }
        }
        System.out.println("De winnaar is: " + winnaar);
        afsluiten();
    }

    public void toonMogelijkCombinaties(){
        List<String> combinaties = dc.geefMogelijkeCombinaties();


        System.out.println("\n--- Mogelijke combinaties ---");
        System.out.println("0. Mislukte worp == strafpunt");

        for (int i = 0; i < combinaties.size(); i++) {
            System.out.printf("%d. %s%n", (i+1), combinaties.get(i));
        }
    }

    /*private void verwerkRijKeuze(String naam, int som) {
        boolean gelukt = false;
        while (!gelukt) {
            System.out.print("In welke rij? (rood, geel, groen, blauw): ");
            String kleur = invoer.next().toUpperCase();

            gelukt = dc.kiesRij(naam, kleur, som);

            if (gelukt) {
                System.out.println("Kruisje gezet!");

                if (dc.rijSluitenToegestaan(naam, kleur, som)) {
                    System.out.print("Je hebt minstens 5 kruisjes en het laatste getal aangekruist. " +
                            "Wil je de rij " + kleur + " SLUITEN voor iedereen? J(a)/N(ee): ");

                    if (invoer.next().equalsIgnoreCase("j")) {
                        dc.sluitRij(utils.Kleur.valueOf(kleur));
                        System.out.println("De rij is nu gesloten!");
                    }
                }

            }else{
                System.out.println("Hier mag je geen kruis zetten! Probeer het opnieuw.");
            }
        }
    }*/

    private void rondeActieveSpeler(String naam, int witteSom) {
        System.out.println("\n--- Optie van de witte dobbelstenen ---"); // de witte-combinatie
        toonScoreBord(naam);
        System.out.print(naam + ", wil je de witte som " + witteSom + " aankruisen? J(a)/N(ee): ");
        if (invoer.next().equalsIgnoreCase("j")) {
            verwerkRijKeuze(naam, witteSom);
            System.out.println("Kruisje gezet!");
        } else {
        System.out.println(naam + " slaat de witte som over");
    }


        toonScoreBord(naam);

        System.out.println("\n--- Optie van de gekleurde dobbelstenen (VERPLICHT anders strafpunt van -5) ---"); // de kleur-combinaties
        toonMogelijkCombinaties();

        boolean succes = false;
        while (!succes) {
            int keuze = leesInvoer(naam + ", maak je keuze (1-8) of 0 voor een mislukte worp: ", 0, 8);

            if (keuze == 0) {
                dc.voegMislukteWorpToe(naam);
                System.out.println("Mislukte worp bijgehouden (-5 punten).");
                succes = true;
            } else{
                boolean gelukt = dc.kiesCombinatie(naam, keuze);
                if (gelukt) {
                    System.out.println("Kruisje gezet!");
                    String kleurNaam = bepaalKleurNaam(keuze);
                    rijSluiten(naam, kleurNaam, 0);
                    succes = true;
                } else {
                    System.out.println("Deze combinatie is ongeldig. Kies een andere optie.");
                }
            }
        }
    }

    private void rondeAndereSpelers(String actieveSpeler, int witteSom) {
        System.out.println("\n--- OPTIE: wit (ANDERE SPELERS) ---");

        for (String naam : gekozenSpelers) {
            if (!naam.equals(actieveSpeler)) {
                System.out.println("\nBeurt voor andere speler: " + naam);
                toonScoreBord(naam);

                System.out.print(naam + ", wil je de witte som " + witteSom + " aankruisen? J(a)/N(ee): ");
                if (invoer.next().equalsIgnoreCase("j")) {
                    verwerkRijKeuze(naam, witteSom);
                    System.out.println("Kruisje gezet!");
                } else {
                    System.out.println(naam + " slaat beurt over");
                }
            }
        }
    }

    private void toonScoreBord(String naam) {
        int[][] matrix = dc.geefStatus(naam);
        String[] kleuren = {"ROOD ", "GEEL ", "GROEN", "BLAUW"};
        int mislukteWorpen = dc.geefAantalMislukteWorpen(naam);

        System.out.println("\nScoreblad van " + naam + ":");
        System.out.println("------------------------------------------------------------------------");

        for (int r = 0; r < 4; r++) {

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

        System.out.print("Mislukte worpen (-5 ptn): ");
        for (int i = 1; i <= 4; i++) {
            if (i <= mislukteWorpen) {
                System.out.print("[ X ] ");
            } else {
                System.out.print("[   ] ");
            }
        }

        System.out.println("\n------------------------------------------------------------------------");
    }

    public void afsluiten() {
        System.out.println("Dit was Qwixx! Fijne dag verder!");
    }
}