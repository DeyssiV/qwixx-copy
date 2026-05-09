package domein;

import utils.Kleur;

import java.util.Random;

public class Dobbelsteen {
    private final Kleur kleur;
    private int waarde;
    private static final Random r = new Random();

    /**
     * Maakt een nieuwe dobbelsteen aan.
     * @param kleur de kleur dat de dobbelsteen moet krijgen.
     */
    public Dobbelsteen(Kleur kleur) {
        this.kleur = kleur;
        rolDobbelsteen();
    }

    /**
     * geeft de kleur van de dobbelsteen.
     * @return de kleur van de dobbelsteen.
     */
    public Kleur getKleur() {
        return kleur;
    }

    /**
     * rolt een random getal tussen 1 en 6.
     * @return een waarde tussen 1 en 6
     */
    public int rolDobbelsteen(){
        int max=6,min=1;
        waarde = r.nextInt(max - min + 1) + min; //Door klasse Random te gebruiken kan je een specifieke interval instellen hier is dat van 1 tem 6 (dobbelsteen)
        return waarde;
    }

    /**
     * geeft de waarde van de dobbelsteen terug.
     * @return de waarde van de dobbelsteen.
     */
    public int getWaarde() {
        return waarde;
    }

}
