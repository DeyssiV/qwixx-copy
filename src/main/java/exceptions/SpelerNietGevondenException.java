package exceptions;

public class SpelerNietGevondenException extends RuntimeException {
    public SpelerNietGevondenException(String gebruikersnaam) {
        super("Geen speler gevonden met gebruikersnaam: " + gebruikersnaam);
    }
}