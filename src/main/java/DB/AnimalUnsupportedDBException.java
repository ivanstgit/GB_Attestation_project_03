package DB;

public class AnimalUnsupportedDBException extends RuntimeException {

    public AnimalUnsupportedDBException() {
        super();
    }

    public AnimalUnsupportedDBException(String reqDBType) {
        super(reqDBType);
    }

}
