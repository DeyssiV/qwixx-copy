  package domein;

  import dtos.SpelDTO;
  import utils.Kleur;

  import java.util.ArrayList;
  import java.util.Comparator;
  import java.util.List;

  public class DomeinController {

    //klasse-attributen
    private final SpelerRepository spelerRepository;
    private Spel spel;
    private boolean alternatiefModus = false;
    private List<String> gekozenNamen = new ArrayList<>();
    public static final int MAX_MISLUKTE_WORPEN = 2;
    public static final int MAX_DOORKRUISTE_RIJEN = 2;


    /**
     * Maakt een nieuwe DomeinController aan en initialiseert de SpelerRepository.
     */
    //constructor
    public DomeinController() {
      spelerRepository = new SpelerRepository();
    }


    //TODO
    //public SpelDTO geefSpelDTO() {
      //return new SpelDTO(spel.getSpelers(), spel.getDobbelstenen());
    //}

    /*public SpelerDTO geefSpelerDTO() {
      return new SpelerDTO(speler.getGebruikersnaam(),speler.getGeboortejaar(),speler.geefTotaleScore(),speler.getAantalGespeeld(),speler.getAantalGewonnen());
    }*/

    /**
     * Geeft een lijst met gebruikersnamen van alle geregistreerde spelers in het systeem.
     *
     * @return een lijst van strings met de namen van beschikbare spelers.
     */
    public List<String> geefGeregistreerdeSpelers() {
      List<String> mogelijkeSpelers = new ArrayList<>();
      for (Speler speler : spelerRepository.geefSpelersLijst()) {
        mogelijkeSpelers.add(speler.getGebruikersnaam());
      }
      return mogelijkeSpelers;
    }

    /**
     * Reset de status van alle actieve spelers voor een nieuw spel.
     * Elke speler krijgt een schoon scoreblad gebaseerd op de huidige spelmodus.
     */
    public void resetSpel(){
      for (Speler speler:spel.getSpelers()){
        speler.resetSpeler(isAlternatiefModus());
      }
    }

    /**
     * Registreert een nieuwe speler in het systeem en slaat deze op in de database.
     *
     * @param gebruikersnaam de gewenste unieke naam van de speler.
     * @param geboortejaar het geboortejaar van de speler voor leeftijdscontrole.
     * @throws IllegalArgumentException als de validatie van naam of jaar faalt.
     */
    public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
      Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
      spelerRepository.voegToe(nieuweSpeler);
    }

    /**
     * Start een nieuw spel op basis van de lijst met vooraf gekozen namen.
     * De lijst met gekozen namen wordt na de start leeggemaakt.
     */
    public void startNieuwSpel() {
      List<Speler> geselecteerdeSpelers = new ArrayList<>();
      for (String naam : gekozenNamen) {
        Speler speler = spelerRepository.geefSpeler(naam);
        geselecteerdeSpelers.add(speler);
      }
      this.spel = new Spel(geselecteerdeSpelers, alternatiefModus);
      gekozenNamen.clear();
    }

    /**
     * Start een nieuwe beurt door de dobbelstenen te rollen.
     */
    public void startBeurt() {
      spel.rolDobbelstenen();
    }

    /**
     * Synchroniseert de lokale spelerslijst met de database via de repository.
     */
    public void herlaadSpelers() {
      spelerRepository.herlaadSpelers();
    }

    /**
     * Geeft de details van de huidige dobbelstenen terug als een lijst van strings.
     * Elke string bevat de kleur en de waarde (bijv. "ROOD 5").
     *
     * @return een lijst met geformatteerde dobbelsteendetails.
     */
    public List<String> geefDetailsDobbelstenen() {
      List<String> resultaat = new ArrayList<>();
      for (Dobbelsteen dobbelsteen : spel.getDobbelstenen()) {
        resultaat.add(String.format("%s %d", dobbelsteen.getKleur(), dobbelsteen.getWaarde()));
      }
      return resultaat;
    }

    /**
     * Probeert een combinatie van een gekleurde en een witte dobbelsteen aan te kruisen.
     * Als de rij hierdoor wordt voltooid, wordt deze automatisch gesloten voor alle spelers.
     *
     * @param gebruikersnaam de naam van de speler die de actie uitvoert.
     * @param keuze de index van de gekozen dobbelsteencombinatie.
     * @return {@code true} als het aankruisen volgens de spelregels is gelukt, anders {@code false}.
     */
    public boolean kiesCombinatie(String gebruikersnaam, int keuze){
      Kleur gekozenKleur = kiesKleur(keuze);

      int witteWaarde = (keuze % 2 != 0) ? spel.getWitteWaarde(1) : spel.getWitteWaarde(2);
      int kleurWaarde = spel.getGekleurdeWaarde(gekozenKleur);

      Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
      boolean gelukt = speler.kruisAan(gekozenKleur, kleurWaarde + witteWaarde);


      if (gelukt && speler.getScoreBlad().isRijGesloten(gekozenKleur)) sluitRij(gekozenKleur);
      return gelukt;
    }

    /**
     * Bepaalt de kleur die hoort bij een specifieke numerieke keuze uit de gebruikersinterface.
     *
     * @param keuze de numerieke index van de geselecteerde combinatie.
     * @return de {@link Kleur} die gekoppeld is aan de geselecteerde index.
     */
    //TODO deze methode moet in SPEL staan
    private Kleur kiesKleur(int keuze) {
      if (keuze <= 2) return Kleur.ROOD;
      if (keuze <= 4) return Kleur.GEEL;
      if (keuze <= 6) return Kleur.GROEN;
      return Kleur.BLAUW;
    }

    /**
     * Vertaalt een numerieke keuze uit de UI naar de bijbehorende kleurnaam.
     *
     * @param keuze de numerieke index van de kleurselectie.
     * @return de naam van de kleur (bijv. "ROOD", "GEEL").
     */
    public String geefKleurVanKeuze(int keuze) {
      return kiesKleur(keuze).toString();
    }

    /**
     * Probeert een specifiek getal (som) aan te kruisen in een specifieke rij.
     *
     * @param gebruikersnaam de speler die de actie uitvoert.
     * @param kleurNaam de kleur van de rij (als string).
     * @param som de waarde die aangekruist moet worden.
     * @return {@code true} als de zet geldig was en is uitgevoerd, anders {@code false}.
     */
    public boolean kiesRij(String gebruikersnaam, String kleurNaam, int som) {
      Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
      Kleur gekozenKleur = Kleur.valueOf(kleurNaam.toUpperCase());
      return speler.kruisAan(gekozenKleur, som);
    }

    /**
     * Geeft een matrix-representatie van de status van het scoreblad voor een specifieke speler.
     * Deze matrix geeft aan welke vakjes reeds zijn aangekruist.
     *
     * @param gebruikersnaam de naam van de speler waarvan de status wordt opgevraagd.
     * @return een 2D-integer array die de status van de vakjes representeert.
     */
    //TODO deze methode moet in SCOREBLAD staan
    public int[][] geefStatus(String gebruikersnaam) {
      Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
      return speler.getScoreBlad().getScoreBladMatrix();
    }

    /**
     * Genereert een lijst van alle beschikbare combinaties tussen witte en
     * gekleurde dobbelstenen voor de huidige beurt.
     *
     * @return een lijst van strings die de combinaties omschrijven voor de GUI.
     */
    public List<String> geefMogelijkeCombinaties() {
      List<String> combinaties = new ArrayList<>();
      List<Dobbelsteen> wit = new ArrayList<>();
      List<Dobbelsteen> gekleurd = new ArrayList<>();

      for (Dobbelsteen d : spel.getDobbelstenen()) {
        if (d.getKleur() == Kleur.WIT) {
          wit.add(d);
        } else {
          gekleurd.add(d);
        }
      }

      for (Dobbelsteen kleurSteen : gekleurd) {
        for (Dobbelsteen witteSteen : wit) {
          int som = kleurSteen.getWaarde() + witteSteen.getWaarde();
          combinaties.add(kleurSteen.getKleur() + ": (" + kleurSteen.getWaarde() + ") + WIT: (" + witteSteen.getWaarde() + ") -> Som: " + som);
        }
      }
      return combinaties;
    }

    /**
     * Controleert of een specifieke kleur-rij op het scoreblad van een speler is afgesloten.
     * Een gesloten rij kan niet meer gebruikt worden om nieuwe getallen aan te kruisen.
     *
     * @param gebruikersnaam de naam van de speler waarvan het scoreblad gecontroleerd wordt.
     * @param kleurNaam de naam van de kleur-rij (bijv. "ROOD", "BLAUW").
     * @return {@code true} als de rij gesloten is, anders {@code false}.
     * @throws IllegalArgumentException als de opgegeven kleurNaam niet overeenkomt met een bestaande kleur.
     */
    public boolean geefIsRijGesloten(String gebruikersnaam, String kleurNaam) {
        Speler s = spelerRepository.geefSpeler(gebruikersnaam);
        Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
        return s.getScoreBlad().isRijGesloten(k);
    }


    /**
     * Berekent de som van de twee witte dobbelstenen.
     *
     * @return de totale waarde van de witte dobbelstenen.
     */
    public int geefWitteSom() {
      List<Dobbelsteen> stenen = spel.getDobbelstenen();
      int som = 0;
      for (Dobbelsteen d : stenen) {
        if (d.getKleur() == Kleur.WIT) som += d.getWaarde();
      }
      return som;
    }

    /**
     * Berekent de huidige leeftijd van een geregistreerde speler.
     *
     * @param gebruikersnaam de naam van de speler.
     * @return de leeftijd in jaren.
     */
    public int geefLeeftijd(String gebruikersnaam) {
      return spelerRepository.geefSpeler(gebruikersnaam).berekenLeeftijd();
    }

    /**
     * Geeft een lijst met alle gebruikersnamen van de spelers in de huidige spelvolgorde.
     *
     * @return een lijst van gebruikersnamen.
     */
    public List<String> volgordeSpelersInStringLijst() {
      List<String> spelersStringLijst = new ArrayList<>();

      for (Speler speler : spel.getSpelers()) {
        spelersStringLijst.add(speler.getGebruikersnaam());
      }
      return spelersStringLijst;
    }

    /**
     * Geeft de gebruikersnaam terug van de speler die momenteel aan de beurt is.
     *
     * @return de naam van de actieve speler.
     */
    public String geefSpelerAanDeBeurt() {
      return spel.getSpelerAanDeBeurt();
    }

    /**
     * Verzet de beurt naar de volgende speler in de reeks.
     */
    public void toonVolgendeSpeler() {
      spel.zetVolgendeSpeler();
    }

    /**
     * Synchroniseert de huidige statistieken (gespeeld/gewonnen) van de
     * opgegeven spelers met de database.
     *
     * @param namen de lijst met gebruikersnamen die bijgewerkt moeten worden.
     */
    public void slaStatsOpInDB(List<String> namen) {
      for (String naam : namen) {
        Speler s = spelerRepository.geefSpeler(naam);
        spelerRepository.updateStats(s);
      }
    }

    /**
     * Verhoogt de teller van het aantal gespeelde spellen voor de opgegeven spelers.
     *
     * @param namen de lijst met gebruikersnamen.
     */
    public void verhoogAantalGespeeldVoorSpelers(List<String> namen) {
      for (String naam : namen) {
        spelerRepository.geefSpeler(naam).verhoogAantalGespeeld();
      }
    }

    /**
     * Bepaalt wie de hoogste score heeft en verhoogt het aantal gewonnen
     * spellen voor deze speler(s) in het geheugen.
     */
    public void verhoogAantalGewonnenVoorWinnaar() {
      Speler winnaar = spelerRepository.geefSpelersLijst()
              .stream()
              .filter(s -> volgordeSpelersInStringLijst().contains(s.getGebruikersnaam())) // enkel huidige spelers
              .max(Comparator.comparingInt(Speler::geefTotaleScore))
              .orElse(null);

      if (winnaar != null) {
        winnaar.verhoogAantalGewonnen();
      }
    }

    /**
     * Geeft het totaal aantal spellen terug dat een specifieke speler ooit heeft gespeeld.
     *
     * @param gebruikersnaam de naam van de speler.
     * @return het totaal aantal gespeelde spellen.
     */
    public int geefAantalGespeeld(String gebruikersnaam) {
      return spelerRepository.geefSpeler(gebruikersnaam).getAantalGespeeld();
    }

    /**
     * Geeft het totaal aantal gewonnen spellen terug van een specifieke speler.
     *
     * @param gebruikersnaam de naam van de speler.
     * @return het totaal aantal gewonnen spellen.
     */
    public int geefAantalGewonnen(String gebruikersnaam) {
      return spelerRepository.geefSpeler(gebruikersnaam).getAantalGewonnen();
    }

    /**
     * Stelt in of het spel in de alternatieve modus (met een ander scoreblad)
     * gespeeld moet worden.
     *
     * @param alternatief {@code true} voor de alternatieve modus, {@code false} voor de standaardmodus.
     */
    public void setAlternatiefModus(boolean alternatief) {
      this.alternatiefModus = alternatief;
    }

    /**
     * Controleert of de alternatieve spelmodus momenteel actief is.
     *
     * @return {@code true} als alternatieve modus aan staat, anders {@code false}.
     */
    public boolean isAlternatiefModus() {
      return alternatiefModus;
    }

    /**
     * Voegt een speler toe aan de selectielijst voor het komende spel.
     *
     * @param naam de gebruikersnaam van de speler.
     */
    public void voegSpelerToeAanSpel(String naam) {
      gekozenNamen.add(naam);
    }

    /**
     * Verwijdert een speler uit de huidige selectielijst voor het komende spel.
     *
     * @param naam de gebruikersnaam van de te verwijderen speler.
     */
    public void verwijderSpelerUitSpel(String naam) {
      gekozenNamen.remove(naam);
    }

    /**
     * Maakt de lijst met geselecteerde spelers volledig leeg.
     */
    public void resetGekozenSpelers() {
      gekozenNamen.clear();
    }

    /**
     * Registreert een mislukte worp voor de opgegeven speler.
     *
     * @param gebruikersnaam de naam van de speler die de worp heeft mislukt.
     */
    public void voegMislukteWorpToe(String gebruikersnaam){
      Speler s = spelerRepository.geefSpeler(gebruikersnaam);
      s.verhoogMislukteWorpen();
    }

    /**
     * Controleert of de speler het maximaal aantal mislukte worpen ({@value #MAX_MISLUKTE_WORPEN})
     * heeft bereikt, wat leidt tot het einde van het spel.
     *
     * @param gebruikersnaam de naam van de te controleren speler.
     * @return {@code true} als de speler te veel mislukte worpen heeft, anders {@code false}.
     */
    public boolean isEindeSpelDoorMislukteWorpen(String gebruikersnaam){
      Speler s = spelerRepository.geefSpeler(gebruikersnaam);
      return s.getMislukteWorpen() >= MAX_MISLUKTE_WORPEN;
    }

    /**
     * Controleert of het spel beëindigd moet worden omdat het maximaal aantal
     * rijen ({@value #MAX_DOORKRUISTE_RIJEN}) is doorkruist.
     *
     * @return {@code true} als het spel is afgelopen, anders {@code false}.
     */
    public boolean isEindeSpelDoorRijDoorkruising(){
      return spel.getAantalDoorkruisteRijen() >= MAX_DOORKRUISTE_RIJEN;
    }

    /**
     * Geeft de huidige totaalscore van een speler terug.
     *
     * @param gebruikersnaam de naam van de speler.
     * @return de berekende totaalscore.
     */
    public int geefTotaleScore(String gebruikersnaam){
      Speler s = spelerRepository.geefSpeler(gebruikersnaam);
      return s.geefTotaleScore();
    }

    /**
     * Controleert of een rij gesloten mag worden op basis van de standaard spelregels.
     *
     * @param gebruikersnaam de naam van de speler.
     * @param kleurNaam de kleur van de te controleren rij.
     * @return {@code true} als aan de sluitvoorwaarden is voldaan.
     */
    public boolean rijSluitenToegestaan(String gebruikersnaam, String kleurNaam) {
      Speler s = spelerRepository.geefSpeler(gebruikersnaam);
      Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
      return s.getScoreBlad().rijMagGeslotenWorden(k);
    }

    /**
     * Controleert of een speler de "lock"-knop mag gebruiken om een rij te sluiten.
     * Dit is toegestaan als de rij nog niet gesloten is en er minstens 5 kruisjes staan.
     *
     * @param gebruikersnaam de naam van de speler.
     * @param kleurNaam de kleur van de rij.
     * @return {@code true} als de actie is toegestaan, anders {@code false}.
     */
    public boolean magRijSluitenViaLockKnop(String gebruikersnaam, String kleurNaam) {
      Speler s = spelerRepository.geefSpeler(gebruikersnaam);
      Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
      return !s.getScoreBlad().isRijGesloten(k)
              && s.getScoreBlad().getAantalKruisjes(k) >= 5;
    }

    /**
     * Sluit een specifieke kleur-rij voor ALLE spelers en verwijdert de
     * bijbehorende dobbelsteen uit het spel.
     *
     * @param kleur de kleur van de rij die definitief gesloten wordt.
     */
    public void sluitRij(Kleur kleur) {
      for (Speler s : spel.getSpelers()) {
        for (Rij rij : s.getScoreBlad().getRijen()) {
          if (rij.getKleur() == kleur) {
            rij.setGesloten(true);
          }
        }
      }

      spel.verhoogAantalDoorkruisteRijen();

      Dobbelsteen teVerwijderenDS = null;

      for (Dobbelsteen d : spel.getDobbelstenen()) {
        if (d.getKleur() == kleur) {
          teVerwijderenDS = d;
          break;
        }
      }

      if (teVerwijderenDS != null) {
        spel.getDobbelstenen().remove(teVerwijderenDS);
      }
    }

    /**
     * Geeft het aantal mislukte worpen van een specifieke speler in het huidige spel.
     *
     * @param gebruikersnaam de naam van de speler.
     * @return het aantal mislukte worpen (tussen 0 en {@value #MAX_MISLUKTE_WORPEN}).
     */
    public int geefAantalMislukteWorpen(String gebruikersnaam) {
      Speler s = spelerRepository.geefSpeler(gebruikersnaam);
      return s.getMislukteWorpen();
    }

    /**
     * Geeft de volledige matrix van waarden op het scoreblad van een specifieke speler.
     * Wordt door de GUI gebruikt om de juiste getallen in de vakjes te tekenen.
     *
     * @param gebruikersnaam de naam van de speler.
     * @return een 2D-integer array van 4 bij 12 met de getalwaarden.
     */
    //TODO deze methode moet in SCOREBLAD
    public int[][] geefVakWaarden(String gebruikersnaam) {
      Speler speler = spelerRepository.geefSpeler(gebruikersnaam);

      int[][] waarden = new int[4][12];

      for (int r = 0; r < speler.getScoreBlad().getRijen().size(); r++) {
        List<Vakje> vakjes = speler.getScoreBlad().getRijen().get(r).getVakjes();

        for (int v = 0; v < vakjes.size(); v++) {
          waarden[r][v] = vakjes.get(v).getWaarde();
        }
      }

      return waarden;
    }

    /**
     * Bepaalt de winnaar(s) van het spel op basis van de hoogste totaalscore.
     * In het geval van een gelijkspel bevat de lijst meerdere namen.
     *
     * @return een lijst met de gebruikersnaam/namen van de winnaar(s).
     */
    public List<String> geefWinnaars() {
      int hoogsteScore = spel.getSpelers().stream()
              .mapToInt(Speler::geefTotaleScore)
              .max()
              .orElse(0);

      return spel.getSpelers().stream()
              .filter(s -> s.geefTotaleScore() == hoogsteScore)
              .map(Speler::getGebruikersnaam)
              .toList();
    }

    /**
     * Geeft het aantal reeds aangekruiste vakjes in een specifieke rij.
     *
     * @param gebruikersnaam de naam van de speler.
     * @param kleurNaam de kleur van de rij.
     * @return het aantal kruisjes.
     */
    public int geefAantalKruisjesInRij(String gebruikersnaam, String kleurNaam) {
      Speler s = spelerRepository.geefSpeler(gebruikersnaam);
      Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
      return s.getScoreBlad().getAantalKruisjes(k);
    }

    /**
     * Geeft de lijst met dobbelsteen-objecten terug die momenteel in het spel zijn.
     *
     * @return een lijst van {@link Dobbelsteen} objecten.
     */
    public List<Dobbelsteen> geefDobbelstenen() {
      return spel.getDobbelstenen();
    }
  }
