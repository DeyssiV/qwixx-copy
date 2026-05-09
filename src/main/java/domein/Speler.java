package domein;

import utils.Kleur;

import java.time.LocalDate;

public class Speler implements Comparable<Speler> {
  private final String gebruikersnaam;
  private final int geboortejaar;
  private ScoreBlad scoreBlad;
  private int mislukteWorpen;
  private int aantalGespeeld;
  private int aantalGewonnen;

  /**
   * maakt een speler aan.
   * maakt een scoreblad aan voor de speler.
   * @param gebruikersnaam de naam van de speler.
   * @param geboortejaar het geboortejaar van de speler.
   */
  public Speler(String gebruikersnaam, int geboortejaar) {
    controleerGebruikersnaam(gebruikersnaam);
    controleerGeboortejaar(geboortejaar);
    this.gebruikersnaam = gebruikersnaam;
    this.geboortejaar = geboortejaar;
//        this.scoreBlad = new ScoreBlad(gebruikersnaam, ScoreBlad.alternatiefmodus); // Elke speler wordt eigen scoreblad aan toegewezen
  }

  public Speler(String gebruikersnaam, int geboortejaar, int aantalGespeeld, int aantalGewonnen) {
    this(gebruikersnaam, geboortejaar); // roept bestaande constructor aan
    this.aantalGespeeld = aantalGespeeld;
    this.aantalGewonnen = aantalGewonnen;
  }

  /**
   * geeft het scoreblad terug.
   * @return het scoreblad.
   */
  public ScoreBlad getScoreBlad() {
    return scoreBlad;
  }

  /**
   * geeft de gebruikersnaam terug.
   * @return de gebruikersnaam.
   */
  public String getGebruikersnaam() {
    return gebruikersnaam;
  }

  /**
   * geeft het geboortejaar terug.
   * @return het geboortejaar.
   */
  public int getGeboortejaar() {
    return geboortejaar;
  }

  /**
   * verhoogt het aantal mislukte worpen met 1.
   */
  public void verhoogMislukteWorpen() {
    this.mislukteWorpen++;
  }

  /**
   * geeft het aantal mislukte worpen.
   * @return aantal mislukte worpen.
   */
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

  //TODO: java doc
  public void resetSpeler(boolean alternatiefModus) {
    this.mislukteWorpen = 0;
    maakNieuwScoreBlad(alternatiefModus);
  }
  //TODO java doc

  public int getAantalGespeeld() { return aantalGespeeld; }

  public int getAantalGewonnen() { return aantalGewonnen; }

  public void verhoogAantalGespeeld() { this.aantalGespeeld++; }

  public void verhoogAantalGewonnen() { this.aantalGewonnen++; }

  /**
   * berekent de leeftijd van de speler.
   * @return waarde van huidigJaar - geboortejaar.
   */
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

  /**
   * geeft de score van de speler.
   * @return score van de speler.
   */
  public int geefTotaleScore(){
    return scoreBlad.berekenTotaleScore(getMislukteWorpen());
  }

  /**
   * kruist een vakje aan op het scoreblad.
   * @param kleur de kleur van het vakje.
   * @param vakje de plaats van het vakje.
   * @return boolean of het gelukt is of niet.
   */
  public boolean kruisAan(Kleur kleur, int vakje){
    return scoreBlad.kruisVakje(kleur, vakje);
  }


  //Doordat we de interface Comparable implementeren moeten we de compareTo methode overriden
  //Dit doen we om later makkelijk de spelers te kunnen sorteren obv de gewonnen score en zo de winnaar makkelijk aan te duiden

  /**
   * vergelijkt een speler met een andere speler.
   * @param andereSpeler de andere speler waarmee vergeleken wordt.
   * @return de waarde voor de sortering.
   */
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

  /**
   * Maakt een nieuw scoreblad aan voor de huidige gebruiker.
   *
   * @param alternatiefModus bepaalt of het scoreblad in alternatieve modus wordt aangemaakt
   */
  public void maakNieuwScoreBlad(boolean alternatiefModus) {
    this.scoreBlad = new ScoreBlad(gebruikersnaam, alternatiefModus);
  }
}
