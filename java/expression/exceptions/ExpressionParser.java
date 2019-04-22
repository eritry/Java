package expression.exceptions;
import expression.expressions.*;
import expression.generic.NumberException;
import expression.operations.*;

public class ExpressionParser<T> implements Parser<T> {
    private PSource source;
    private int cntIn = 0;
    private Operation<T> op;

    public ExpressionParser(final Operation<T> x) {
        op = x;
    }

    public TripleExpression<T> parse(String expression) {
        source = new PSource(expression);
        source.nextChar();

        TripleExpression<T> res = null;
        try {
            res = addDivide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private TripleExpression<T> addDivide() throws NumberException, ParseException {
        skipSpaces();

        TripleExpression<T> acc = mulDiv();
        skipSpaces();
        while (test('+') || test('-')) {
            char sign = source.getChar();
            source.nextChar();

            cntIn++;
            TripleExpression<T> cur = mulDiv();
            cntIn--;

            skipSpaces();
            if (sign == '+') {
                acc = new Add<T>(acc, cur, op);
            } else {
                acc = new Subtract<T>(acc, cur, op);
            }

        }
        return acc;
    }

    private TripleExpression<T> mulDiv() throws ParseException, NumberException {
        TripleExpression<T> cur = unary();
        skipSpaces();

        TripleExpression<T> acc = cur;

        while (test('*') || test('/') || source.extend("mod")) {
            char type = source.getChar();
            if (type == '/' || type == '*') {
                source.nextChar();
            }

            cntIn++;
            cur = unary();
            cntIn--;

            skipSpaces();

            if (type == '*') {
                acc = new Multiply<>(acc, cur, op);
            } else if (type == '/'){
                acc = new Divide<>(acc, cur, op);
            } else {
                acc = new Mod<>(acc, cur, op);
            }
        }
        return acc;
    }

    private TripleExpression<T> unary() throws NumberException, ParseException {
        skipSpaces();

        if (test('-') || test('+')) {

            char sign = source.getChar();
            source.nextChar();
            skipSpaces();

            if (testDigit()) {
                return number(sign);
            } else {
                return new Subtract<>(new Const<>(null), unary(), op);
            }
        } else if (source.extend("abs")) {
            return new Abs<>(unary(), op);
        } else if (source.extend("square")) {
            return new Sqr<>(unary(), op);
        } else if (test('(')) {
            source.nextChar();
            TripleExpression<T> cur = addDivide();
            skipSpaces();
            source.nextChar();
            return cur;
        } else {
            return variable();
        }
    }

    private TripleExpression<T> variable() throws NumberException {
        skipSpaces();

        if (test('-') || testDigit()) {
            return number('+');
        }

        char val = source.getChar();
        source.nextChar();
        return new Variable<>(Character.toString(val));
    }

    private Const<T> number(char c) throws NumberException {
        final StringBuilder sb = new StringBuilder();
        sb.append(c);
        readDigits(sb);
        T res = op.parseNumber(sb.toString());
        return new Const<T>(res);
    }

    private void readDigits(final StringBuilder sb) {
        do {
            sb.append(source.getChar());
        } while (Character.isDigit(source.nextChar()));
    }

    private boolean test(final char c) {
        return source.getChar() == c;
    }

    private boolean testDigit() {
        return Character.isDigit(source.getChar());
    }

    private void skipSpaces() {
        while (Character.isWhitespace(source.getChar())) {
            source.nextChar();
        }
    }
}
