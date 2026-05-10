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
  }


  /**
   * Maakt een nieuwe speler aan met de opgegeven statistieken.
   *
   * @param gebruikersnaam  de unieke naam van de speler.
   * @param geboortejaar    het jaar waarin de speler geboren is.
   * @param aantalGespeeld  het totaal aantal gespeelde spellen.
   * @param aantalGewonnen  het totaal aantal gewonnen spellen.
   * @throws IllegalArgumentException als de gebruikersnaam of het geboortejaar
   *                                  niet aan de validatievoorwaarden voldoen.
   */
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

  /**
   * Controleert of de gebruikersnaam voldoet aan de eisen: niet leeg en
   * minimaal 5 karakters lang.
   *
   * @param gebruikersnaam de te controleren naam.
   * @throws IllegalArgumentException als de naam leeg is of te kort.
   */
  private void controleerGebruikersnaam(String gebruikersnaam)
  {
    if (gebruikersnaam == null || gebruikersnaam.isBlank())
      throw new IllegalArgumentException("Gebruikersnaam moet ingevuld zijn!");
    else if (gebruikersnaam.length() < 5)
    {
      throw new IllegalArgumentException("Gebruikersnaam moet minstens 5 karakters hebben!");
    }
  }

  /**
   * Reset de status van de speler voor een nieuw spel. De mislukte worpen worden
   * op nul gezet en er wordt een nieuw scoreblad aangemaakt.
   *
   * @param alternatiefModus {@code true} als het scoreblad de alternatieve regels
   *                         moet gebruiken, anders {@code false}.
   */
  public void resetSpeler(boolean alternatiefModus) {
    this.mislukteWorpen = 0;
    maakNieuwScoreBlad(alternatiefModus);
  }

  /**
   * @return het totaal aantal spellen dat deze speler heeft gespeeld.
   */
  public int getAantalGespeeld() { return aantalGespeeld; }
  /**
   * @return het totaal aantal spellen dat deze speler heeft gewonnen.
   */
  public int getAantalGewonnen() { return aantalGewonnen; }
  /**
   * Verhoogt de teller van het aantal gespeelde spellen met één.
   */
  public void verhoogAantalGespeeld() { this.aantalGespeeld++; }
  /**
   * Verhoogt de teller van het aantal gewonnen spellen met één.
   */
  public void verhoogAantalGewonnen() { this.aantalGewonnen++; }

  /**
   * berekent de leeftijd van de speler.
   * @return waarde van huidigJaar - geboortejaar.
   */
  public int berekenLeeftijd() {
    int huidigJaar = LocalDate.now().getYear();
    return huidigJaar - this.geboortejaar;
  }

  /**
   * Controleert of de speler een geldige leeftijd heeft (tussen 8 en 100 jaar).
   *
   * @param geboortejaar het geboortejaar van de speler.
   * @throws IllegalArgumentException als de speler te jong of te oud is.
   */
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
    if (scoreBlad == null) return 0;
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
