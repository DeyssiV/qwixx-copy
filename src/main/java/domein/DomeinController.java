package domein;

import persistentie.SpelerMapper;

import java.util.ArrayList;
import java.util.List;

public class DomeinController {
  private final SpelerRepository spelerRepository;
  private Speler speler;
  private Spel spel;
  private SpelerMapper sm;

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
      Speler speler = sm.geefSpeler(gebruikersNaam);
      spelers.add(speler);
    }
  }

  public void startBeurt(){

  }

  public void kiesRij(Rij rij){

  }

  public void kiesCombinatie(){

  }


}
