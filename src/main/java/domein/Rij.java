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
    public static final int MIN_KRUISJES_VOOR_SLUITEN = 2;


    /**
     * Maakt een nieuwe rij aan met specifieke kleur.
     * Volgorde van vakjes bepaald door kleur (rood/geel == oplopend en groen/blauw == aflopend).
     * @param kleur De kleur (Kleur) van rij.
     * @param waarden de waarden die gebruikt worden om de vakjes in de rij aan te maken
     */
    public Rij(Kleur kleur, int[] waarden) {
        this.kleur = kleur;

        this.vakjes = new ArrayList<>();

        for (int waarde : waarden) {
            vakjes.add(new Vakje(waarde));
        }


    }

    /**
     * Geeft kleur van rij.
     * @return kleur Kleur van rij in Kleur.
     */
    public Kleur getKleur() {
        return kleur;
    }

    /**
     * Controleert of de rij gesloten is.
     * @return true, false Indien gesloten true, anders false.
     */
    public boolean isGesloten() {
        return gesloten;
    }

    /**
     * Geeft lijst van vakjes.
     * @return vakjes Lijst van vakjes Vakje.
     */
    public List<Vakje> getVakjes() {
        return Collections.unmodifiableList(vakjes);
    }

    /**
     * Stelt in of de rij gesloten is of niet.
     * @param gesloten De status van rij.
     */
    public void setGesloten(boolean gesloten) {
        this.gesloten = gesloten;
    }

    /**
     * Zet kruisje op gekozen vakje in rij.
     * Controleert ook of de rij niet gesloten is of als het vakje al aangekruist is
     * of voldoet aan de volgorde van aankruising en of het laatste vakje aangekruist mag worden.
     * @param waardeVak De waarde in Integer van het vakje dat overeenstemt met het nummer op vakje
     *                  in scoreblad die aangekruist mag worden.
     * @return true, false Als het kruisje gezet is true, anders false.
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
        if (isUitersteVakje && getAantalKruisjes() < MIN_KRUISJES_VOOR_SLUITEN) {
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
     * Berekent de score voor gekozen rij o.b.v. aantal gezette kruisjes.
     * Hiervoor wordt een array met de score gebruikt.
     * @return score Behaalde score in de rij in Integer.
     */
    public int berekenScoreRij(){
        int aantal = getAantalKruisjes();
        if (aantal<=0) return 0;
        int [] scoreRij = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78};
        return scoreRij[aantal-1];
    }

    /**
     * Telt aantal kruisjes op.
     * @return aantal kruisjes Totaal aantal kruisjes in rij als Integer.
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
     * @return de index van het vakje met de opgegeven waarde,
     *         of -1 als de waarde niet gevonden werd
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
     * Controleert of de rij momenteel mag worden afgesloten.
     * Een rij mag alleen worden gesloten als:
     * <ul>
     *   <li>De rij niet al eerder gesloten is.</li>
     *   <li>Het laatste vakje in de rij nog niet is aangekruist.</li>
     *   <li>Er minimaal 5 kruisjes in de rij staan.</li>
     * </ul>
     * @return {@code true} als aan alle voorwaarden is voldaan, anders {@code false}.
     */
    public boolean magSluiten() {
        Vakje laatste = vakjes.get(vakjes.size() - 1);
        return !gesloten && !laatste.isAangekruist() && getAantalKruisjes() >= MIN_KRUISJES_VOOR_SLUITEN;
    }
}
