package domein;

import java.util.ArrayList;
import java.util.List;

public class Spel {
    List<Speler> spelers;
    List<Dobbelsteen> dobbelstenen;

    public Spel(List<Speler> spelers) {
        this.spelers = spelers;
        this.dobbelstenen = new ArrayList<>();

        //de 6 dobbelstenen volgens spelregels QWIXX toevoegen
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.ROOD));
        dobbelstenen.add(new Dobbelsteen(Kleur.GEEL));
        dobbelstenen.add(new Dobbelsteen(Kleur.GROEN));
        dobbelstenen.add(new Dobbelsteen(Kleur.BLAUW));
    }
}
