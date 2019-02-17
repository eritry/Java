package expression.parser;

public class FastString {
    String s;
    int pos;

    public FastString(String t) {
        s = t;
        pos = 0;
    }

    @Override
    public String toString() {
        return s.substring(pos);
    }

    public char first() {
        return s.charAt(pos);
    }

    public void deleteFirst() {
        pos++;
    }

    public boolean isEmpty() {
        return pos >= s.length();
    }
}
