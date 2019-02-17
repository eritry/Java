package expression.exceptions;
import java.io.IOException;


public class PSource {
    public static char END = '\0';

    private char c;
    protected int pos;

    private final String data;

    public PSource(final String data) throws ParseException {
        this.data = data + END;
    }

    protected char readChar() throws IOException {
        return data.charAt(pos);
    }

    public char getChar() {
        return c;
    }

    public void back() {
        pos--;
    }

    public char nextChar() throws ParseException {
        try {
            c = readChar();
            pos++;
            return c;
        } catch (final IOException e) {
            throw error("Source read error", e.getMessage());
        }
    }

    public ParseException error(final String format, final Object... args) throws ParseException {
        return new ParseException(pos, String.format("%d: %s", pos, String.format(format, args)));
    }

    public boolean extend(String s) throws ParseException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(getChar());
            if (getChar() == s.charAt(i)) {
                nextChar();
            } else {
                if (i > 0) {
                    throw error("Expected '%s', founded '%s'", s, sb.toString());
                }
                return false;
            }
        }
        return true;
    }
}
