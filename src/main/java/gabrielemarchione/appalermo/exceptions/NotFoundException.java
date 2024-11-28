package gabrielemarchione.appalermo.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

//    public NotFoundException(UUID utenteId) {
//        super("Il record con id" + utenteId + " non è stato trovato");
//    }


}
