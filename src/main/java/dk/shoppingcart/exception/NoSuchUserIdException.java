package dk.shoppingcart.exception;

public class NoSuchUserIdException extends Exception {
    public NoSuchUserIdException(String message) {
        super(message);
    }
}
