package domein;

import java.time.LocalDate;
import java.util.Date;

public class Speler implements Comparable<Speler> {
  private final String gebruikersnaam;
  private final int geboortejaar;
  private ScoreBlad scoreBlad;

  public Speler(String gebruikersnaam, int geboortejaar) {
        //TODO: controleermethodes aanroepen
        controleerGebruikersnaam(gebruikersnaam);
        controleerGeboortejaar(geboortejaar);
        this.gebruikersnaam = gebruikersnaam;
        this.geboortejaar = geboortejaar;
        this.scoreBlad = new ScoreBlad(); // Elke speler wordt eigen scoreblad aan toegewezen
  }

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }
  public int getGeboortejaar() {
    return geboortejaar;
  }

  //TODO: controleermethodes toevoegen
  private void controleerGebruikersnaam(String gebruikersnaam)
  {
    if (gebruikersnaam == null || gebruikersnaam.isBlank())
        throw new IllegalArgumentException("Gebruikersnaam moet ingevuld zijn!");
    else if (gebruikersnaam.length() < 5)
    {
      throw new IllegalArgumentException("Gebruikersnaam moet minstens 5 karakters hebben!");
    }
  }

  private void controleerGeboortejaar(int geboortejaar)
  {
    // haal de huidige datum op
    LocalDate huidigeDatum = LocalDate.now();
    // zet het huidig jaar in een variabele
    int huidigJaar = huidigeDatum.getYear();
    // kijk of de speler ouder is dan 8 en jonger dan 100
    if ((huidigJaar - geboortejaar) < 8 || (huidigJaar - geboortejaar) > 100)
      throw new IllegalArgumentException("Alleen mensen tussen de 8 en 100 jaar mogen dit spel spelen!");
  }

  public int getScore(){
    return scoreBlad.getScore();
  }

  public void kiesRij(Rij rij){
    //TODO
  }

  //Doordat we de interface Comparable implementeren moeten we de compareTo methode overriden
  //Dit doen we om later makkelijk de spelers te kunnen sorteren obv de gewonnen score en zo de winnaar makkelijk aan te duiden
  @Override
  public int compareTo(Speler andereSpeler) {
    return Integer.compare(this.getScore(), andereSpeler.getScore());
  }
}
