package domein;

public class Vakje {
    private int waarde;
    private boolean aangekruist;
    private Rij rij;

    public Vakje(int waarde) {
        this.waarde = waarde;
        this.aangekruist = false;
    }

    public int getWaarde() {
        return waarde;
    }

    public boolean isAangekruist() {
        return aangekruist;
    }

    public Rij getRij() {
        return rij;
    }

    public void setAangekruist(boolean aangekruist) {
        this.aangekruist = aangekruist;
    }
}
