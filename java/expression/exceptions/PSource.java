package expression.exceptions;

public class PSource {
    public static char END = '\0';

    private char c;
    protected int pos;

    private final String data;

    public PSource(final String data) {
        this.data = data + END;
    }

    protected char readChar() {
        return data.charAt(pos);
    }

    public char getChar() {
        return c;
    }

    public char nextChar() {
        c = readChar();
        pos++;
        return c;
    }

    public boolean prevIsBracket() {
        int p = pos - 2;
        while (p >= 0 && Character.isWhitespace(data.charAt(p))) {
            p--;
        }
        if (p < 0) return false;
        return data.charAt(p) == '(';
    }

    public ParseException error(final String format, final Object... args) throws ParseException {
        return new ParseException(String.format(format, args));
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
