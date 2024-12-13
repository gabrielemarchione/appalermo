package gabrielemarchione.appalermo.exceptions;

public class AuthDeniedException extends RuntimeException {
    public AuthDeniedException(String message) {
        super(message);
    }
}
