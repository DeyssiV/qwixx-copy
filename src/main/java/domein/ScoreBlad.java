package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class ScoreBlad {
    String gebruikersnaam;
    private int score;
    private List<Rij> rijen;
    private boolean alternatiefModus;
    private static final int[][] STANDAARD_MATRIX = {
            {2,3,4,5,6,7,8,9,10,11,12,0},
            {2,3,4,5,6,7,8,9,10,11,12,0},
            {12,11,10,9,8,7,6,5,4,3,2,0},
            {12,11,10,9,8,7,6,5,4,3,2,0}
    };
    private static final int[][] ALTERNATIEVE_MATRIX = {
            {10,6,2,8,3,4,12,5,9,7,11,0},
            {9,12,4,6,7,2,5,8,11,3,10,0},
            {8,2,10,12,6,9,7,4,5,11,3,0},
            {5,7,11,9,12,3,8,10,2,6,4,0}
    };

    /**
     * Maakt een nieuw scoreblad aan voor gekozen gebruiker.
     * Maakt 4 rijen aan ROOD, GEEL, GROEN en BLAUW
     * @param gebruikersnaam De naam van de gekozen speler die bij deze scoreblad hoort
     */
    public ScoreBlad(String gebruikersnaam, boolean alternatiefModus) {
        this.gebruikersnaam = gebruikersnaam;
        this.alternatiefModus = alternatiefModus;
        this.score = 0;

        int[][] matrix = alternatiefModus
                ? ALTERNATIEVE_MATRIX
                : STANDAARD_MATRIX;

        this.rijen = new ArrayList<>();

        this.rijen.add(new Rij(Kleur.ROOD, matrix[0]));
        this.rijen.add(new Rij(Kleur.GEEL, matrix[1]));
        this.rijen.add(new Rij(Kleur.GROEN, matrix[2]));
        this.rijen.add(new Rij(Kleur.BLAUW, matrix[3]));
    }

    /**
     * Geeft lijst van al de rijen van scoreblad.
     * @return Een lijst van 4 rijen van Rij.
     */
    public List<Rij> getRijen() {
        return rijen;
    }

    /**
     * Geeft huidige score van scoreblad terug.
     * @return Een score in Integer.
     */
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Kruist gekozen vakje in rij door gekozen kleur.
     * Controleert of het oplopende vakjes zijn door de kleur.
     * (oplopend voor rood/geel en aflopend voor groen/blauw)
     * @param kleur De kleur van rij waarin het vakje moet aangekruist worden
     * @param vakje Het getal van vakje dat aangekruist moet worden.
     * @return true, false Als het aankruisen van vakje gelukt is, geeft true anders false.
     */
    public boolean kruisVakje(Kleur kleur, int vakje){
        for (Rij rij : rijen) {
            if (rij.getKleur() == kleur) {
                return rij.registreerKruis(vakje);
            }
        }
        return false;
    }

    /**
     * Maakt een matrix van scoreblad voor de CUI.
     * Elke rij heeft 12 elementen waarbij 1 voor de aangekruist staat en 0 voor open.
     * @return 2D-array Matrix van Integers die de status van scoreblad weergeven, 4 op 12.
     */
    public int[][] getScoreBladMatrix() {
        int[][] matrix = new int[4][12]; // 4 rijen, 12 kolommen
        for (int r = 0; r < rijen.size(); r++) {
            List<Vakje> vakjes = rijen.get(r).getVakjes();
            for (int v = 0; v < vakjes.size(); v++) {
                matrix[r][v] = vakjes.get(v).isAangekruist() ? 1 : 0;
            }
        }
        return matrix;
    }

    /**
     * Berekent de totale score van het scoreblad door de scores per rij op te tellen
     * en daarna de strafpunten door het aantal mislukte worpen ervan af te trekken.
     * @param aantalMislukteWorpen Het aantal keren dat een actieve speler zijn beurt overgeslagen
     *                             (=mislukte worp heeft gedaan)
     * @return eindscore De berekende totale eindscore in Integer
     */
    public int berekenTotaleScore(int aantalMislukteWorpen){
        int totScore = 0;
        for (Rij rij:rijen){
            totScore += rij.berekenScoreRij();
        }
        totScore -= (aantalMislukteWorpen*5);
        return totScore;
    }

    /**
     * Zoekt de rij op die bij de opgegeven kleur hoort.
     * @param kleur de kleur van de gezochte rij.
     * @return de {@link Rij} met de overeenkomstige kleur, of {@code null} als deze niet gevonden wordt.
     */
    private Rij isRijVan(Kleur kleur) {
        for (Rij rij : rijen) {
            if (rij.getKleur() == kleur) return rij;
        }
        return null;
    }

    /**
     * Controleert of de rij van de opgegeven kleur gesloten is.
     * @param kleur de kleur van de te controleren rij.
     * @return {@code true} als de rij bestaat en gesloten is, anders {@code false}.
     */
    public boolean isRijGesloten(Kleur kleur)        { return isRijVan(kleur) != null && isRijVan(kleur).isGesloten(); }

    /**
     * Controleert of de rij van de opgegeven kleur op dit moment mag worden gesloten
     * volgens de spelregels.
     * @param kleur de kleur van de betreffende rij.
     * @return {@code true} als de rij mag sluiten, anders {@code false}.
     */
    public boolean rijMagGeslotenWorden(Kleur kleur) { Rij r = isRijVan(kleur); return r != null && r.magSluiten(); }

    /**
     * Geeft het aantal aangekruiste vakjes terug voor de rij van de opgegeven kleur.
     * @param kleur de kleur van de rij.
     * @return het aantal kruisjes in de rij, of {@code 0} als de rij niet bestaat.
     */
    public int getAantalKruisjes(Kleur kleur)        { Rij r = isRijVan(kleur); return r == null ? 0 : r.getAantalKruisjes(); }



}
