package domein;

import dtos.SpelDTO;
import dtos.SpelerDTO;
import persistentie.SpelerMapper;
import utils.Kleur;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


  public class DomeinController {
    private final SpelerRepository spelerRepository;
    private Speler speler;
    private Spel spel;
    private SpelerMapper spelerMapper;




    public List<String> geefGeregistreerdeSpelers() {
      List<String> mogelijkeSpelers = new ArrayList<>();
      int huidigJaar = LocalDate.now().getYear(); // Haal het huidige jaar op

      for (Speler speler : spelerRepository.geefSpelersLijst()) {
        int leeftijd = huidigJaar - speler.getGeboortejaar();
        mogelijkeSpelers.add(speler.getGebruikersnaam());
      }

      return mogelijkeSpelers;
    }

    public DomeinController() {
      spelerRepository = new SpelerRepository();
    }

    public SpelDTO geefSpelDTO()
    {
      return new SpelDTO(spel.getSpelers(),spel.getDobbelstenen(),spel.getDobbelsteen(),spel.getScoreBlad());
    }

  public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
    Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
    spelerRepository.voegToe(nieuweSpeler);
  }

  public void startNieuwSpel(List<String> gebruikersNamen){
    List<Speler> spelers = new ArrayList<>();
    List<Speler> geselecteerdeSpelers = new ArrayList<>();

      for (String gebruikersNaam:gebruikersNamen){
        Speler speler = spelerRepository.geefSpeler(gebruikersNaam);
        geselecteerdeSpelers.add(speler);
      }
      this.spel = new Spel(geselecteerdeSpelers);
    }


    public void startBeurt() {
      spel.rolDobbelstenen();
    }

    public List<String> geefDetailsDobbelstenen() {
      List<String> resultaat = new ArrayList<>();
      for (Dobbelsteen dobbelsteen : spel.getDobbelstenen()) {
        resultaat.add(String.format("%s %d", dobbelsteen.getKleur(), dobbelsteen.getWaarde()));
      }
      return resultaat;
    }

  /*public int geefWitteSom() {
    return spel.getSomWitteDobbelstenen();
  }*/

    /*int waarde = spel.geefWaardeDobbelsteen();
    Kleur kleur = spel.geefKleurDobbelsteen();
    System.out.println(spel.dobbelstenen);
  }*/


    public String kiesCombinatie(String gebruikersnaam, int keuze){
      List<Dobbelsteen> stenen = spel.getDobbelstenen();

      int w1 = 0, w2 = 0, rood = 0, geel = 0, groen = 0, blauw = 0;

      int witTeller = 1;
      for (Dobbelsteen d : stenen) {
        if (d.getKleur() == Kleur.WIT) {
          if (witTeller == 1) { w1 = d.getWaarde(); witTeller++; }
          else w2 = d.getWaarde();
        } else if (d.getKleur() == Kleur.ROOD) rood = d.getWaarde();
        else if (d.getKleur() == Kleur.GEEL) geel = d.getWaarde();
        else if (d.getKleur() == Kleur.GROEN) groen = d.getWaarde();
        else if (d.getKleur() == Kleur.BLAUW) blauw = d.getWaarde();
      }

      Speler speler = spelerRepository.geefSpeler(gebruikersnaam);

      switch (keuze){
        case 1 -> { return speler.kruisAan(Kleur.ROOD,  rood + w1) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }
        case 2 -> { return speler.kruisAan(Kleur.ROOD,  rood + w2) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }
        case 3 -> { return speler.kruisAan(Kleur.GEEL,  geel + w1) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }
        case 4 -> { return speler.kruisAan(Kleur.GEEL,  geel + w2) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }
        case 5 -> { return speler.kruisAan(Kleur.GROEN, groen + w1) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }
        case 6 -> { return speler.kruisAan(Kleur.GROEN, groen + w2) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }
        case 7 -> { return speler.kruisAan(Kleur.BLAUW, blauw + w1) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }
        case 8 -> { return speler.kruisAan(Kleur.BLAUW, blauw + w2) ? "Kruisje gezet!" : "Hier mag je geen kruis zetten!"; }

        default -> { return "Beurt overgeslagen"; }
      }
    }

    public boolean kiesRij(String gebruikersnaam, String kleurNaam, int som) {
      Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
      Kleur gekozenKleur = Kleur.valueOf(kleurNaam.toUpperCase());
      return speler.kruisAan(gekozenKleur, som);
    }


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
      Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
      int huidigJaar = java.time.LocalDate.now().getYear();
      return huidigJaar - speler.getGeboortejaar();
    }

    public List<String> geefSpelersInVolgorde() {
      List<String> spelersInVolgorde = new ArrayList<>();

      for (Speler speler : spel.geefSpelers()) {
        spelersInVolgorde.add(speler.getGebruikersnaam());
      }
      return spelersInVolgorde;
    }
  }
