package startupCui;

import domein.DomeinController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QwixxCui {
    private final DomeinController dc;
    private final Scanner invoer = new Scanner(System.in);
    List<String> gekozenSpelers;

    public QwixxCui(DomeinController dc) {
        this.dc = dc;
    }

    //hoofdmenu en setup
    public void start() {
        int keuze;
        do {
            toonMenuTekst();
            keuze = leesInvoer("Maak je keuze: ", 1, 3);
            switch (keuze) {
                case 1 -> registreerSpelers();
                case 2 -> opstartQwixx();
                case 3 -> afsluiten();
            }
        } while (keuze != 3); // zolang de gebruiker niet voor afsluiten kiest
    }

    //tekst voor het hoofdmenu weergeven
    private void toonMenuTekst() {
        System.out.printf("%n--- Hoofdmenu ---%n");
        System.out.println("1. Registreer nieuwe speler");
        System.out.println("2. Start nieuw spel");
        System.out.println("3. Afsluiten");
    }

    //hulpmethode om invoer te controleren
    private int leesInvoer(String keuze, int min, int max) {
        while (true) {
            try{
            System.out.print(keuze);
            if (!invoer.hasNextInt()) {
                invoer.nextLine(); //buffer leegmaken
                throw new exceptions.FouteInvoerException("Dit is geen geldig getal!");
            }

            int getal = invoer.nextInt();
            invoer.nextLine(); //buffer leegmaken
                 if (getal < min || getal > max) {
                     throw new exceptions.FouteInvoerException("Dit is geen geldig getal! Je moet kiezen tussen " + min + " en " + max);
                }
                 return getal;
            } catch (exceptions.FouteInvoerException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //extra hulpmethode om de Ja of Nee van speler in te lezen
    private boolean leesJaNee(String vraag) {
        while (true) {
            try {
                System.out.print(vraag);
                String antwoord = invoer.next().toLowerCase();
                invoer.nextLine(); // buffer leegmaken

                if (!antwoord.equalsIgnoreCase("j") && !antwoord.equalsIgnoreCase("n")) {
                    throw new exceptions.FouteInvoerException("Ongeldige keuze! Typ 'J' voor ja of 'N' voor nee.");
                }

                return antwoord.equalsIgnoreCase("j"); // geeft true terug bij 'j'en false bij 'n'
            } catch (exceptions.FouteInvoerException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //spel info
    public void startSpel() {
        System.out.println("\nJe spelers zijn gekozen: " + gekozenSpelers);
        gekozenSpelers.forEach(dc::voegSpelerToeAanSpel);
        dc.startNieuwSpel();

        List<String> speelVolgorde = dc.volgordeSpelersInStringLijst();
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
                System.out.println("Ongeldige kleur. Het moet een van deze kleuren zijn! ROOD, GEEL, GROEN, BLAUW");
            }
        }
    }

    private void rijSluiten(String naam, String kleur, int som) {
        if (dc.rijSluitenToegestaan(naam, kleur, som)) {
            if (leesJaNee("Je mag deze rij sluiten. Wilt u dit doen? J(a)/N(ee): ")) {
                dc.sluitRij(utils.Kleur.valueOf(kleur.toUpperCase()));
                System.out.println("De rij " + kleur + " is nu gesloten voor iedereen!");
            }
        }
    }

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

            } catch (exceptions.GebruikersnaamInGebruikException e) { //exception catchen waarbij je speler wilt aanmaken met al dezelfde gebruikersnaam (moet UNIEK zijn)
                System.out.println(e.getMessage());
            }catch (Exception e) { //bij andere error die we niet kennen
                System.out.println(e.getMessage());
                invoer.nextLine();
            }
    }

    private void opstartQwixx() {
        try{
            List<String> beschikbareNamen = dc.geefGeregistreerdeSpelers();
            gekozenSpelers = new ArrayList<>();

            if (beschikbareNamen.size()<4) {
                throw new exceptions.TeWeinigSpelersException("Er moeten minstens 4 spelers geregistreerd zijn (nu: " + beschikbareNamen.size() + ").");
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
                        throw new exceptions.SpelerAlGekozenException("Speler " + gekozenNaam + " is al toegevoegd aan dit spel!");
                    }
                    gekozenSpelers.add(gekozenNaam);
                    System.out.println("--> " + gekozenNaam + " toegevoegd.");

                    if (i >= 2 && i < 5) {
                        if (!leesJaNee("Wil je nog een speler toevoegen? J(a)/N(ee): ")) {
                            startSpel();
                            return;
                        }
                    }

                } catch (exceptions.SpelerAlGekozenException e) {
                    System.out.println(e.getMessage());
                    i--;
                }
            }
        }catch (exceptions.TeWeinigSpelersException e) {
            System.out.println(e.getMessage());
        }
    }

    //de ronde van de speler die gedobbeld heeft (witte en gekleurde combo als optie)
    private void rondeActieveSpeler(String naam, int witteSom) {
        System.out.println("\n--- Optie van de witte dobbelstenen ---"); // de witte-combinatie
        toonScoreBord(naam);
        if (leesJaNee(naam + ", wil je de witte som " + witteSom + " aankruisen? J(a)/N(ee): ")) {
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

            if (keuze == 0) { //bij het beurt overslaan wordt er een strafpunt bijgehouden
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

    //methode om de combos te laten zien aan de dobbelende speler
    public void toonMogelijkCombinaties(){
        List<String> combinaties = dc.geefMogelijkeCombinaties();


        System.out.println("\n--- Mogelijke combinaties ---");
        System.out.println("0. Mislukte worp = strafpunt");

        //met for-lus index geven (die later als optie gekozen kan worden) en de som waarde
        for (int i = 0; i < combinaties.size(); i++) {
            System.out.printf("%d. %s%n", (i+1), combinaties.get(i));
        }
    }

    //ronde van al de andere spelers (alleen witte dobbelstenen als optie)
    private void rondeAndereSpelers(String actieveSpeler, int witteSom) {
        System.out.println("\n--- OPTIE: wit (ANDERE SPELERS) ---");

        for (String naam : gekozenSpelers) {
            if (!naam.equals(actieveSpeler)) {
                System.out.println("\nBeurt voor andere speler: " + naam);
                toonScoreBord(naam);

                if (leesJaNee(naam + ", wil je de witte som " + witteSom + " aankruisen? J(a)/N(ee): ")) {
                    verwerkRijKeuze(naam, witteSom);
                    System.out.println("Kruisje gezet!");
                } else {
                    System.out.println(naam + " slaat beurt over");
                }
            }
        }
    }

    //het scoreblad per speler laten zien om te zien wat er aangeduid kan worden
    private void toonScoreBord(String naam) {
        int[][] matrix = dc.geefStatus(naam);
        String[] kleuren = {"ROOD ", "GEEL ", "GROEN", "BLAUW"};
        int mislukteWorpen = dc.geefAantalMislukteWorpen(naam);

        System.out.println("\nScoreblad van " + naam + ":");
        System.out.println("--------------------------------------------------------------------------");

        for (int r = 0; r < 4; r++) {
            //kleuren per rij laten zien
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

        //mislukte worpen laten zien
        System.out.print("Mislukte worpen (-5 ptn): ");
        for (int i = 1; i <= 4; i++) {
            if (i <= mislukteWorpen) {
                System.out.print("[ X ] ");
            } else {
                System.out.print("[   ] ");
            }
        }

        System.out.println("\n--------------------------------------------------------------------------");
    }

    //op het einde de afsluiter tekst weergeven winnaar en aantal punten etc
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
        //TODO gelijkspel?
    }

    //Methode om het bericht op het einde van het spel te laten zien bv. Mislukte worpen (-5 ptn): [ X ] [   ] [   ] [   ]
    public void afsluiten() {
        System.out.println("Dit was Qwixx! Fijne dag verder!");
    }

}