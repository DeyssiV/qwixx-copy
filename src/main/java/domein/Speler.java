package domein;

import utils.Kleur;

import java.time.LocalDate;

public class Speler implements Comparable<Speler> {
  private final String gebruikersnaam;
  private final int geboortejaar;
  private ScoreBlad scoreBlad;
  private int mislukteWorpen;


  public Speler(String gebruikersnaam, int geboortejaar) {
        controleerGebruikersnaam(gebruikersnaam);
        controleerGeboortejaar(geboortejaar);
        this.gebruikersnaam = gebruikersnaam;
        this.geboortejaar = geboortejaar;
        this.scoreBlad = new ScoreBlad(gebruikersnaam); // Elke speler wordt eigen scoreblad aan toegewezen
  }

  public ScoreBlad getScoreBlad() {
    return scoreBlad;
  }

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }

  public int getGeboortejaar() {
    return geboortejaar;
  }

  public void setMislukteWorpen() {
    this.mislukteWorpen++;
  }

  public int getMislukteWorpen() {
    return mislukteWorpen;
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

  public int berekenLeeftijd() {
    int huidigJaar = LocalDate.now().getYear();
    return huidigJaar - this.geboortejaar;
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
    return scoreBlad.berekenTotaleScore(getMislukteWorpen());
  }


  public boolean kruisAan(Kleur kleur, int vakje){
    return scoreBlad.kruisVakje(kleur, vakje);
  }


  //Doordat we de interface Comparable implementeren moeten we de compareTo methode overriden
  //Dit doen we om later makkelijk de spelers te kunnen sorteren obv de gewonnen score en zo de winnaar makkelijk aan te duiden
  @Override
  public int compareTo(Speler andereSpeler) {
    // sortering obv geboortejaar
    if (this.geboortejaar != andereSpeler.geboortejaar) {
      return Integer.compare(andereSpeler.geboortejaar, this.geboortejaar);
    }
    // sortering obv lengte gebruikersnaam
    if (this.gebruikersnaam.length() != andereSpeler.gebruikersnaam.length()) {
      return Integer.compare(andereSpeler.gebruikersnaam.length(), this.gebruikersnaam.length());
    }
    // sortering obv alfabetische volgorde
    return this.gebruikersnaam.compareToIgnoreCase(andereSpeler.gebruikersnaam);

    //return Integer.compare(andereSpeler.getScore(), this.getScore());
  }
}
