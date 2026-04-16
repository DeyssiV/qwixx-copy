package domein;

/**
 * Stelt een vakje voor op het scoreblad met een waarde en een aangeruiste status.
 */
public class Vakje {
    private int waarde;
    private boolean aangekruist;

    /**
     * Maakt een nieuw vakje aan met de opgegeven waarde.
     * Het vakje is standaard niet aangekruist bij aanmaak.
     *
     * @param waarde De numerieke waarde van het vakje
     */
    public Vakje(int waarde) {
        this.waarde = waarde;
        this.aangekruist = false;
    }

    /**
     * Geeft de waarde van het vakje terug.
     *
     * @return De waarde van het vakje als Integer
     */
    public int getWaarde() {
        return waarde;
    }

    /**
     * Geeft aan of het vakje aangekruist is of niet.
     *
     * @return true als het vakje aangekruist is, anders false
     */
    public boolean isAangekruist() {
        return aangekruist;
    }

    /**
     * Stelt de aangekruiste status van het vakje in.
     *
     * @param aangekruist true om het vakje aan te kruisen, false om het te openen
     */
    public void setAangekruist(boolean aangekruist) {
        this.aangekruist = aangekruist;
    }
}
