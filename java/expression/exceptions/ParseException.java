package expression.exceptions;

public class ParseException extends Exception {
    private final int pos;

    public ParseException(final int pos, final String message) {
        super(message);
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

}
