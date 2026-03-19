package domein;

import utils.Kleur;

import java.util.Random;

public class Dobbelsteen {
    private final Kleur kleur;
    private int waarde;
    Random r = new Random();

    public Dobbelsteen(Kleur kleur) {
        this.kleur = kleur;
    }

    public Kleur getKleur() {
        return kleur;
    }


    public int rolDobbelsteen(){
        int max=6,min=1;
        waarde = r.nextInt(max - min + 1) + min; //Door klasse Random te gebruiken kan je een specifieke interval instellen hier is dat van 1 tem 6 (dobbelsteen)
        return waarde;
    }


    //waarde ophalen van de dobbelsteen
    //-> welke waarde als de waarde eerst moet bepaald worden door het rollen van dobbelsteen??
    public int getWaarde() {
        return waarde;
    }

    public void setWaarde(int waarde) {
        this.waarde = waarde;
    }
}
