package domein;

import dtos.SpelDTO;
import utils.Kleur;

import java.util.ArrayList;
import java.util.List;

/**
 * Stelt een Spel voor van QWIXX met spelers en dobbelstenen.
 * Beheert de dobbelstenen, spelers en de spellogica.
 */
public class Spel {
    List<Speler> spelers;
    List<Dobbelsteen> dobbelstenen;
    private Dobbelsteen dobbelsteen;
    private ScoreBlad scoreBlad;
    private int aantalDoorkruisteRijen = 0;
    private boolean alternatiefModus;

    /**
     * Maakt een nieuw spel aan met de gegeven spelers en initialiseert
     * de 6 dobbelstenen volgens de QWIXX-spelregels (2 wit, 1 rood, 1 geel, 1 groen, 1 blauw).
     * De spelers worden gesorteerd bij aanmaak.
     * @param spelers De lijst van spelers die deelnemen aan het spel
     */
    public Spel(List<Speler> spelers, boolean alternatiefModus) {
        this.spelers = new ArrayList<>(spelers);
        java.util.Collections.sort(this.spelers);
        this.alternatiefModus = alternatiefModus;
        this.dobbelstenen = new ArrayList<>();

        //de 6 dobbelstenen volgens spelregels QWIXX toevoegen
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.WIT));
        dobbelstenen.add(new Dobbelsteen(Kleur.ROOD));
        dobbelstenen.add(new Dobbelsteen(Kleur.GEEL));
        dobbelstenen.add(new Dobbelsteen(Kleur.GROEN));
        dobbelstenen.add(new Dobbelsteen(Kleur.BLAUW));

        for (Speler speler : spelers) {
            speler.maakNieuwScoreBlad(alternatiefModus);
        }
    }

    /**
     * Geeft het aantal doorkruiste (gesloten) rijen in het spel terug.
     * @return Het aantal doorkruiste rijen als Integer
     */
    public int getAantalDoorkruisteRijen() {
        return aantalDoorkruisteRijen;
    }

    /**
     * Geeft de lijst van alle spelers in het spel terug.
     * @return Een lijst van Speler-objecten
     */
    public List<Speler> getSpelers()
    {
        return spelers;
    }

    /**
     * Geeft de lijst van alle dobbelstenen in het spel terug.
     * @return Een lijst van Dobbelsteen-objecten
     */
    public List<Dobbelsteen> getDobbelstenen()
    {
        return dobbelstenen;
    }

    /**
     * Geeft de huidige actieve dobbelsteen terug.
     *
     * @return De actieve Dobbelsteen
     */
    public Dobbelsteen getDobbelsteen() {
        return dobbelsteen;
    }

    /**
     * Geeft het scoreblad van het huidige spel terug.
     *
     * @return Het ScoreBlad-object van dit spel
     */
    public ScoreBlad getScoreBlad()
    {
        return scoreBlad;
    }

    /**
     * Geeft de waarde van de huidige actieve dobbelsteen terug.
     *
     * @return De waarde van de dobbelsteen als Integer
     */
    public int geefWaardeDobbelsteen(){
        return dobbelsteen.getWaarde();
    }

    /**
     * Geeft de kleur van de huidige actieve dobbelsteen terug.
     *
     * @return De kleur van de dobbelsteen als Kleur
     */
    public Kleur geefKleurDobbelsteen(){
        return dobbelsteen.getKleur();
    }

    /**
     * Geeft een kopie van alle dobbelstenen terug gefilterd op waarde en kleur.
     *
     * @param waarde De waarde waarop gefilterd wordt
     * @param kleur  De kleur waarop gefilterd wordt, mag niet null zijn
     * @return Een lijst van Dobbelsteen-objecten
     * @throws NullPointerException Als de opgegeven kleur null is
     */
    public List<Dobbelsteen> toonDobbelstenen(int waarde, Kleur kleur) {
        if (kleur == null) {
            throw new NullPointerException("Kleur mag niet null zijn");
        }
        return new ArrayList<>(dobbelstenen); // kopie van dobbelstenen
    }

    /**
     * Geeft de waarde terug van de eerste of tweede witte dobbelsteen.
     *
     * @param getal 1 voor de eerste witte dobbelsteen, 2 voor de tweede
     * @return De waarde van de gevraagde witte dobbelsteen, of 0 als niet gevonden
     */
    public int getWitteWaarde(int getal) {
        int teller = 1;
        for (Dobbelsteen d : dobbelstenen) {
            if (d.getKleur() == Kleur.WIT) {
                if (teller == getal) {
                    return d.getWaarde();
                }
                teller++;
            }
        }
        return 0;
    }

    /**
     * Geeft de waarde terug van de dobbelsteen met de opgegeven kleur.
     *
     * @param kleur De kleur van de gezochte dobbelsteen
     * @return De waarde van de gekleurde dobbelsteen, of 0 als niet gevonden
     */
    public int getGekleurdeWaarde(Kleur kleur) {
        for (Dobbelsteen d : dobbelstenen) {
            if (d.getKleur() == kleur) {
                return d.getWaarde();
            }
        }
        return 0;
    }

    /**
     * Rolt alle dobbelstenen in het spel opnieuw.
     * Elke dobbelsteen krijgt een nieuwe willekeurige waarde.
     */
    public void rolDobbelstenen() {
        for (Dobbelsteen dobbelsteen : dobbelstenen) {
            dobbelsteen.rolDobbelsteen();
        }
    }

    /**
     * Geeft de lijst van alle spelers in het spel terug.
     *
     * @return Een lijst van Speler-objecten
     */
    public List<Speler> geefSpelers(){
        return this.spelers;
    }

    /**
     * Berekent alle mogelijke combinaties van de gekleurde dobbelsteen
     * met elk van de twee witte dobbelstenen voor de opgegeven kleur.
     *
     * @param kleur De kleur van de dobbelsteen waarmee gecombineerd wordt
     * @return Een lijst van Integer-waarden met alle mogelijke combinaties
     */
    public List<Integer> berekenCombinaties(Kleur kleur) {
        List<Integer> combinaties = new ArrayList<>();
        int gekleurdeWaarde = getGekleurdeWaarde(kleur);

        for (Dobbelsteen d : dobbelstenen) {
            if (d.getKleur() == Kleur.WIT) {
                combinaties.add(gekleurdeWaarde + d.getWaarde());
            }
        }
        return combinaties;
    }


}
