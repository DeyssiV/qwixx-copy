package exceptions;

public class FouteInvoerException extends RuntimeException {
    public FouteInvoerException() {
        super("Foutieve invoer gedetecteerd.");
    }

    public FouteInvoerException(String message) {
        super(message);
    }
}