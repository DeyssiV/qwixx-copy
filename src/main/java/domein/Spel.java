package domein;

import java.util.ArrayList;
import java.util.List;

public class Spel {
    List<Speler> spelers;
    List<Dobbelsteen> dobbelstenen;
    //Dobbelsteen[] dobbelstenen = new Dobbelsteen[6];
    private Dobbelsteen dobbelsteen;
    private ScoreBlad scoreBlad;

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

    public int geefWaardeDobbelsteen(){
        return dobbelsteen.getWaarde();
    }

    public Kleur geefKleurDobbelsteen(){
        return dobbelsteen.getKleur();
    }

    public void toonDobbelstenen(int waarde, Kleur kleur){
        waarde = geefWaardeDobbelsteen();
        kleur = geefKleurDobbelsteen();
    }

    //TODO : (deyssi) ik zit hier vast, ik weet ni helemaal hoe ik een witte dobbelsteen kan opvragen
    public int toonSomWaardeWitteDobbelstenenEnScoreblad(){
        return geefWaardeDobbelsteen() + scoreBlad.getScore();
    }

    //TODO
    public void toonAlleMogelijkeCombinaties(){

    }


}
