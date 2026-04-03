package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

import static utils.Kleur.GEEL;
import static utils.Kleur.ROOD;

public class Rij {
    private Kleur kleur;
    private boolean gesloten;
    private List<Vakje> vakjes;
    private int laatsteWaarde;


    /**
     * Maakt een nieuwe rij aan met specifieke kleur.
     * Volgorde van vakjes bepaald door kleur (rood/geel == oplopend en groen/blauw == aflopend).
     * @param kleur De kleur (Kleur) van rij.
     */
    public Rij(Kleur kleur) {
        this.kleur = kleur;

        this.vakjes = new ArrayList<>();

        //TODO dit in methode plaatsen en in SCOREBLAD klasse
        if (kleur == ROOD || kleur == GEEL) {
            for (int i = 2; i <= 12; i++) vakjes.add(new Vakje(i));
            this.laatsteWaarde = 1;
        } else {
            for (int i = 12; i >= 2; i--) vakjes.add(new Vakje(i));
            this.laatsteWaarde = 13;
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
        return vakjes;
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
     * @param isRoodOfGeel Geeft aan of de rij oplopend of aflopend is obv de kleur van de rij.
     * @return true, false Als het kruisje gezet is true, anders false.
     */
    public boolean registreerKruis(int waardeVak, boolean isRoodOfGeel){
        if (this.gesloten) {
            return false;
        }

        for (Vakje v : vakjes) {
            if (v.getWaarde() == waardeVak) {
                if(v.isAangekruist()){
                    return false;
                }
                //TODO aanpassen zodat het ook voor de alternatieve spel
                boolean isUitersteVakje = (isRoodOfGeel && waardeVak == 12) || (!isRoodOfGeel && waardeVak == 2);

                if(isUitersteVakje && getAantalKruisjes() < 5){
                    return false;
                }
                boolean kruisZetten = false;
                if (isRoodOfGeel) {if (waardeVak > laatsteWaarde) {kruisZetten = true;}}
                else if (waardeVak < laatsteWaarde) {kruisZetten = true;}
                if (kruisZetten) {
                    v.setAangekruist(true);
                    this.laatsteWaarde = waardeVak;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Berekent de score voor gekozen rij o.b.v. aantal gezette kruisjes.
     * Hiervoor wordt een array met de score gebruikt.
     * @param aantal Het totaal aantal kruisjes dat gezet werden in die rij (incl. slot).
     * @return score Behaalde score in de rij in Integer.
     */
    public int berekenScoreRij(int aantal){
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
}
