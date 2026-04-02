package exceptions;

public class TeWeinigSpelersException extends RuntimeException {
  public TeWeinigSpelersException() {
    super("Er zijn niet genoeg spelers geregistreerd om een spel te starten.");
  }

  public TeWeinigSpelersException(String message) {
    super(message);
  }
}