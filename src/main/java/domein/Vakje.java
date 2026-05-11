package domein;


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
    }

    /**
     * Geeft de waarde van het vakje terug.
     *
     * @return De waarde van het vakje
     */
     public int getWaarde() {
        return waarde;
    }

    /**
     * Geeft aan of het vakje aangekruist is of niet.
     *
     * @return true als het vakje aangekruist is, anders false
     */
    public boolean isAangekruist() { return aangekruist; }

    /**
     * Kruist het vakje aan.
     *
     */
    public void kruisAan() {
        this.aangekruist = true;
    }
}
