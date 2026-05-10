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

  public List<String> geefGeregistreerdeSpelers() {
    List<String> mogelijkeSpelers = new ArrayList<>();
    for (Speler speler : spelerRepository.geefSpelersLijst()) {
      mogelijkeSpelers.add(speler.getGebruikersnaam());
    }
    return mogelijkeSpelers;
  }

  public void resetSpel(){
    for (Speler speler:spel.getSpelers()){
      speler.resetSpeler(isAlternatiefModus());
    }
  }

  public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
    Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
    spelerRepository.voegToe(nieuweSpeler);
  }

  public void startNieuwSpel() {
    List<Speler> geselecteerdeSpelers = new ArrayList<>();
    for (String naam : gekozenNamen) {
      Speler speler = spelerRepository.geefSpeler(naam);
      geselecteerdeSpelers.add(speler);
    }
    this.spel = new Spel(geselecteerdeSpelers, alternatiefModus);
    gekozenNamen.clear();
  }

  public void startBeurt() {
    spel.rolDobbelstenen();
  }

  public void herlaadSpelers() {
    spelerRepository.herlaadSpelers();
  }

  public List<String> geefDetailsDobbelstenen() {
    List<String> resultaat = new ArrayList<>();
    for (Dobbelsteen dobbelsteen : spel.getDobbelstenen()) {
      resultaat.add(String.format("%s %d", dobbelsteen.getKleur(), dobbelsteen.getWaarde()));
    }
    return resultaat;
  }

  public boolean kiesCombinatie(String gebruikersnaam, int keuze){
    Kleur gekozenKleur = kiesKleur(keuze);

    int witteWaarde = (keuze % 2 != 0) ? spel.getWitteWaarde(1) : spel.getWitteWaarde(2);
    int kleurWaarde = spel.getGekleurdeWaarde(gekozenKleur);

    Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
    boolean gelukt = speler.kruisAan(gekozenKleur, kleurWaarde + witteWaarde);


    if (gelukt && speler.getScoreBlad().isRijGesloten(gekozenKleur)) sluitRij(gekozenKleur);
    return gelukt;
  }

  //TODO deze methode moet in SPEL staan
  private Kleur kiesKleur(int keuze) {
    if (keuze <= 2) return Kleur.ROOD;
    if (keuze <= 4) return Kleur.GEEL;
    if (keuze <= 6) return Kleur.GROEN;
    return Kleur.BLAUW;
  }

  public String geefKleurVanKeuze(int keuze) {
    return kiesKleur(keuze).toString();
  }

  public boolean kiesRij(String gebruikersnaam, String kleurNaam, int som) {
    Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
    Kleur gekozenKleur = Kleur.valueOf(kleurNaam.toUpperCase());
    return speler.kruisAan(gekozenKleur, som);
  }

  //TODO deze methode moet in SCOREBLAD staan
  public int[][] geefStatus(String gebruikersnaam) {
    Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
    return speler.getScoreBlad().getScoreBladMatrix();
  }

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

  public int geefWitteSom() {
    List<Dobbelsteen> stenen = spel.getDobbelstenen();
    int som = 0;
    for (Dobbelsteen d : stenen) {
      if (d.getKleur() == Kleur.WIT) som += d.getWaarde();
    }
    return som;
  }

  public int geefLeeftijd(String gebruikersnaam) {
    return spelerRepository.geefSpeler(gebruikersnaam).berekenLeeftijd();
  }

  public List<String> volgordeSpelersInStringLijst() {
    List<String> spelersStringLijst = new ArrayList<>();

    for (Speler speler : spel.getSpelers()) {
      spelersStringLijst.add(speler.getGebruikersnaam());
    }
    return spelersStringLijst;
  }

  public String geefSpelerAanDeBeurt() {
    return spel.getSpelerAanDeBeurt();
  }

  public void toonVolgendeSpeler() {
    spel.zetVolgendeSpeler();
  }

  public void slaStatsOpInDB(List<String> namen) {
    for (String naam : namen) {
      Speler s = spelerRepository.geefSpeler(naam);
      spelerRepository.updateStats(s);
    }
  }

  public void verhoogAantalGespeeldVoorSpelers(List<String> namen) {
    for (String naam : namen) {
      spelerRepository.geefSpeler(naam).verhoogAantalGespeeld();
    }
  }

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

  public int geefAantalGespeeld(String gebruikersnaam) {
    return spelerRepository.geefSpeler(gebruikersnaam).getAantalGespeeld();
  }

  public int geefAantalGewonnen(String gebruikersnaam) {
    return spelerRepository.geefSpeler(gebruikersnaam).getAantalGewonnen();
  }

  public void setAlternatiefModus(boolean alternatief) {
    this.alternatiefModus = alternatief;
  }

  public boolean isAlternatiefModus() {
    return alternatiefModus;
  }

  public void voegSpelerToeAanSpel(String naam) {
    gekozenNamen.add(naam);
  }

  public void verwijderSpelerUitSpel(String naam) {
    gekozenNamen.remove(naam);
  }

  public void resetGekozenSpelers() {
    gekozenNamen.clear();
  }

  public void voegMislukteWorpToe(String gebruikersnaam){
    Speler s = spelerRepository.geefSpeler(gebruikersnaam);
    s.verhoogMislukteWorpen();
  }

  public boolean isEindeSpelDoorMislukteWorpen(String gebruikersnaam){
    Speler s = spelerRepository.geefSpeler(gebruikersnaam);
    return s.getMislukteWorpen() >= MAX_MISLUKTE_WORPEN;
  }

  public boolean isEindeSpelDoorRijDoorkruising(){
    return spel.getAantalDoorkruisteRijen() >= MAX_DOORKRUISTE_RIJEN;
  }

  public int geefTotaleScore(String gebruikersnaam){
    Speler s = spelerRepository.geefSpeler(gebruikersnaam);
    return s.geefTotaleScore();
  }

  public boolean rijSluitenToegestaan(String gebruikersnaam, String kleurNaam) {
    Speler s = spelerRepository.geefSpeler(gebruikersnaam);
    Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
    return s.getScoreBlad().rijMagGeslotenWorden(k);
  }

  public boolean magRijSluitenViaLockKnop(String gebruikersnaam, String kleurNaam) {
    Speler s = spelerRepository.geefSpeler(gebruikersnaam);
    Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
    return !s.getScoreBlad().isRijGesloten(k)
            && s.getScoreBlad().getAantalKruisjes(k) >= 5;
  }

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

  public int geefAantalMislukteWorpen(String gebruikersnaam) {
    Speler s = spelerRepository.geefSpeler(gebruikersnaam);
    return s.getMislukteWorpen();
  }

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

  public int geefAantalKruisjesInRij(String gebruikersnaam, String kleurNaam) {
    Speler s = spelerRepository.geefSpeler(gebruikersnaam);
    Kleur k = Kleur.valueOf(kleurNaam.toUpperCase());
    return s.getScoreBlad().getAantalKruisjes(k);
  }


  public List<Dobbelsteen> geefDobbelstenen() {
    return spel.getDobbelstenen();
  }
}
