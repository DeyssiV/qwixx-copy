package domein;

import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

public class Rij {
    private Kleur kleur;
    private boolean gesloten;
    //private ScoreBlad scoreBlad;
    private List<Vakje> vakjes;
    //private Vakje[] vakjes = new Vakje[11];
    private int laatsteWaarde;


    public Rij(Kleur kleur) {
        this.kleur = kleur;

        this.vakjes = new ArrayList<>();

        if (kleur == Kleur.ROOD || kleur == Kleur.GEEL) {
            for (int i = 2; i <= 12; i++) vakjes.add(new Vakje(i));
            this.laatsteWaarde = 1;
        } else {
            for (int i = 12; i >= 2; i--) vakjes.add(new Vakje(i));
            this.laatsteWaarde = 13;
        }

    }

    public Kleur getKleur() {
        return kleur;
    }

    public boolean isGesloten() {
        return gesloten;
    }

    public List<Vakje> getVakjes() {
        return vakjes;
    }

    public void setGesloten(boolean gesloten) {
        this.gesloten = gesloten;
    }

    public boolean registreerKruis(int waardeVak, boolean isRoodOfGeel){
        for (Vakje v : vakjes) {
            if (v.getWaarde() == waardeVak) {
                if(v.isAangekruist()){
                    System.out.println("  -> FAILED: Vakje is al bezet.");
                    return false;
                }
                boolean isUitersteVakje = (isRoodOfGeel && waardeVak == 12) || (!isRoodOfGeel && waardeVak == 2);
                if(isUitersteVakje && getAantalKruisjes() < 5){
                    System.out.println("  -> FAILED: Je hebt minstens 5 kruisjes nodig om het laatste vakje aan te kruisen!");
                    return false;
                }
                boolean kruisZetten = false;
                if (isRoodOfGeel) {if (waardeVak > laatsteWaarde) {kruisZetten = true;}}
                else if (waardeVak < laatsteWaarde) {kruisZetten = true;}
                if (kruisZetten) {
                    v.setAangekruist(true);
                    this.laatsteWaarde = waardeVak;
                    if (isUitersteVakje) {
                        this.gesloten = true;
                        System.out.println("  -> SUCCESS: Rij gesloten! Je krijgt een extra kruisje.");
                    }
                    System.out.println("  -> SUCCESS: Kruisje gezet.");
                    return true;
                }
                System.out.println("  -> FAILED: Kruisje niet gezet.");
            }
        }
        return false;
    }

    public int berekenScoreRij(int aantal){
        if (aantal<=0) return 0;
        int [] scoreRij = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78};
        return scoreRij[aantal-1];
    }

    public int getAantalKruisjes() {
        int teller = 0;
        for (Vakje v : vakjes) {
            if (v.isAangekruist()) teller++;
        }
        if (this.gesloten) teller++;
        return teller;
    }
}
