package domein;

import dtos.SpelDTO;
import dtos.SpelerDTO;
import persistentie.SpelerMapper;
import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class DomeinController {
  private final SpelerRepository spelerRepository;
  private Speler speler;
  private Spel spel;
  private SpelerMapper spelerMapper;

  public List<String> geefGeregistreerdeSpelers() {
    List<String> mogelijkeSpeler = new ArrayList<>();
    for (Speler speler : spelerRepository.geefSpelersLijst()) {
      mogelijkeSpeler.add(speler.getGebruikersnaam());
    }
    return mogelijkeSpeler;
  }

  public DomeinController() {
    spelerRepository = new SpelerRepository();
  }

  public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
    Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
    spelerRepository.voegToe(nieuweSpeler);
  }

  public void startNieuwSpel(List<String> gebruikersNamen){
    List<Speler> spelers = new ArrayList<>();

    for (String gebruikersNaam:gebruikersNamen){
      Speler speler = spelerRepository.geefSpeler(gebruikersNaam);
      spelers.add(speler);
    }
    spel = new Spel(spelers);
  }

  public void startBeurt(){
    int waarde = spel.geefWaardeDobbelsteen();
    Kleur kleur = spel.geefKleurDobbelsteen();
    System.out.println(spel.getDobbelstenen());
  }

  public void kiesRij(Rij rij){ //TODO Rij mag niet meegegeven worden


  }

  public void kiesCombinatie(){

  }

  public SpelDTO geefSpelDTO()
  {
    return new SpelDTO(spel.getSpelers(),spel.getDobbelstenen(),spel.getDobbelsteen(),spel.getScoreBlad());
  }

  public SpelerDTO geefSpelerDTO()
  {
    return new SpelerDTO(speler.getGebruikersnaam(),speler.getGeboortejaar(),speler.getScore());
  }

  private List<String> gekozenNamen = new ArrayList<>();

  public void voegSpelerToeAanSpel(String naam) {
    gekozenNamen.add(naam);
  }

  public List<String> geefGekozenSpelers() {
    return gekozenNamen;
  }

  public void verwijderSpelerUitSpel(String naam) {
    gekozenNamen.remove(naam);
  }

  public void resetGekozenSpelers() {
    gekozenNamen.clear();
  }

}
