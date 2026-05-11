package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Rij {
    private Kleur kleur;
    private boolean gesloten;
    private List<Vakje> vakjes;
    private int laatsteIndex = -1;
    public static final int TEST_MIN_KRUISJES_VOOR_SLUITEN = 2;
    public static final int MIN_KRUISJES_VOOR_SLUITEN = 5;
    private static final int[] SCORE_PER_AANTAL_KRUISJES = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91};


    /**
     * Maakt een nieuwe rij aan met de opgegeven kleur en vakjes waarden.
     * @param kleur de kleur van rij
     * @param waarden de waarden van de vakjes in de rij
     */
    public Rij(Kleur kleur, int[] waarden) {
        this.kleur = kleur;

        this.vakjes = new ArrayList<>();

        for (int waarde : waarden) {
            vakjes.add(new Vakje(waarde));
        }


    }

    /**
     * Geeft kleur van rij terug.
     * @return de kleur van de rij
     */
    public Kleur getKleur() {
        return kleur;
    }

    /**
     * Controleert of de rij gesloten is
     * @return true als de rij gesloten is anders false
     */
    public boolean isGesloten() {
        return gesloten;
    }

    /**
     * Geeft lijst van vakjes in rij terug.
     * @return een onwijzigbare lijst van vakjes
     */
    public List<Vakje> getVakjes() {
        return Collections.unmodifiableList(vakjes);
    }

    /**
     * Stelt in of de rij gesloten is of niet.
     * @param gesloten true om de rij te sluiten, false om te openen
     */
    public void setGesloten(boolean gesloten) {
        this.gesloten = gesloten;
    }

    /**
     * Zet kruisje op gekozen vakje in rij.
     * Controleert ook of de rij niet gesloten is of als het vakje al aangekruist is
     * of voldoet aan de volgorde van aankruising en of het laatste vakje aangekruist mag worden.
     * @param waardeVak de waarde van het aan te kruisen vakje
     * @return true als het kruisje gezet is anders false
     */
    public boolean registreerKruis(int waardeVak){
        Vakje gekozenVakje;
        if (this.gesloten) {
            return false;
        }
        int gekozenIndex = zoekIndexVanWaarde(waardeVak);
        if (gekozenIndex == -1) {
            return false;
        }
        gekozenVakje = vakjes.get(gekozenIndex);
        if (gekozenVakje.isAangekruist()) {
            return false;
        }
        boolean isUitersteVakje = gekozenIndex == vakjes.size() - 1;
        if (isUitersteVakje && getAantalKruisjes() < TEST_MIN_KRUISJES_VOOR_SLUITEN) {
            return false;
        }
        // enkel rechts van vorige kruisjes mogen gekozen worden
        if (gekozenIndex <= laatsteIndex) {
            return false;
        }
        gekozenVakje.kruisAan();
        laatsteIndex = gekozenIndex;

        if (isUitersteVakje){
            this.gesloten = true;
        }
        return true;

    }

    /**
     * Berekent de score van deze rij op basis van het aantal kruisjes.
     * @return de behaalde score, of 0 als er geen kruisjes zijn
     */
    public int berekenScoreRij(){
        int aantal = getAantalKruisjes();
        if (aantal<=0) return 0;
        if (aantal > SCORE_PER_AANTAL_KRUISJES.length) {
            aantal = SCORE_PER_AANTAL_KRUISJES.length;
        }
        return SCORE_PER_AANTAL_KRUISJES[aantal - 1];
    }

    /**
     * Geeft het totaal aantal aangekruiste vakjes terug.
     * Als de rij gesloten is, wordt het sluitkruisje meegeteld.
     * @return het aantal kruisjes in de rij
     */
    public int getAantalKruisjes() {
        int teller = 0;
        for (Vakje v : vakjes) {
            if (v.isAangekruist()) teller++;
        }
        if (this.gesloten) teller++;
        return teller;
    }

    /**
     * Zoekt de index van een vakje met de opgegeven waarde.
     *
     * @param waarde de waarde waarvan de index gezocht wordt
     * @return de index van het vakje, of -1 als de waarde niet gevonden wordt
     */
    private int zoekIndexVanWaarde(int waarde) {
        for (int i = 0; i < vakjes.size(); i++) {
            if (vakjes.get(i).getWaarde() == waarde) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Controleert of de rij momenteel gesloten mag worden.
     * Dit is toegestaan als de rij nog niet gesloten is, het laatste vakje
     * nog niet aangekruist is, en er minstens 5 kruisjes staan.
     * @return true als aan alle voorwaarden is voldaan, anders false
     */
    public boolean magSluiten() {
        Vakje laatste = vakjes.get(vakjes.size() - 1);
        return !gesloten && !laatste.isAangekruist() && getAantalKruisjes() >= MIN_KRUISJES_VOOR_SLUITEN;
    }
}
